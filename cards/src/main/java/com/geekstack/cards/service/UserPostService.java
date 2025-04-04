package com.geekstack.cards.service;

import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geekstack.cards.model.Comment;
import com.geekstack.cards.model.UserPost;
import com.geekstack.cards.repository.UserPostMongoRepository;
import com.geekstack.cards.repository.UserPostMySQLRepository;
import com.google.firebase.messaging.FirebaseMessaging;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class UserPostService {

    @Autowired
    private UserPostMongoRepository userPostMongoRepository;

    @Autowired
    private UserPostMySQLRepository userPostMySQLRepository;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    public List<UserPost> listUserPost(int page, int limit) {
        List<UserPost> userPosts = userPostMongoRepository.userPostingsDefault(page, limit);

        Set<String> uniquePostUserIds = userPosts.stream()
                .map(UserPost::getUserId)
                .collect(Collectors.toSet());

        Set<String> uniqueCommentUserIds = new HashSet<>();
        for (UserPost post : userPosts) {
            List<Comment> comments = post.getListofcomments();
            if (comments != null) {
                comments.stream()
                        .map(Comment::getUserId)
                        .filter(Objects::nonNull)
                        .forEach(uniqueCommentUserIds::add);
            }
        }

        Set<String> allUniqueUserIds = new HashSet<>();
        allUniqueUserIds.addAll(uniquePostUserIds);
        allUniqueUserIds.addAll(uniqueCommentUserIds);

        List<Map<String, Object>> allUserDetails = userPostMySQLRepository
                .batchGetUser(new ArrayList<>(allUniqueUserIds));
        System.out.println("finish user details pull\n");

        Map<String, Map<String, Object>> userMap = new HashMap<>();
        for (Map<String, Object> user : allUserDetails) {
            String userId = (String) user.get("userId");
            userMap.put(userId, user);
        }

        for (UserPost post : userPosts) {
            String postUserId = post.getUserId();
            Map<String, Object> postUser = userMap.get(postUserId);

            if (postUser != null) {
                post.setDisplaypic(getStringOrNull(postUser, "displaypic"));
                post.setName(getStringOrNull(postUser, "name"));
            }

            List<Comment> comments = post.getListofcomments();
            if (comments != null) {
                for (Comment comment : comments) {
                    String commentUserId = comment.getUserId();
                    if (commentUserId != null) {
                        Map<String, Object> commentUser = userMap.get(commentUserId);
                        if (commentUser != null) {
                            comment.setDisplaypic(getStringOrNull(commentUser, "displaypic"));
                            comment.setName(getStringOrNull(commentUser, "name"));
                        }
                    }
                }
            }
        }

        return userPosts;
    }

    public List<UserPost> listUserPostByType(int page, int limit, String type) {
        List<UserPost> userPosts = userPostMongoRepository.userPostingsByType(page, limit, type);

        Set<String> uniquePostUserIds = userPosts.stream()
                .map(UserPost::getUserId)
                .collect(Collectors.toSet());

        Set<String> uniqueCommentUserIds = new HashSet<>();
        for (UserPost post : userPosts) {
            List<Comment> comments = post.getListofcomments();
            if (comments != null) {
                comments.stream()
                        .map(Comment::getUserId)
                        .filter(Objects::nonNull)
                        .forEach(uniqueCommentUserIds::add);
            }
        }

        Set<String> allUniqueUserIds = new HashSet<>();
        allUniqueUserIds.addAll(uniquePostUserIds);
        allUniqueUserIds.addAll(uniqueCommentUserIds);

        List<Map<String, Object>> allUserDetails = userPostMySQLRepository
                .batchGetUser(new ArrayList<>(allUniqueUserIds));
        System.out.println("finish user details pull\n");

        Map<String, Map<String, Object>> userMap = new HashMap<>();
        for (Map<String, Object> user : allUserDetails) {
            String userId = (String) user.get("userId");
            userMap.put(userId, user);
        }

        for (UserPost post : userPosts) {
            String postUserId = post.getUserId();
            Map<String, Object> postUser = userMap.get(postUserId);

            if (postUser != null) {
                post.setDisplaypic(getStringOrNull(postUser, "displaypic"));
                post.setName(getStringOrNull(postUser, "name"));
            }

            List<Comment> comments = post.getListofcomments();
            if (comments != null) {
                for (Comment comment : comments) {
                    String commentUserId = comment.getUserId();
                    if (commentUserId != null) {
                        Map<String, Object> commentUser = userMap.get(commentUserId);
                        if (commentUser != null) {
                            comment.setDisplaypic(getStringOrNull(commentUser, "displaypic"));
                            comment.setName(getStringOrNull(commentUser, "name"));
                        }
                    }
                }
            }
        }

        return userPosts;
    }

    public List<UserPost> listUserPostByUserId(String payload,int page, int limit) throws Exception {
        String userId = firebaseService.verifyIdToken(payload).getUid();
        List<UserPost> userPosts = userPostMongoRepository.userPostingsByUserId(userId, page, limit);

        Set<String> uniquePostUserIds = userPosts.stream()
                .map(UserPost::getUserId)
                .collect(Collectors.toSet());

        Set<String> uniqueCommentUserIds = new HashSet<>();
        for (UserPost post : userPosts) {
            List<Comment> comments = post.getListofcomments();
            if (comments != null) {
                comments.stream()
                        .map(Comment::getUserId)
                        .filter(Objects::nonNull)
                        .forEach(uniqueCommentUserIds::add);
            }
        }

        Set<String> allUniqueUserIds = new HashSet<>();
        allUniqueUserIds.addAll(uniquePostUserIds);
        allUniqueUserIds.addAll(uniqueCommentUserIds);

        List<Map<String, Object>> allUserDetails = userPostMySQLRepository
                .batchGetUser(new ArrayList<>(allUniqueUserIds));
        System.out.println("finish user details pull\n");

        Map<String, Map<String, Object>> userMap = new HashMap<>();
        for (Map<String, Object> user : allUserDetails) {
            userMap.put((String) user.get("userId"), user);
        }

        for (UserPost post : userPosts) {
            String postUserId = post.getUserId();
            Map<String, Object> postUser = userMap.get(postUserId);

            if (postUser != null) {
                post.setDisplaypic(getStringOrNull(postUser, "displaypic"));
                post.setName(getStringOrNull(postUser, "name"));
            }

            List<Comment> comments = post.getListofcomments();
            if (comments != null) {
                for (Comment comment : comments) {
                    String commentUserId = comment.getUserId();
                    if (commentUserId != null) {
                        Map<String, Object> commentUser = userMap.get(commentUserId);
                        if (commentUser != null) {
                            comment.setDisplaypic(getStringOrNull(commentUser, "displaypic"));
                            comment.setName(getStringOrNull(commentUser, "name"));
                        }
                    }
                }
            }
        }

        return userPosts;
    }

    public List<UserPost> listUserPostLikedByUserId(String payload,int page, int limit) throws Exception {
        String userId = firebaseService.verifyIdToken(payload).getUid();
        List<UserPost> userPosts = userPostMongoRepository.userPostingsLikedByUserId(userId, page, limit);

        Set<String> uniquePostUserIds = userPosts.stream()
                .map(UserPost::getUserId)
                .collect(Collectors.toSet());

        Set<String> uniqueCommentUserIds = new HashSet<>();
        for (UserPost post : userPosts) {
            List<Comment> comments = post.getListofcomments();
            if (comments != null) {
                comments.stream()
                        .map(Comment::getUserId)
                        .filter(Objects::nonNull)
                        .forEach(uniqueCommentUserIds::add);
            }
        }

        Set<String> allUniqueUserIds = new HashSet<>();
        allUniqueUserIds.addAll(uniquePostUserIds);
        allUniqueUserIds.addAll(uniqueCommentUserIds);

        List<Map<String, Object>> allUserDetails = userPostMySQLRepository
                .batchGetUser(new ArrayList<>(allUniqueUserIds));
        System.out.println("finish user details pull\n");

        Map<String, Map<String, Object>> userMap = new HashMap<>();
        for (Map<String, Object> user : allUserDetails) {
            userMap.put((String) user.get("userId"), user);
        }

        for (UserPost post : userPosts) {
            String postUserId = post.getUserId();
            Map<String, Object> postUser = userMap.get(postUserId);

            if (postUser != null) {
                post.setDisplaypic(getStringOrNull(postUser, "displaypic"));
                post.setName(getStringOrNull(postUser, "name"));
            }

            List<Comment> comments = post.getListofcomments();
            if (comments != null) {
                for (Comment comment : comments) {
                    String commentUserId = comment.getUserId();
                    if (commentUserId != null) {
                        Map<String, Object> commentUser = userMap.get(commentUserId);
                        if (commentUser != null) {
                            comment.setDisplaypic(getStringOrNull(commentUser, "displaypic"));
                            comment.setName(getStringOrNull(commentUser, "name"));
                        }
                    }
                }
            }
        }

        return userPosts;
    }

    public UserPost getOnePost(String postId) {
        // Get single post instead of list
        UserPost userPost = userPostMongoRepository.getOnePost(postId);
        
        // Collect unique user IDs from post and comments
        Set<String> allUniqueUserIds = new HashSet<>();
        
        // Add post author ID
        if (userPost.getUserId() != null) {
            allUniqueUserIds.add(userPost.getUserId());
        }
    
        // Add comment author IDs
        if (userPost.getListofcomments() != null) {
            userPost.getListofcomments().stream()
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
                user -> user
            ));
    
        // Set post author details
        if (userPost.getUserId() != null) {
            Map<String, Object> postUser = userMap.get(userPost.getUserId());
            if (postUser != null) {
                userPost.setDisplaypic(getStringOrNull(postUser, "displaypic"));
                userPost.setName(getStringOrNull(postUser, "name"));
            }
        }
    
        // Set comment author details
        if (userPost.getListofcomments() != null) {
            for (Comment comment : userPost.getListofcomments()) {
                if (comment.getUserId() != null) {
                    Map<String, Object> commentUser = userMap.get(comment.getUserId());
                    if (commentUser != null) {
                        comment.setDisplaypic(getStringOrNull(commentUser, "displaypic"));
                        comment.setName(getStringOrNull(commentUser, "name"));
                    }
                }
            }
        }
    
        return userPost;
    }
    
    private String getStringOrNull(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }

    // Create a post
    public void createPost(UserPost userPost) {
        userPost.setTimestamp(LocalDateTime.now());
        userPost.setListofcomments(new ArrayList<>());
        userPost.setListoflikes(new ArrayList<>());
        userPostMongoRepository.userPostAction(userPost);
    }

    // Delete a post
    public void deletePost(String postId) {
        userPostMongoRepository.deletePost(postId);
    }

    // Comment on a post by postId
    public String commentPost(String payload) {
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject jobject = reader.readObject();
        JsonObject userObject = jobject.getJsonObject("user");

        String message = "commented on your post.";

        rabbitMQProducer.sendNotificationEvent(jobject.getString("postId"), jobject.getString("posterId"),
                LocalDateTime.now(ZoneOffset.UTC),message, userObject.getString("userId"), userObject.getString("name"), userObject.getString("displaypic"));
        return userPostMongoRepository.addComment(jobject.getString("postId"), jobject.getString("comment"), userObject.getString("userId"));
    }

    // Delete comment from a post where comment is from and delete by commentId
    public void deleteCommmentFromPost(String postId, String commentId) {
        userPostMongoRepository.deleteComment(postId, commentId);
    }

    // Unlike a post
    public void unlikePost(String postId, String userId) {

        userPostMongoRepository.unlikeAPost(postId, userId);

    }

    public void handleLikeEvent(String payload) {
        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject jobject = reader.readObject();
        JsonObject userObject = jobject.getJsonObject("user");
        String message = "liked your post.";

        rabbitMQProducer.sendLikeEvent(jobject.getString("postId"), userObject.getString("userId"));
        rabbitMQProducer.sendNotificationEvent(jobject.getString("postId"), jobject.getString("posterId"),
                LocalDateTime.now(ZoneOffset.UTC),message, userObject.getString("userId"), userObject.getString("name"), userObject.getString("displaypic"));
    }
}
