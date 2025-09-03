package com.geekstack.cards.model;

import java.util.ArrayList;
import java.util.List;

public class UserPostFinal extends UserPost {
    private List<String> listoflikes;
    private List<Comment> listofcomments;

    public UserPostFinal() {

    }

    public UserPostFinal(UserPost userPost) {
        this.setPostId(userPost.getPostId());
        this.setPostType(userPost.getPostType());
        this.setCode(userPost.getCode());
        this.setUserId(userPost.getUserId());
        this.setHeadline(userPost.getHeadline());
        this.setContent(userPost.getContent());
        this.setDeckName(userPost.getDeckName());
        this.setIsTournamentDeck(userPost.getIsTournamentDeck());
        this.setTimestamp(userPost.getTimestamp());
        this.setSelectedCards(userPost.getSelectedCards());
        this.setListofcards(userPost.getListofcards());
        this.setName(userPost.getName());
        this.setDisplaypic(userPost.getDisplaypic());

        this.listoflikes = new ArrayList<>();
        this.listofcomments = new ArrayList<>();
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

}
