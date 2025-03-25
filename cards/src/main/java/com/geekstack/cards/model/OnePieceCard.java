package com.geekstack.cards.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "CL_onepiece")
public class OnePieceCard {
    
    @Id
    private String _id;

    @Field("booster")
    private String booster;
    private String cardfrom;
    private String cardname;

    @Field("cardname_lower")
    @TextIndexed
    private String cardname_lower;
    
    private String cardId;

    @Field("cardUid")
    private String cardUid;
    private String rarity;
    private String category;
    private String costlife;
    private String attribute;
    private String power;
    private String counter;
    private String color;
    private String typing;
    private String typing_lower;
    private String effects;
    private String trigger;
    private String image;
    private String urlimage;
    private int count;

    public OnePieceCard(){

    }

    public OnePieceCard(String _id, String booster, String cardfrom, String cardname, String cardname_lower,
            String cardId, String cardUid, String rarity, String category, String costlife, String attribute,
            String power, String counter, String color, String typing, String typing_lower, String effects,
            String trigger, String image, String urlimage) {
        this._id = _id;
        this.booster = booster;
        this.cardfrom = cardfrom;
        this.cardname = cardname;
        this.cardname_lower = cardname_lower;
        this.cardId = cardId;
        this.cardUid = cardUid;
        this.rarity = rarity;
        this.category = category;
        this.costlife = costlife;
        this.attribute = attribute;
        this.power = power;
        this.counter = counter;
        this.color = color;
        this.typing = typing;
        this.typing_lower = typing_lower;
        this.effects = effects;
        this.trigger = trigger;
        this.image = image;
        this.urlimage = urlimage;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getBooster() {
        return booster;
    }

    public void setBooster(String booster) {
        this.booster = booster;
    }

    public String getCardfrom() {
        return cardfrom;
    }

    public void setCardfrom(String cardfrom) {
        this.cardfrom = cardfrom;
    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public String getCardname_lower() {
        return cardname_lower;
    }

    public void setCardname_lower(String cardname_lower) {
        this.cardname_lower = cardname_lower;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardUid() {
        return cardUid;
    }

    public void setCardUid(String cardUid) {
        this.cardUid = cardUid;
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

    public String getCostlife() {
        return costlife;
    }

    public void setCostlife(String costlife) {
        this.costlife = costlife;
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

    public String getTyping_lower() {
        return typing_lower;
    }

    public void setTyping_lower(String typing_lower) {
        this.typing_lower = typing_lower;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrlimage() {
        return urlimage;
    }

    public void setUrlimage(String urlimage) {
        this.urlimage = urlimage;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

        
}
