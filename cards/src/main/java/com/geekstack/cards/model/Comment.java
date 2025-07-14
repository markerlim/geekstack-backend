package com.geekstack.cards.model;

import java.time.ZonedDateTime;


public class Comment {
    
    private String commentId;
    private String comment;
    private String userId;
    private String name;
    private String displaypic;
    private ZonedDateTime timestamp;

    public Comment() {

    }

    public Comment(String commentId, String comment, String userId, ZonedDateTime timestamp) {
        this.commentId = commentId;
        this.comment = comment;
        this.userId = userId;
        this.timestamp = timestamp;
    }
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplaypic() {
        return displaypic;
    }

    public void setDisplaypic(String displaypic) {
        this.displaypic = displaypic;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

}
