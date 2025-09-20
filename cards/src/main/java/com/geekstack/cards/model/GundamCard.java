package com.geekstack.cards.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "CL_gundamcardgame")
public class GundamCard {

    @Id
    private String _id;

    @TextIndexed
    private String cardName;

    @TextIndexed
    private String cardId;

    @Field("package")
    private String packageId;
    private String series;
    private String urlimage;
    private String cardUid;
    private String detailUrl;
    private String rarity;
    private String level;
    private String cost;
    private String color;
    private String cardType;
    private String effect;
    private String zone;
    private String trait;
    private String link;
    private String attackPower;
    private String hitPoints;
    private String sourceTitle;
    private String obtainedFrom;
    private int count;

    // Used in the frontend so when deck saving need this,
    // Might include this in db directly
    private String tcg;

    // Constructors
    public GundamCard() {
    }

    public GundamCard(String _id, String cardName, String cardId, String packageId, String series,
            String urlimage, String cardUid, String detailUrl,
            String rarity, String level, String cost, String color, String cardType,
            String effect, String zone, String trait, String link, String attackPower,
            String hitPoints, String sourceTitle, String obtainedFrom, int count) {
        this._id = _id;
        this.cardName = cardName;
        this.cardId = cardId;
        this.packageId = packageId;
        this.series = series;
        this.urlimage = urlimage;
        this.cardUid = cardUid;
        this.detailUrl = detailUrl;
        this.rarity = rarity;
        this.level = level;
        this.cost = cost;
        this.color = color;
        this.cardType = cardType;
        this.effect = effect;
        this.zone = zone;
        this.trait = trait;
        this.link = link;
        this.attackPower = attackPower;
        this.hitPoints = hitPoints;
        this.sourceTitle = sourceTitle;
        this.obtainedFrom = obtainedFrom;
        this.count = count;
    }

    // Getters and Setters
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

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
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

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getTrait() {
        return trait;
    }

    public void setTrait(String trait) {
        this.trait = trait;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(String attackPower) {
        this.attackPower = attackPower;
    }

    public String getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(String hitPoints) {
        this.hitPoints = hitPoints;
    }

    public String getSourceTitle() {
        return sourceTitle;
    }

    public void setSourceTitle(String sourceTitle) {
        this.sourceTitle = sourceTitle;
    }

    public String getObtainedFrom() {
        return obtainedFrom;
    }

    public void setObtainedFrom(String obtainedFrom) {
        this.obtainedFrom = obtainedFrom;
    }

    @Override
    public String toString() {
        return "GundamCard{" +
                "id=" + _id +
                ", cardName='" + cardName + '\'' +
                ", cardId='" + cardId + '\'' +
                ", packageId='" + packageId + '\'' +
                ", series='" + series + '\'' +
                ", urlImage='" + urlimage + '\'' +
                ", cardUid='" + cardUid + '\'' +
                ", detailUrl='" + detailUrl + '\'' +
                ", rarity='" + rarity + '\'' +
                ", level='" + level + '\'' +
                ", cost='" + cost + '\'' +
                ", color='" + color + '\'' +
                ", cardType='" + cardType + '\'' +
                ", effect='" + effect + '\'' +
                ", zone='" + zone + '\'' +
                ", trait='" + trait + '\'' +
                ", link='" + link + '\'' +
                ", attackPower='" + attackPower + '\'' +
                ", hitPoints='" + hitPoints + '\'' +
                ", sourceTitle='" + sourceTitle + '\'' +
                ", obtainedFrom='" + obtainedFrom + '\'' +
                '}';
    }

    public String getTcg() {
        return tcg;
    }

    public void setTcg(String tcg) {
        this.tcg = tcg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    
}