package com.geekstack.cards.model;

import static com.geekstack.cards.utils.Constants.C_NOTIFICATION;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = C_NOTIFICATION)
public class Notification {
    @Id
    private String notificationId; 
    private String userId;//Receiver
    private String senderId;//Id of user that trigger event
    private String senderName;
    private String senderDp;
    private String postId;//Post Id
    private String message;//Event action
    private Boolean isRead;//Boolean of read
    private LocalDateTime timestamp;//Time stamp of event

    public Notification(){

    } 

    public Notification(String notificationId, String userId, String senderId, String senderName, String senderDp,
            String postId, String message, Boolean isRead, LocalDateTime timestamp) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderDp = senderDp;
        this.postId = postId;
        this.message = message;
        this.isRead = isRead;
        this.timestamp = timestamp;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getSenderId() {
        return senderId;
    }


    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderDp() {
        return senderDp;
    }

    public void setSenderDp(String senderDp) {
        this.senderDp = senderDp;
    }

    @Override
    public String toString() {
        return "Notification [notificationId=" + notificationId + ", userId=" + userId + ", senderId=" + senderId
                + ", senderName=" + senderName + ", senderDp=" + senderDp + ", postId=" + postId + ", message="
                + message + ", isRead=" + isRead + ", timestamp=" + timestamp + "]";
    }

}