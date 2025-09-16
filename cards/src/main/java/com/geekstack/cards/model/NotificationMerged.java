package com.geekstack.cards.model;

import java.time.ZonedDateTime;
import java.util.List;

public class NotificationMerged {
    private String postId;
    private String message;
    private List<SenderInfo> latestSender;
    private ZonedDateTime timestamp;
    private boolean isRead;

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


    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public static class SenderInfo {
        private String name;
        private String dp;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDp() {
            return dp;
        }

        public void setDp(String dp) {
            this.dp = dp;
        }

    }

    public List<SenderInfo> getLatestSender() {
        return latestSender;
    }

    public void setLatestSender(List<SenderInfo> latestSender) {
        this.latestSender = latestSender;
    }
}
