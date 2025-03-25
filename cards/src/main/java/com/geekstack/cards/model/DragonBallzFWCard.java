package com.geekstack.cards.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "CL_dragonballzfw")
public class DragonBallzFWCard {

    @Id
    private String _id;
    private boolean awakenform;

    @Field("booster")
    private String booster;
    private String cardId;
    @Field("cardUid")
    private String cardUid;

    private String cardName;
    @Field("cardNameLower")
    @TextIndexed
    private String cardNameLower;
    
    private String cardtype;
    private String color;
    private String combopower;
    private String cost;
    private String effects;
    private String features;
    private String image;
    private String power;
    private String rarityAct;
    private String rarity;
    private String setFrom;
    private String specifieddcost;
    private String urlimage;
    private int count;

    public DragonBallzFWCard(){
        
    }

    public DragonBallzFWCard(String _id, boolean awakenform, String booster, String cardId, String cardUid,
            String cardName, String cardNameLower, String cardtype, String color, String combopower, String cost,
            String effects, String features, String image, String power, String rarityAct, String rarity,
            String setFrom, String specifieddcost, String urlimage) {
        this._id = _id;
        this.awakenform = awakenform;
        this.booster = booster;
        this.cardId = cardId;
        this.cardUid = cardUid;
        this.cardName = cardName;
        this.cardNameLower = cardNameLower;
        this.cardtype = cardtype;
        this.color = color;
        this.combopower = combopower;
        this.cost = cost;
        this.effects = effects;
        this.features = features;
        this.image = image;
        this.power = power;
        this.rarityAct = rarityAct;
        this.rarity = rarity;
        this.setFrom = setFrom;
        this.specifieddcost = specifieddcost;
        this.urlimage = urlimage;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public boolean isAwakenform() {
        return awakenform;
    }

    public void setAwakenform(boolean awakenform) {
        this.awakenform = awakenform;
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

    public String getCardNameLower() {
        return cardNameLower;
    }

    public void setCardNameLower(String cardNameLower) {
        this.cardNameLower = cardNameLower;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCombopower() {
        return combopower;
    }

    public void setCombopower(String combopower) {
        this.combopower = combopower;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getEffects() {
        return effects;
    }

    public void setEffects(String effects) {
        this.effects = effects;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getRarityAct() {
        return rarityAct;
    }

    public void setRarityAct(String rarityAct) {
        this.rarityAct = rarityAct;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getSetFrom() {
        return setFrom;
    }

    public void setSetFrom(String setFrom) {
        this.setFrom = setFrom;
    }

    public String getSpecifieddcost() {
        return specifieddcost;
    }

    public void setSpecifieddcost(String specifieddcost) {
        this.specifieddcost = specifieddcost;
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
