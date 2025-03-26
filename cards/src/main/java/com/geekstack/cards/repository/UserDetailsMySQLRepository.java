package com.geekstack.cards.repository;

import java.util.HashMap;
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

    private final static String SQL_UPDATE_NAME = "UPDATE users SET name = ? WHERE userId = ?";

    private final static String SQL_UPDATE_DP = "UPDATE users SET displaypic = ? WHERE userId = ?";

    
    public int createUser(String userId, String name, String image, String email){
        int count = jdbcTemplate.update(SQL_SAVE_USER,userId,name,image,email);
        return count;
    }

    public Map<String,String> userExists(String userId) {
        SqlRowSet rs = jdbcTemplate.queryForRowSet(SQL_USER_EXIST,userId);
        Map<String,String> holder = new HashMap<>();
        if(rs.next()){
            holder.put("userId",rs.getString("userId"));
            holder.put("name",rs.getString("name"));
            holder.put("displaypic",rs.getString("displaypic"));
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
}
