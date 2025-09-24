package com.geekstack.cards.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "CL_dragonballzfw")
public class DragonBallzFWCard {

    @Id
    private String _id;

    @Field("booster")
    private String booster;

    @Field("cardNo")
    @TextIndexed
    private String cardId;

    @Field("cardUid")
    private String cardUid;

    @Field("cardName")
    @TextIndexed
    private String cardName;

    @Field("cardType")
    private String cardType;

    @Field("color")
    private String color;

    @Field("cost")
    private String cost;

    @Field("specifiedCost")
    private String specifiedCost;

    @Field("power")
    private String power;

    @Field("comboPower")
    private String comboPower;

    @Field("features")
    @TextIndexed
    private String features;

    @Field("effect")
    private String effect;

    @Field("rarity")
    private String rarity;

    @Field("urlimage")
    private String urlimage;

    @Field("obtainedFrom")
    private String obtainedFrom;

    @Field("side")
    private String side;

    // Frontend use - not stored in DB
    private int count;
    private String tcg;

    // Constructors
    public DragonBallzFWCard() {
    }

    public DragonBallzFWCard(String _id, String booster, String cardId, String cardUid, String cardName,
            String cardType, String color, String cost, String specifiedCost, String power,
            String comboPower, String features, String effect, String rarity, String urlimage,
            String obtainedFrom, String side) {
        this._id = _id;
        this.booster = booster;
        this.cardId = cardId;
        this.cardUid = cardUid;
        this.cardName = cardName;
        this.cardType = cardType;
        this.color = color;
        this.cost = cost;
        this.specifiedCost = specifiedCost;
        this.power = power;
        this.comboPower = comboPower;
        this.features = features;
        this.effect = effect;
        this.rarity = rarity;
        this.urlimage = urlimage;
        this.obtainedFrom = obtainedFrom;
        this.side = side;
    }

    // Getters and Setters
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

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getSpecifiedCost() {
        return specifiedCost;
    }

    public void setSpecifiedCost(String specifiedCost) {
        this.specifiedCost = specifiedCost;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getComboPower() {
        return comboPower;
    }

    public void setComboPower(String comboPower) {
        this.comboPower = comboPower;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getUrlimage() {
        return urlimage;
    }

    public void setUrlimage(String urlimage) {
        this.urlimage = urlimage;
    }

    public String getObtainedFrom() {
        return obtainedFrom;
    }

    public void setObtainedFrom(String obtainedFrom) {
        this.obtainedFrom = obtainedFrom;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTcg() {
        return tcg;
    }

    public void setTcg(String tcg) {
        this.tcg = tcg;
    }
}
