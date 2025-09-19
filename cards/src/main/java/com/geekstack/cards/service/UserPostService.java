package com.geekstack.cards.service;

import java.io.StringReader;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.geekstack.cards.model.Comment;
import com.geekstack.cards.model.UserPost;
import com.geekstack.cards.model.UserPostFinal;
import com.geekstack.cards.repository.UserPostMongoRepository;
import com.geekstack.cards.repository.UserPostMySQLRepository;

import jakarta.json.Json;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class UserPostService {
    private final static Logger logger = LoggerFactory.getLogger(UserPostService.class);

    @Autowired
    private UserPostMongoRepository userPostMongoRepository;

    @Autowired
    private UserPostMySQLRepository userPostMySQLRepository;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @Autowired
    private GoogleCloudStorageService googleCloudStorageService;

    @SuppressWarnings("unchecked")
    private <T> List<T> castList(Object obj, Class<T> clazz) {
        if (obj instanceof List<?>) {
            List<?> list = (List<?>) obj;
            if (list.isEmpty() || clazz.isInstance(list.get(0))) {
                return (List<T>) list;
            }
            throw new ClassCastException("List contains wrong types");
        }
        throw new ClassCastException("Not a List");
    }

    public List<UserPostFinal> listUserPost(int page, int limit, String commands) {
        List<UserPost> userPosts = new ArrayList<>();
        String[] loc = commands != null ? commands.split(":", 4) : new String[] {};
        if (loc.length == 0) {
            userPosts = userPostMongoRepository.userPostingsDefault(page, limit);
        }

        switch (loc[0]) {
            case "SEARCH":
                if (loc.length >= 2) {
                    userPosts = userPostMongoRepository.userPostingsBySearchTerm(loc[1], page, limit);
                }
                break;
            case "SEARCHANDTYPE":
                if (loc.length == 3) {
                    userPosts = userPostMongoRepository.userPostingsByTypeAndSearchTerm(loc[2], page, limit, loc[1]);
                } else if (loc.length >= 4) {
                    userPosts = userPostMongoRepository.userPostingsByTypeAndCodeAndSearchTerm(loc[2], page, limit,
                            loc[1], loc[3]);
                }
                break;
            case "CATEGORY":
                if (loc.length >= 2) {
                    userPosts = userPostMongoRepository.userPostingsByType(page, limit, loc[1]);
                }
                break;
            case "CATEGORYANDCODE":
                if (loc.length >= 3) {
                    userPosts = userPostMongoRepository.userPostingsByTypeAndCode(page, limit, loc[1], loc[2]);
                }
                break;
            case "USERPOST":
                if (loc.length >= 2) {
                    userPosts = userPostMongoRepository.userPostingsByUserId(loc[1], page, limit);
                }
                break;
            case "LIKEDPOST":
                if (loc.length >= 2) {
                    userPosts = userPostMongoRepository.userPostingsLikedByUserId(loc[1], page, limit);
                }
                break;
            default:
                userPosts = userPostMongoRepository.userPostingsDefault(page, limit);
        }
        Set<String> uniquePostUserIds = userPosts.stream()
                .map(UserPost::getUserId)
                .collect(Collectors.toSet());

        Set<String> allUniqueUserIds = new HashSet<>();
        allUniqueUserIds.addAll(uniquePostUserIds);

        List<Map<String, Object>> allUserDetails = userPostMySQLRepository
                .batchGetUser(new ArrayList<>(allUniqueUserIds));
        System.out.println("finish user details pull\n");

        Map<String, Map<String, Object>> userMap = new HashMap<>();
        for (Map<String, Object> user : allUserDetails) {
            String userId = (String) user.get("userId");
            userMap.put(userId, user);
        }
        List<UserPostFinal> result = new ArrayList<>();
        for (UserPost upost : userPosts) {
            UserPostFinal post = new UserPostFinal(upost);
            String postUserId = post.getUserId();
            String postId = post.getPostId();
            Map<String, Object> postUser = userMap.get(postUserId);

            if (postUser != null) {
                post.setDisplaypic(getStringOrNull(postUser, "displaypic"));
                post.setName(getStringOrNull(postUser, "name"));
            }
            Map<String, Object> engagement = userPostMySQLRepository.getCommentsAndLikes(postId);
            post.setListofcomments(castList(engagement.get("comments"), Comment.class));
            post.setListoflikes(castList(engagement.get("likes"), String.class));
            result.add(post);
        }
        return result;
    }

    public UserPostFinal getOnePost(String postId) {
        // Get single post instead of list
        UserPost userPost = userPostMongoRepository.getOnePost(postId);
        UserPostFinal post = new UserPostFinal(userPost);
        Map<String, Object> engagement = userPostMySQLRepository.getCommentsAndLikes(postId);
        post.setListofcomments(castList(engagement.get("comments"), Comment.class));
        post.setListoflikes(castList(engagement.get("likes"), String.class));
        // Collect unique user IDs from post and comments
        Set<String> allUniqueUserIds = new HashSet<>();

        // Add post author ID
        if (userPost.getUserId() != null) {
            allUniqueUserIds.add(userPost.getUserId());
        }

        // Add comment author IDs
        if (post.getListofcomments() != null) {
            post.getListofcomments().stream()
                    .map(Comment::getUserId)
                    .filter(Objects::nonNull)
                    .forEach(allUniqueUserIds::add);
        }

        // Batch fetch user details
        List<Map<String, Object>> allUserDetails = userPostMySQLRepository
                .batchGetUser(new ArrayList<>(allUniqueUserIds));

        System.out.println("Finished user details pull for " + allUniqueUserIds.size() + " users\n");

        // Create user map for easy lookup
        Map<String, Map<String, Object>> userMap = allUserDetails.stream()
                .collect(Collectors.toMap(
                        user -> (String) user.get("userId"),
                        user -> user));

        // Set post author details
        if (userPost.getUserId() != null) {
            Map<String, Object> postUser = userMap.get(userPost.getUserId());
            if (postUser != null) {
                userPost.setDisplaypic(getStringOrNull(postUser, "displaypic"));
                userPost.setName(getStringOrNull(postUser, "name"));
            }
        }

        // Set comment author details
        if (post.getListofcomments() != null) {
            for (Comment comment : post.getListofcomments()) {
                if (comment.getUserId() != null) {
                    Map<String, Object> commentUser = userMap.get(comment.getUserId());
                    if (commentUser != null) {
                        comment.setDisplaypic(getStringOrNull(commentUser, "displaypic"));
                        comment.setName(getStringOrNull(commentUser, "name"));
                    }
                }
            }
        }

        return post;
    }

    private String getStringOrNull(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }

    // Create a post
    public void createPost(UserPost userPost) {
        userPost.setTimestamp(ZonedDateTime.now());
        userPostMongoRepository.userPostAction(userPost);
    }

    // Delete a post
    public void deletePost(String postId, String userId) {
        Map<String, Object> holder = userPostMongoRepository.deletePost(postId, userId);
        List<String> urlArray = new ArrayList<>();

        // Process selectedCover
        Object selectedCoverObj = holder.get("selectedCover");
        if (selectedCoverObj instanceof String) {
            String selectedCover = (String) selectedCoverObj;
            if (selectedCover.startsWith("https://storage.googleapis.com/") && 
                selectedCover.contains("/userpost/")) {
                urlArray.add(selectedCover);
            }
        }

        // Process content HTML for image URLs
        Object contentObj = holder.get("content");
        if (contentObj instanceof String) {
            String content = (String) contentObj;
            String regex = "<img[^>]+src=[\"'](https://storage\\.googleapis\\.com/[^\"']+/userpost/[^\"'>]+)[\"']";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(content);
            while (matcher.find()) {
                urlArray.add(matcher.group(1));
            }
        }

        // Only delete images if there are URLs to delete
        if (!urlArray.isEmpty()) {
            if (googleCloudStorageService.deleteImages(urlArray)) {
                logger.info("Successfully deleted {} images from GCS for postId: {}", urlArray.size(), postId);
            } else {
                logger.warn("Failed to delete some or all images from GCS for postId: {}", postId);
            }
        } else {
            logger.info("No images to delete from GCS for postId: {}", postId);
        }
    }

    // Comment on a post by postId
    public Comment commentPost(String payload, String userId) {
        logger.info("Attempting to create comment for user: {}", userId);
        try {
            JsonReader reader = Json.createReader(new StringReader(payload));
            JsonObject jobject = reader.readObject();

            // Validate required fields
            String commentId = UUID.randomUUID().toString();
            String postId = jobject.getString("postId");
            String comment = jobject.getString("comment");
            String posterId = jobject.getString("posterId");
            ZonedDateTime timestamp = ZonedDateTime.now(ZoneOffset.UTC);

            if (postId == null || comment == null || posterId == null) {
                throw new IllegalArgumentException("Missing required fields in payload");
            }
            Comment returnComment = userPostMySQLRepository.createAndGetComment(commentId, postId, userId, comment,
                    timestamp);
            String rabbitMQMSG = "commented on your post.";
            // Send notification
            logger.debug("Sending notification for post: {}", postId);
            rabbitMQProducer.sendNotificationEvent(
                    postId,
                    posterId,
                    timestamp,
                    rabbitMQMSG,
                    userId,
                    returnComment.getName(),
                    returnComment.getDisplaypic());

            return returnComment;

        } catch (JsonException e) {
            logger.error("Invalid JSON payload: {}", payload, e);
            throw new IllegalArgumentException("Invalid comment payload", e);
        } catch (DataAccessException e) {
            logger.error("Database error while processing comment", e);
            throw new RuntimeException("Failed to process comment", e);
        } catch (Exception e) {
            logger.error("Unexpected error in commentPost", e);
            throw new RuntimeException("Comment processing failed", e);
        }
    }

    // Get all comments from a post (Most likely not used)
    public List<Comment> getListOfComments(String postId) {
        return userPostMySQLRepository.getListOfComments(postId);
    }

    // Get all likes from a post (Most likely not used)
    public List<String> getListOfLikes(String postId) {
        return userPostMySQLRepository.getListOfLikes(postId);
    }

    // Delete comment from a post where comment is from and delete by commentId
    public void deleteCommmentFromPost(String commentId, String userId) {
        userPostMySQLRepository.deleteComment(commentId, userId);
    }

    // Unlike a post
    public void unlikePost(String postId, String userId) {
        userPostMySQLRepository.unlikePost(postId, userId);
    }

    // Liking a post
    public void handleLikeEvent(String postId, String posterId, String userId, String displaypic, String name) {

        String message = "liked your post.";

        // For likes to be batch processed
        rabbitMQProducer.sendLikeEvent(postId, userId);

        // For notifications to be sent
        rabbitMQProducer.sendNotificationEvent(postId, posterId,
                ZonedDateTime.now(ZoneOffset.UTC), message, userId,
                name, displaypic);
    }
}
