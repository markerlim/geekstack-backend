package com.geekstack.cards.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "CL_hololiveocg")
public class HololiveCard {
    @Id
    private ObjectId _id;
    
    private String cardId;

    @Field("cardUid")
    private String cardUid;
    private String cardNameJP;
    private String cardNameEN;
    private String cardNameEN_lower;
    private String type;
    private String rarity;
    private String color;
    private String lifehp;
    private String tags;
    private String text;
    private String image;

    @Field("booster")
    private String booster;
    private String setfrom;

    public HololiveCard(){

    }
    
    public HololiveCard(ObjectId _id, String cardId, String cardUid, String cardNameJP, String cardNameEN,
            String cardNameEN_lower, String type, String rarity, String color, String lifehp, String tags, String text,
            String image, String booster, String setfrom) {
        this._id = _id;
        this.cardId = cardId;
        this.cardUid = cardUid;
        this.cardNameJP = cardNameJP;
        this.cardNameEN = cardNameEN;
        this.cardNameEN_lower = cardNameEN_lower;
        this.type = type;
        this.rarity = rarity;
        this.color = color;
        this.lifehp = lifehp;
        this.tags = tags;
        this.text = text;
        this.image = image;
        this.booster = booster;
        this.setfrom = setfrom;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
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

    public String getCardNameJP() {
        return cardNameJP;
    }

    public void setCardNameJP(String cardNameJP) {
        this.cardNameJP = cardNameJP;
    }

    public String getCardNameEN() {
        return cardNameEN;
    }

    public void setCardNameEN(String cardNameEN) {
        this.cardNameEN = cardNameEN;
    }

    public String getCardNameEN_lower() {
        return cardNameEN_lower;
    }

    public void setCardNameEN_lower(String cardNameEN_lower) {
        this.cardNameEN_lower = cardNameEN_lower;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLifehp() {
        return lifehp;
    }

    public void setLifehp(String lifehp) {
        this.lifehp = lifehp;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBooster() {
        return booster;
    }

    public void setBooster(String booster) {
        this.booster = booster;
    }

    public String getSetfrom() {
        return setfrom;
    }

    public void setSetfrom(String setfrom) {
        this.setfrom = setfrom;
    }

    
}
