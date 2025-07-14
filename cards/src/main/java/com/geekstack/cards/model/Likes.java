package com.geekstack.cards.model;

public class Likes {
    
    private String likedPostId;
    private String userId;
    private String displaypic;
    private String name;

    public Likes(){
        
    }

    public Likes(String likedPostId, String userId, String displaypic, String name) {
        this.likedPostId = likedPostId;
        this.userId = userId;
        this.displaypic = displaypic;
        this.name = name;
    }
    public String getLikedPostId() {
        return likedPostId;
    }
    public void setLikedPostId(String likedPostId) {
        this.likedPostId = likedPostId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getDisplaypic() {
        return displaypic;
    }
    public void setDisplaypic(String displaypic) {
        this.displaypic = displaypic;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    
    
}
