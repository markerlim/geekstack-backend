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

}
