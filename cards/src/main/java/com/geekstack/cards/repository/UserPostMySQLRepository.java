package com.geekstack.cards.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.geekstack.cards.model.Comment;
import com.geekstack.cards.model.Likes;

@Repository
public class UserPostMySQLRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final static String SQL_GET_USERDETAILS = "SELECT userId ,name, displaypic FROM users WHERE userId IN (%s)";

    private final static String SQL_CREATE_COMMENT = "INSERT INTO comments(commentId,postId,userId,comments,timestamp) VALUES (?,?,?,?,?)";

    private final static String SQL_GETBY_COMMENTID = "SELECT c.commentId, c.postId, c.userId, c.comments, c.timestamp, "
            +
            "u.name, u.displaypic " +
            "FROM comments c " +
            "JOIN users u ON c.userId = u.userId " +
            "WHERE c.commentId = ?";

    private final static String SQL_GET_COMMENTS = "SELECT c.commentId, c.postId, c.userId, c.comments, c.timestamp, " +
            "u.name, u.displaypic " +
            "FROM comments c " +
            "JOIN users u ON c.userId = u.userId " +
            "WHERE c.postId = ?";

    private final static String SQL_LIKED_POST = "INSERT INTO likes(likedPostId,userId) VALUES (?,?)";

    private static final String SQL_UNLIKED_POST = "DELETE FROM likes WHERE likedPostId = ? AND userId = ?";

    private final static String SQL_GET_LISTOFLIKES = "SELECT postId,userId FROM likes WHERE postId = ?";

    private final static String SQL_GET_LISTOFLIKES_WITH_USERDETAILS = "SELECT l.postId, l.userId," +
            "u.name,u.displaypic" +
            "FROM likes l " +
            "JOIN users u ON l.userId = u.userId " +
            "WHERE l.postId = ?";
    
    private static final String SQL_DELETE_COMMENT = "DELETE FROM comments WHERE commentId = ? and userId = ?";

    // Combined query using UNION ALL
    private static final String SQL_LIKES_COMMENTS_UNION = "SELECT 'comment' as type, c.commentId, c.userId, c.comments, c.timestamp, "
            +
            "u.name, u.displaypic " +
            "FROM comments c JOIN users u ON c.userId = u.userId " +
            "WHERE c.postId = ? " +
            "UNION ALL " +
            "SELECT 'like' as type, NULL as commentId, l.userId, NULL as comments, " +
            "NULL as timestamp, NULL as name, NULL as displaypic " +
            "FROM likes l " +
            "WHERE l.likedPostId = ?";

    /**
     * Batch fetch user details
     * 
     * @param ids
     * @return
     */
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
     * Creates comment for post by postId
     * 
     * @param commentId
     * @param postId
     * @param userId
     * @param comments
     * @param timestamp
     * @return True for comment creation success, False for comment creation fail
     */
    public boolean createComment(String commentId, String postId, String userId, String comments,
            LocalDateTime timestamp) {
        int rowsUpdated = jdbcTemplate.update(SQL_CREATE_COMMENT, commentId, postId, userId, comments, timestamp);
        return rowsUpdated > 0;
    }

    /**
     * Creates comment for post by postId
     * 
     * @param commentId
     * @param postId
     * @param userId
     * @param comments
     * @param timestamp
     * @return Comment Object
     */
    public Comment createAndGetComment(String commentId, String postId, String userId,
            String comments, ZonedDateTime timestamp) {
        try {
            jdbcTemplate.update(SQL_CREATE_COMMENT,
                    commentId, postId, userId, comments, timestamp);

            return jdbcTemplate.queryForObject(
                    SQL_GETBY_COMMENTID,
                    (rs, rowNum) -> {
                        Comment comment = new Comment();
                        comment.setCommentId(rs.getString("commentId"));
                        comment.setUserId(rs.getString("userId"));
                        comment.setComment(rs.getString("comments"));
                        Object timestampObj = rs.getTimestamp("timestamp");
                        ZonedDateTime zonedDateTime = timestampObj != null
                                ? ((Timestamp) timestampObj).toInstant()
                                        .atZone(ZoneId.systemDefault())
                                : null;
                        comment.setTimestamp(zonedDateTime);
                        comment.setName(rs.getString("name"));
                        comment.setDisplaypic(rs.getString("displaypic"));
                        return comment;
                    },
                    commentId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean deleteComment(String commentId, String userId){
        int rowsUpdated = jdbcTemplate.update(SQL_DELETE_COMMENT, commentId, userId);
        return rowsUpdated > 0;
    }

    /**
     * Fetch for a list of comments by postId
     * 
     * @param postId
     * @return a list of Comment Object
     */
    public List<Comment> getListOfComments(String postId) {
        return jdbcTemplate.query(
                SQL_GET_COMMENTS,
                (rs, rowNum) -> {
                    Comment comment = new Comment();
                    comment.setCommentId(rs.getString("commentId"));
                    comment.setUserId(rs.getString("userId"));
                    comment.setComment(rs.getString("comments"));
                    Object timestampObj = rs.getTimestamp("timestamp");
                    ZonedDateTime zonedDateTime = timestampObj != null
                            ? ((Timestamp) timestampObj).toInstant()
                                    .atZone(ZoneId.systemDefault())
                            : null;
                    comment.setTimestamp(zonedDateTime);
                    comment.setName(rs.getString("name"));
                    comment.setDisplaypic(rs.getString("displaypic"));
                    return comment;
                },
                postId);
    }

    // NOT IN USE BY APP, CREATED IN CASE
    public boolean likePost(String postId, String userId) {
        int rowsUpdated = jdbcTemplate.update(SQL_LIKED_POST, postId, userId);
        return rowsUpdated > 0;
    }

    public void unlikePost(String likedPostId, String userId) {
        jdbcTemplate.update(SQL_UNLIKED_POST, likedPostId, userId);
    }

    // NOT IN USE BY APP, CREATED IN CASE
    public List<String> getListOfLikes(String postId) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL_GET_LISTOFLIKES, postId);
        List<String> likes = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            likes.add((String) row.get("userId"));
        }
        return likes;
    }

    // NOT IN USE BY APP, CREATED IN CASE
    public List<Likes> getListOfLikesWUserDets(String postId) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL_GET_LISTOFLIKES_WITH_USERDETAILS, postId);
        List<Likes> likes = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            Likes like = new Likes();
            like.setLikedPostId(postId);
            like.setUserId((String) row.get("userId"));
            like.setName((String) row.get("name"));
            like.setDisplaypic((String) row.get("displaypic"));
            likes.add(like);
        }
        return likes;
    }

    /**
     * Batch handle likes by passing an array of post liked by userId
     * 
     * @param likedPostIds
     * @param userId
     */
    public void likeMultiplePosts(List<String> likedPostIds, String userId) {
        jdbcTemplate.batchUpdate(
                SQL_LIKED_POST,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        String likedPostId = likedPostIds.get(i);
                        ps.setString(1, likedPostId); // likedPostId
                        ps.setString(2, userId); // userId
                    }

                    @Override
                    public int getBatchSize() {
                        return likedPostIds.size();
                    }
                });
    }

    /**
     * Gets both comments and likes in a single database call
     * 
     * @param postId
     * @return
     *         Map of comments and likes.
     *         Use get("comments") and set type List of Comment(obj)
     *         Use get("likes") and set type List of String
     */
    public Map<String, Object> getCommentsAndLikes(String postId) {

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL_LIKES_COMMENTS_UNION, postId, postId);

        List<Comment> comments = new ArrayList<>();
        List<String> likes = new ArrayList<>();

        for (Map<String, Object> row : rows) {
            if ("comment".equals(row.get("type"))) {
                Comment comment = new Comment();
                comment.setCommentId((String) row.get("commentId"));
                comment.setUserId((String) row.get("userId"));
                comment.setComment((String) row.get("comments"));
                Object timestampObj = row.get("timestamp");
                ZonedDateTime zonedDateTime = timestampObj != null
                        ? ((Timestamp) timestampObj).toInstant()
                                .atZone(ZoneId.systemDefault())
                        : null;
                comment.setTimestamp(zonedDateTime);
                comment.setName((String) row.get("name"));
                comment.setDisplaypic((String) row.get("displaypic"));
                comments.add(comment);
            } else {
                likes.add((String) row.get("userId"));
            }
        }

        return Map.of(
                "comments", comments,
                "likes", likes);
    }
}
