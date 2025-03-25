package com.geekstack.cards.model;

public class UnionArenaCardDTO {

    private String cardName;
    private String cardId;
    private String rarity;

    public UnionArenaCardDTO() {}

    public UnionArenaCardDTO(String cardName, String cardId, String rarity) {
        this.cardName = cardName;
        this.cardId = cardId;
        this.rarity = rarity;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }
}