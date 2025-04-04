package com.geekstack.cards.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class UserDetailsMySQLRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final static String SQL_USER_EXIST = """
            SELECT userId,name,displaypic FROM users WHERE userId = ?
            """;
    private final static String SQL_SAVE_USER = """
            INSERT INTO users(userId, name, displaypic, email) VALUES (?,?,?,?)
            """;

    private final static String SQL_SAVE_FCM = """
            INSERT INTO fcm_tokens(id,user_id,token) VALUES (UUID(),?,?)
            """;

    private final static String SQL_REMOVE_FCM = """
            DELETE FROM fcm_tokens WHERE token = ?
            """;

    private final static String SQL_REMOVE_FCM_BYUSERID = """
            DELETE FROM fcm_tokens WHERE user_id = ?
            """;
    private final static String SQL_BATCHGET_TOKENS = "SELECT user_id,token FROM fcm_tokens WHERE user_id IN (%s)";

    private final static String SQL_UPDATE_NAME = "UPDATE users SET name = ? WHERE userId = ?";

    private final static String SQL_UPDATE_DP = "UPDATE users SET displaypic = ? WHERE userId = ?";

    public int createUser(String userId, String name, String image, String email) {
        int count = jdbcTemplate.update(SQL_SAVE_USER, userId, name, image, email);
        return count;
    }

    public Map<String, String> userExists(String userId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_USER_EXIST, userId);
        Map<String, String> holder = new HashMap<>();
        if (rs.next()) {
            holder.put("userId", rs.getString("userId"));
            holder.put("name", rs.getString("name"));
            holder.put("displaypic", rs.getString("displaypic"));
        }
        return holder;
    }

    public boolean updateUserName(String name, String userId) {
        int rowsUpdated = jdbcTemplate.update(SQL_UPDATE_NAME, name, userId);
        return rowsUpdated > 0;
    }

    public boolean updateDisplayPic(String displaypic, String userId) {
        int rowsUpdated = jdbcTemplate.update(SQL_UPDATE_DP, displaypic, userId);
        return rowsUpdated > 0;
    }

    public void updateFcmToken(String userId, String fcmToken) {
        jdbcTemplate.update(SQL_SAVE_FCM, userId, fcmToken);
    }

    public void removeFcmToken(String token) {
        jdbcTemplate.update(SQL_REMOVE_FCM,token);
    }

    public void removeFcmTokensByUserId(String userId) {
        jdbcTemplate.update(SQL_REMOVE_FCM_BYUSERID, userId);
    }

    public List<Map<String, Object>> batchGetTokens(List<String> ids) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        int batchSize = 30;

        for (int i = 0; i < ids.size(); i += batchSize) {
            List<String> batch = ids.subList(i, Math.min(i + batchSize, ids.size()));

            String placeholders = String.join(",", Collections.nCopies(batch.size(), "?"));

            String sql = String.format(SQL_BATCHGET_TOKENS, placeholders);

            List<Map<String, Object>> batchResults = jdbcTemplate.queryForList(sql, batch.toArray());

            resultList.addAll(batchResults);
        }

        return resultList;
    }

}
