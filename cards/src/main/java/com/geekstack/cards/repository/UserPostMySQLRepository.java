package com.geekstack.cards.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserPostMySQLRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final static String SQL_GET_USERDETAILS = "SELECT userId ,name, displaypic FROM users WHERE userId IN (%s)";

    public List<Map<String, Object>> batchGetUser(List<String> ids) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        int batchSize = 30;

        for (int i = 0; i < ids.size(); i += batchSize) {
            List<String> batch = ids.subList(i, Math.min(i + batchSize, ids.size()));

            String placeholders = String.join(",", Collections.nCopies(batch.size(), "?"));

            String sql = String.format(SQL_GET_USERDETAILS, placeholders);

            List<Map<String, Object>> batchResults = jdbcTemplate.queryForList(sql, batch.toArray());

            resultList.addAll(batchResults);
        }

        return resultList;
    }

    /**
     * Archived
     * private final static String SQL_LIKE_COUNT = "SELECT COUNT(*) FROM userlikes
     * WHERE postId = ?";
     * 
     * private final static String SQL_CREATE_COMMENT = "INSERT INTO
     * usercomments(postId,userId,comments,timestamp) VALUES (?,?,?,?)";
     * 
     * private final static String SQL_LIKE_POST = "INSERT INTO
     * userlikes(postId,userId) VALUES (?,?)";
     * 
     * private final static String SQL_CREATE_POST = "INSERT INTO
     * userpost(postId,userId,timestamp) VALUES (?,?,?)";
     * 
     * public Map<String, List<Comment>> listOfComments(List<String> postIds) {
     * List<Comment> loc = new ArrayList<>();
     * final StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM
     * usercomments WHERE postId IN (");
     * 
     * for (int i = 0; i < postIds.size(); i++) {
     * sqlBuilder.append("?");
     * if (i < postIds.size() - 1) {
     * sqlBuilder.append(", ");
     * }
     * }
     * 
     * sqlBuilder.append(")");
     * 
     * final String SQL_COMMENTS_BY_POSTID = sqlBuilder.toString();
     * SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_COMMENTS_BY_POSTID,
     * postIds.toArray());
     * Map<String, List<Comment>> set = new HashMap<>();
     * while (rs.next()) {
     * Comment comment = new Comment();
     * comment.setComment(rs.getString("comments"));
     * Timestamp timestamp = rs.getTimestamp("timestamp");
     * comment.setTimestamp(timestamp.toLocalDateTime().toLocalDate());
     * comment.setCommentId(rs.getString("commentId"));
     * comment.setUserId(rs.getString("userId"));
     * comment.setPostId(rs.getString("postId"));
     * loc.add(comment);
     * }
     * for (String s : postIds) {
     * List<Comment> newlist = new ArrayList<>();
     * newlist = loc.stream()
     * .filter(comment -> comment.getPostId().equals(s))
     * .collect(Collectors.toList());
     * set.put(s, newlist);
     * }
     * return set;
     * }
     * 
     * public Map<String, List<String>> listOfLikes(List<String> postIds) {
     * List<LikeAction> lolikes = new ArrayList<>();
     * final StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM userlikes
     * WHERE postId IN (");
     * 
     * for (int i = 0; i < postIds.size(); i++) {
     * sqlBuilder.append("?");
     * if (i < postIds.size() - 1) {
     * sqlBuilder.append(", ");
     * }
     * }
     * 
     * sqlBuilder.append(")");
     * 
     * final String SQL_LIKES_BY_POSTID = sqlBuilder.toString();
     * SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_LIKES_BY_POSTID,
     * postIds.toArray());
     * Map<String, List<String>> set = new HashMap<>();
     * 
     * while (rs.next()) {
     * LikeAction likeAction = new LikeAction(rs.getString("likerId"),
     * rs.getString("postId"),
     * rs.getString("userId"));
     * lolikes.add(likeAction);
     * }
     * 
     * for (String s : postIds) {
     * List<String> newlist = new ArrayList<>();
     * newlist = lolikes.stream()
     * .filter(likeObj -> likeObj.getPostId().equals(s)) // Filter by postId
     * .map(LikeAction::getUserId) // Extract only userId
     * .collect(Collectors.toList());
     * 
     * set.put(s, newlist);
     * }
     * return set;
     * }
     * 
     * public Map<String, LocalDate> listOfTimestamp(List<String> postIds) {
     * final StringBuilder sqlBuilder = new StringBuilder("SELECT postId, timestamp
     * FROM userpost WHERE postId IN (");
     * 
     * for (int i = 0; i < postIds.size(); i++) {
     * sqlBuilder.append("?");
     * if (i < postIds.size() - 1) {
     * sqlBuilder.append(", ");
     * }
     * }
     * 
     * sqlBuilder.append(")");
     * 
     * final String SQL_POST_TIMESTAMP = sqlBuilder.toString();
     * 
     * SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_POST_TIMESTAMP,
     * postIds.toArray());
     * Map<String, LocalDate> set = new HashMap<>();
     * while (rs.next()) {
     * set.put(rs.getString("postId"),
     * rs.getTimestamp("timestamp").toLocalDateTime().toLocalDate());
     * }
     * return set;
     * }
     * 
     * public Long numberoflikes(String postId) {
     * return jdbcTemplate.queryForObject(SQL_LIKE_COUNT, Long.class, postId);
     * }
     * 
     * // Update: Create Post
     * public Boolean createUserPost(String postId, String userId, LocalDate
     * timestamp) {
     * int count = jdbcTemplate.update(SQL_CREATE_POST, postId, userId, timestamp);
     * 
     * if (count == 0) {
     * return false;
     * }
     * 
     * return true;
     * }
     * 
     * // Update: Commment
     * public Boolean postCommentByPostId(String postId, Comment comment) {
     * int count = jdbcTemplate.update(SQL_CREATE_COMMENT, postId,
     * comment.getUserId(), comment.getComment(),
     * comment.getTimestamp());
     * 
     * if (count == 0) {
     * return false;
     * }
     * 
     * return true;
     * }
     * 
     * // Update: Likes
     * public Boolean likeByPostId(String postId, String userId) {
     * int count = jdbcTemplate.update(SQL_LIKE_POST, postId, userId);
     * if (count == 0) {
     * return false;
     * }
     * 
     * return true;
     * }
     */
}
