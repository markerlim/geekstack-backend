package com.geekstack.cards.model;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "userpost")
public class UserPost {

    @Id
    private String postId;

    private String postType;// c
    private String code;// c
    private String userId;// c
    private String headline;
    private String content;// c
    private String deckName;// c
    private Boolean isTournamentDeck;// c
    private ZonedDateTime timestamp;// c
    private String selectedCover;// c
    private List<CardDetails> listofcards;// c
    private List<String> listofhashtags;
    private String name;
    private String displaypic;

    public UserPost() {

    }

    public UserPost(String postId, String postType, String code, String userId, String headline, String content,
            String deckName, Boolean isTournamentDeck, ZonedDateTime timestamp, String selectedCover,
            List<CardDetails> listofcards, List<String> listofhashtags, String name,
            String displaypic) {
        this.postId = postId;
        this.postType = postType;
        this.code = code;
        this.userId = userId;
        this.headline = headline;
        this.content = content;
        this.deckName = deckName;
        this.isTournamentDeck = isTournamentDeck;
        this.timestamp = timestamp;
        this.selectedCover = selectedCover;
        this.listofcards = listofcards;
        this.listofhashtags = listofhashtags;
        this.name = name;
        this.displaypic = displaypic;
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

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getIsTournamentDeck() {
        return isTournamentDeck;
    }

    public void setIsTournamentDeck(Boolean isTournamentDeck) {
        this.isTournamentDeck = isTournamentDeck;
    }

    public String getSelectedCover() {
        return selectedCover;
    }

    public void setSelectedCover(String selectedCover) {
        this.selectedCover = selectedCover;
    }

    public List<CardDetails> getListofcards() {
        return listofcards;
    }

    public void setListofcards(List<CardDetails> listofcards) {
        this.listofcards = listofcards;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDeckName() {
        return deckName;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getListofhashtags() {
        return listofhashtags;
    }

    public void setListofhashtags(List<String> listofhashtags) {
        this.listofhashtags = listofhashtags;
    }

    @Override
    public String toString() {
        return "UserPost [postId=" + postId + ", postType=" + postType + ", code=" + code + ", userId=" + userId
                + ", headline=" + headline + ", content=" + content + ", deckName=" + deckName + ", isTournamentDeck="
                + isTournamentDeck + ", timestamp=" + timestamp + ", selectedCover=" + selectedCover + ", listofcards="
                + listofcards + ", name=" + name
                + ", displaypic=" + displaypic + "]";
    }
}
