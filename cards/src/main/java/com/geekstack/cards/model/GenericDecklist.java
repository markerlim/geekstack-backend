package com.geekstack.cards.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;

public class GenericDecklist {

    @Field("deckuid")
    private String deckuid;

    @Field("image")
    private String deckcover;

    @Field("deckName")
    private String deckname;

    @Field("listofcards")
    private List<GenericCard> listofcards;

    public String getDeckuid() {
        return deckuid;
    }

    public void setDeckuid(String deckuid) {
        this.deckuid = deckuid;
    }

    public String getDeckcover() {
        return deckcover;
    }

    public void setDeckcover(String deckcover) {
        this.deckcover = deckcover;
    }

    public String getDeckname() {
        return deckname;
    }

    public void setDeckname(String deckname) {
        this.deckname = deckname;
    }

    public List<GenericCard> getListofcards() {
        return listofcards;
    }

    public void setListofcards(List<GenericCard> listofcards) {
        this.listofcards = listofcards;
    }

}
