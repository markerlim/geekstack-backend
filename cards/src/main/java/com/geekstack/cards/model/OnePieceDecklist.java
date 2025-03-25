package com.geekstack.cards.model;

import java.util.List;

public class OnePieceDecklist {
    
    private String deckuid;
    private String deckldrid;
    private String deckcover;
    private String deckname;
    private List<OnePieceCard> listofcards;

    public OnePieceDecklist(){

    }
    
    public String getDeckuid() {
        return deckuid;
    }
    public void setDeckuid(String deckuid) {
        this.deckuid = deckuid;
    }
    public String getDeckldrid() {
        return deckldrid;
    }
    public void setDeckldrid(String deckldrid) {
        this.deckldrid = deckldrid;
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
    public List<OnePieceCard> getListofcards() {
        return listofcards;
    }
    public void setListofcards(List<OnePieceCard> listofcards) {
        this.listofcards = listofcards;
    }
    
}
