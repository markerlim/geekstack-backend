package com.geekstack.cards.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "CL_onepiece_v2")
public class OnePieceCard {
    
    @Id
    private String _id;

    @TextIndexed
    private String cardName;
    private String cardId;
    private String rarity;
    private String category;
    private String lifecost;
    private String attribute;
    private String power;
    private String counter;
    private String color;
    private String typing;
    private String effects;
    private String trigger;
    private String urlimage;
    private String cardUid;
    private String booster;
    private int count;

    public OnePieceCard(){}

    
    public OnePieceCard(String _id, String cardName, String cardId, String rarity, String category, String lifecost,
            String attribute, String power, String counter, String color, String typing, String effects, String trigger,
            String urlimage, String cardUid, String booster, int count) {
        this._id = _id;
        this.cardName = cardName;
        this.cardId = cardId;
        this.rarity = rarity;
        this.category = category;
        this.lifecost = lifecost;
        this.attribute = attribute;
        this.power = power;
        this.counter = counter;
        this.color = color;
        this.typing = typing;
        this.effects = effects;
        this.trigger = trigger;
        this.urlimage = urlimage;
        this.cardUid = cardUid;
        this.booster = booster;
        this.count = count;
    }


    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
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
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getLifecost() {
        return lifecost;
    }
    public void setLifecost(String lifecost) {
        this.lifecost = lifecost;
    }
    public String getAttribute() {
        return attribute;
    }
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
    public String getPower() {
        return power;
    }
    public void setPower(String power) {
        this.power = power;
    }
    public String getCounter() {
        return counter;
    }
    public void setCounter(String counter) {
        this.counter = counter;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public String getTyping() {
        return typing;
    }
    public void setTyping(String typing) {
        this.typing = typing;
    }
    public String getEffects() {
        return effects;
    }
    public void setEffects(String effects) {
        this.effects = effects;
    }
    public String getTrigger() {
        return trigger;
    }
    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }
    public String getUrlimage() {
        return urlimage;
    }
    public void setUrlimage(String urlimage) {
        this.urlimage = urlimage;
    }
    public String getCardUid() {
        return cardUid;
    }
    public void setCardUid(String cardUid) {
        this.cardUid = cardUid;
    }
    public String getBooster() {
        return booster;
    }
    public void setBooster(String booster) {
        this.booster = booster;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

           
}
