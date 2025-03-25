package com.geekstack.cards.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;

public class UserPost {

    @Id
    private String postId;
    private String postType;//c
    private String code;//c
    private String userId;//c
    private String headline;
    private String content;//c
    private String deckName;//c
    private Boolean isTournamentDeck;//c
    private LocalDateTime timestamp;//c
    private List<CardDetails> selectedCards;//c
    private List<CardDetails> listofcards;//c
    private List<String> listoflikes;
    private List<Comment> listofcomments;
    private String name;
    private String displaypic;

    public UserPost() {

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

    public List<String> getListoflikes() {
        return listoflikes;
    }

    public void setListoflikes(List<String> listoflikes) {
        this.listoflikes = listoflikes;
    }

    public List<Comment> getListofcomments() {
        return listofcomments;
    }

    public void setListofcomments(List<Comment> listofcomments) {
        this.listofcomments = listofcomments;
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

    public List<CardDetails> getSelectedCards() {
        return selectedCards;
    }

    public void setSelectedCards(List<CardDetails> selectedCards) {
        this.selectedCards = selectedCards;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "UserPost [postId=" + postId + ", postType=" + postType + ", code=" + code + ", userId=" + userId
                + ", headline=" + headline + ", content=" + content + ", deckName=" + deckName + ", isTournamentDeck="
                + isTournamentDeck + ", timestamp=" + timestamp + ", selectedCards=" + selectedCards + ", listofcards="
                + listofcards + ", listoflikes=" + listoflikes + ", listofcomments=" + listofcomments + ", name=" + name
                + ", displaypic=" + displaypic + "]";
    }
    
}
