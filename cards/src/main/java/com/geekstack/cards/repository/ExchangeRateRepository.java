package com.geekstack.cards.repository;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ExchangeRateRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public final static String SQL_GET_ER = "SELECT * FROM exc_rate WHERE base=?";
    public final static String SQL_SAVE_EXCHANGE = "INSERT INTO exc_rate(base,symbol,value,timestamp) VALUES(?,?,?,?)";

    public int updateExchangeRate(String base, String symbol, double value, LocalDateTime timestamp){
        int count = jdbcTemplate.update(SQL_SAVE_EXCHANGE,base,symbol,value,timestamp);
        return count;
    }}
