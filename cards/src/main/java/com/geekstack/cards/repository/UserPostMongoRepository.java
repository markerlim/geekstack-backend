package com.geekstack.cards.repository;

import static com.geekstack.cards.utils.Constants.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.geekstack.cards.model.Comment;
import com.geekstack.cards.model.UserPost;

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

    public List<UserPost> userPostingsByUserId(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Query query = new Query(Criteria.where(F_USERID_REAL).is(userId)).with(pageable);
    
        List<UserPost> posts = mongoTemplate.find(query, UserPost.class, C_USERPOST);
        long total = mongoTemplate.count(new Query(), UserPost.class, C_USERPOST);
    
        return new PageImpl<>(posts, pageable, total).getContent();
    }

    public List<UserPost> userPostingsLikedByUserId(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        Query query = new Query(Criteria.where(F_USERPOST_LISTL).in(userId)).with(pageable);
    
        List<UserPost> posts = mongoTemplate.find(query, UserPost.class, C_USERPOST);
        long total = mongoTemplate.count(new Query(), UserPost.class, C_USERPOST);
    
        return new PageImpl<>(posts, pageable, total).getContent();
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
                new Comment(shortIdStr, comment, userId, LocalDateTime.now()));

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
    public void deleteComment(String postId, String commentId) {
        Query query = new Query(Criteria.where(F_USERPOST_ID).is(postId));
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
     * db.userpost.deleteOne({ postId: "insert postId" });
     * 
     * @param postId
     */
    public void deletePost(String postId) {
        Query query = new Query(Criteria.where(F_USERPOST_ID).is(postId));
        mongoTemplate.remove(query, UserPost.class, C_USERPOST);
    }
}
