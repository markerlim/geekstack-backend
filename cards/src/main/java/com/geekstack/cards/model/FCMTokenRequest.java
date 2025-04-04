package com.geekstack.cards.model;

public class FCMTokenRequest {
    private String userId;
    private String token;

    public FCMTokenRequest(){
        
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    @Override
    public String toString() {
        return "FCMTokenRequest [userId=" + userId + ", token=" + token + "]";
    }
    
}

