package com.geekstack.cards.model;

import java.util.List;

public class CookieRunDecklist {
    private String deckuid;
    private String deckcover;
    private String deckname;
    private List<CookieRunCard> listofcards;

    public CookieRunDecklist(){}

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

    public List<CookieRunCard> getListofcards() {
        return listofcards;
    }

    public void setListofcards(List<CookieRunCard> listofcards) {
        this.listofcards = listofcards;
    }
    
}
