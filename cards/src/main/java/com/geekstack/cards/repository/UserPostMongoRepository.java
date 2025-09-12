package com.geekstack.cards.repository;

import static com.geekstack.cards.utils.Constants.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.geekstack.cards.exception.DocumentNotFoundException;
import com.geekstack.cards.model.Comment;
import com.geekstack.cards.model.UserPost;
import com.mongodb.client.result.DeleteResult;

@Repository
public class UserPostMongoRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     */
    public List<UserPost> userPostingsDefault(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Query query = new Query().with(pageable);

        List<UserPost> posts = mongoTemplate.find(query, UserPost.class, C_USERPOST);
        long total = mongoTemplate.count(new Query(), UserPost.class, C_USERPOST);

        return new PageImpl<>(posts, pageable, total).getContent();
    }

    public List<UserPost> userPostingsByType(int page, int size, String type) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Query query = new Query(Criteria.where(F_USERPOST_TYPE).is(type)).with(pageable);

        List<UserPost> posts = mongoTemplate.find(query, UserPost.class, C_USERPOST);
        long total = mongoTemplate.count(new Query(), UserPost.class, C_USERPOST);

        return new PageImpl<>(posts, pageable, total).getContent();
    }

    public List<UserPost> userPostingsByUserId(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Query query = new Query(Criteria.where(F_USERID_REAL).is(userId)).with(pageable);

        List<UserPost> posts = mongoTemplate.find(query, UserPost.class, C_USERPOST);
        long total = mongoTemplate.count(new Query(), UserPost.class, C_USERPOST);

        return new PageImpl<>(posts, pageable, total).getContent();
    }

    public List<UserPost> userPostingsBySearchTerm(String term, int page, int size) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage()
                .matchingPhrase(term);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Query query = TextQuery.queryText(textCriteria).with(pageable);
        List<UserPost> posts = mongoTemplate.find(query, UserPost.class, C_USERPOST);
        long total = mongoTemplate.count(TextQuery.queryText(textCriteria), UserPost.class, C_USERPOST);

        return new PageImpl<>(posts, pageable, total).getContent();
    }

    public List<UserPost> userPostingsLikedByUserId(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Query query = new Query(Criteria.where(F_USERPOST_LISTL).in(userId)).with(pageable);

        List<UserPost> posts = mongoTemplate.find(query, UserPost.class, C_USERPOST);
        long total = mongoTemplate.count(new Query(), UserPost.class, C_USERPOST);

        return new PageImpl<>(posts, pageable, total).getContent();
    }

    public List<UserPost> userPostingsByTypeAndCode(int page, int size, String type, String code) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Query query = new Query(Criteria.where(F_USERPOST_TYPE).is(type).and(F_USERPOST_CODE).is(code.toLowerCase()))
                .with(pageable);

        List<UserPost> posts = mongoTemplate.find(query, UserPost.class, C_USERPOST);
        long total = mongoTemplate.count(new Query(), UserPost.class, C_USERPOST);

        return new PageImpl<>(posts, pageable, total).getContent();
    }

    public List<UserPost> userPostingsByTypeAndSearchTerm(String term, int page, int size, String type) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage()
                .matchingPhrase(term);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Query query = TextQuery.queryText(textCriteria)
                .addCriteria(Criteria.where(F_USERPOST_TYPE).is(type))
                .with(pageable);
        List<UserPost> posts = mongoTemplate.find(query, UserPost.class, C_USERPOST);
        long total = mongoTemplate.count(TextQuery.queryText(textCriteria), UserPost.class, C_USERPOST);

        return new PageImpl<>(posts, pageable, total).getContent();
    }

    public List<UserPost> userPostingsByTypeAndCodeAndSearchTerm(String term, int page, int size, String type,
            String code) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage()
                .matchingPhrase(term);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Query query = TextQuery.queryText(textCriteria)
                .addCriteria(Criteria.where(F_USERPOST_TYPE).is(type).and(F_USERPOST_CODE).is(code.toLowerCase()))
                .with(pageable);
        List<UserPost> posts = mongoTemplate.find(query, UserPost.class, C_USERPOST);
        long total = mongoTemplate.count(TextQuery.queryText(textCriteria), UserPost.class, C_USERPOST);

        return new PageImpl<>(posts, pageable, total).getContent();
    }

    public UserPost getOnePost(String postId) {
        Query query = new Query(Criteria.where(F_USERPOST_ID).is(postId));
        return mongoTemplate.findOne(query, UserPost.class, C_USERPOST);
    }

    /**
     * db.userpost.insertOne({userpost object})
     * 
     * @param userpost
     */
    public void userPostAction(UserPost userpost) {
        mongoTemplate.insert(userpost, C_USERPOST);
    }

    /**
     * db.userposts.updateOne(
     * { "postId": "post123" },
     * {
     * $push: {
     * "listofcomments": {
     * "commentId": "comment003",
     * "comment": "This deck is awesome!",
     * "userId": "user999",
     * "timestamp": "2024-02-19"
     * }
     * }
     * }
     * );
     * 
     * @param postId
     * @param comment
     */
    public String addComment(String postId, String comment, String userId) {
        Query query = new Query(Criteria.where(F_USERPOST_ID).is(postId));
        long shortId = (long) (Math.random() * 100_000_000);
        String shortIdStr = String.format("%08d", shortId);

        Update update = new Update().push(F_USERPOST_LISTC,
                new Comment(shortIdStr, comment, userId, ZonedDateTime.now(ZoneOffset.UTC)));

        mongoTemplate.updateFirst(query, update, UserPost.class, C_USERPOST);
        return shortIdStr;
    }

    /**
     * db.userposts.updateOne(
     * { "postId": "post123" },
     * {
     * $pull: {
     * "listofcomments": {
     * "commentId": "comment003"
     * }
     * }
     * }
     * );
     * 
     * @param postId
     * @param commentId
     */
    public void deleteComment(String postId, String commentId, String userId) {
        // Validate inputs
        if (!StringUtils.hasText(postId)) {
            throw new IllegalArgumentException("Post ID cannot be empty");
        }
        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }

        // Create query to match both postId AND userId
        Query query = new Query().addCriteria(
                new Criteria().andOperator(
                        Criteria.where(F_USERPOST_ID).is(postId),
                        Criteria.where(F_USERID_REAL).is(userId)));
        Update update = new Update().pull(F_USERPOST_LISTC, Query.query(Criteria.where(F_USERPOST_CID).is(commentId)));

        mongoTemplate.updateFirst(query, update, C_USERPOST);
    }

    /**
     * db.userposts.updateOne(
     * { "postId": "post123" },
     * {
     * $addToSet: { "listoflikes": "user999" }
     * }
     * );
     * 
     * @param postId
     * @param userId
     */
    public void likeAPost(String postId, String userId) {
        Query query = new Query(Criteria.where(F_USERPOST_ID).is(postId));
        Update update = new Update().addToSet(F_USERPOST_LISTL, userId);
        mongoTemplate.updateFirst(query, update, UserPost.class, C_USERPOST);
    }

    /**
     * db.userposts.updateMany(
     * { "postId": { $in: ["post123", "post456", "post789"] } },
     * { $addToSet: { "listoflikes": "user999" } }
     * );
     * 
     * @param postIds
     * @param userId
     */
    public void likeMultiplePosts(List<String> postIds, String userId) {
        Query query = new Query(Criteria.where(F_USERPOST_ID).in(postIds));
        Update update = new Update().addToSet(F_USERPOST_LISTL, userId);
        mongoTemplate.updateMulti(query, update, UserPost.class, C_USERPOST);
    }

    /**
     * db.userposts.updateOne(
     * { "postId": "post123" },
     * {
     * $pull: { "listoflikes": "user999" }
     * }
     * );
     * 
     * @param postId
     * @param userId
     */
    public void unlikeAPost(String postId, String userId) {
        Query query = new Query(Criteria.where(F_USERPOST_ID).is(postId));
        Update update = new Update().pull(F_USERPOST_LISTL, userId);
        mongoTemplate.updateFirst(query, update, UserPost.class, C_USERPOST);
    }

    /**
     * db.userposts.updateMany(
     * { "postId": { $in: ["post123", "post456", "post789"] } },
     * {
     * $pull: { "listoflikes": "user999" }
     * }
     * );
     * 
     * @param postId
     * @param userId
     */
    public void unlikeMultiplePost(List<String> postIds, String userId) {
        Query query = new Query(Criteria.where(F_USERPOST_ID).in(postIds));
        Update update = new Update().pull(F_USERPOST_LISTL, userId);
        mongoTemplate.updateFirst(query, update, UserPost.class, C_USERPOST);
    }

    /**
     * Deletes a post by its ID after verifying ownership
     * Example MongoDB command:
     * db.userpost.deleteOne({
     * postId: "actual_post_id",
     * userId: "authenticated_user_id"
     * });
     * 
     * @param postId The ID of the post to delete
     * @param userId The ID of the authenticated user (for ownership verification)
     */
    public Map<String,Object> deletePost(String postId, String userId) {
        // Validate inputs
        if (!StringUtils.hasText(postId)) {
            throw new IllegalArgumentException("Post ID cannot be empty");
        }
        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("User ID cannot be empty");
        }

        // Create query to match both postId AND userId
        Query query = new Query().addCriteria(
                new Criteria().andOperator(
                        Criteria.where(F_USERPOST_ID).is(postId),
                        Criteria.where(F_USERID_REAL).is(userId)));

        UserPost post = mongoTemplate.findOne(query, UserPost.class, C_USERPOST);

        if (post == null) {
            throw new DocumentNotFoundException("Post not found or not owned by user");
        }

        // Extract selectedCover and content
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        result.put("selectedCover", post.getSelectedCover());
        result.put("content", post.getContent());

        DeleteResult deleteResult = mongoTemplate.remove(query, UserPost.class, C_USERPOST);

        if (deleteResult.getDeletedCount() == 0) {
            throw new DocumentNotFoundException("Post not found or not owned by user");
        }

        return result;
    }
}
