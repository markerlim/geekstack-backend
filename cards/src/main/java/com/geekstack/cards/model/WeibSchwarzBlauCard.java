package com.geekstack.cards.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "CL_wsblau")
public class WeibSchwarzBlauCard {

    @Id
    private String _id;

    @Field("cardId")
    @TextIndexed
    private String cardId;

    @Field("detail_url")
    private String detailUrl;

    @Field("cardUid")
    private String cardUid;

    @Field("urlimage")
    private String urlimage;

    @Field("cardName")
    @TextIndexed
    private String cardName;

    @Field("specifications")
    private List<String> specifications;

    @Field("product")
    private String product;

    @Field("series")
    @TextIndexed
    private String series;

    @Field("cardType")
    private String cardType;

    @Field("rarity")
    private String rarity;

    @Field("color")
    private String color;

    @Field("features")
    @TextIndexed
    private String features;

    @Field("level")
    private String level;

    @Field("cost")
    private String cost;

    @Field("power")
    private String power;

    @Field("soul")
    private String soul;

    @Field("trigger")
    private String trigger;

    @Field("effect")
    private String effect;

    @Field("pronunciation")
    private String pronunciation;

    @Field("cardNameJP")
    private String cardNameJP;

    @Field("productJP")
    private String productJP;

    @Field("seriesJP")
    @TextIndexed
    private String seriesJP;

    @Field("cardTypeJP")
    private String cardTypeJP;

    @Field("colorJP")
    private String colorJP;

    @Field("featuresJP")
    @TextIndexed
    private String featuresJP;

    @Field("effectJP")
    @TextIndexed
    private String effectJP;

    @Field("expansionCode")
    private String expansionCode;

    @Field("expansionTitle")
    private String expansionTitle;

    // Frontend use - not stored in DB
    private int count;
    private String tcg;

    // Constructors
    public WeibSchwarzBlauCard() {
    }

    public WeibSchwarzBlauCard(String _id, String cardId, String detailUrl, String cardUid, String urlimage,
            String cardName, List<String> specifications, String product, String series, String cardType,
            String rarity, String color, String features, String level, String cost, String power,
            String soul, String trigger, String effect, String pronunciation, String cardNameJP,
            String productJP, String seriesJP, String cardTypeJP, String colorJP, String featuresJP,
            String effectJP, String expansionCode, String expansionTitle) {
        this._id = _id;
        this.cardId = cardId;
        this.detailUrl = detailUrl;
        this.cardUid = cardUid;
        this.urlimage = urlimage;
        this.cardName = cardName;
        this.specifications = specifications;
        this.product = product;
        this.series = series;
        this.cardType = cardType;
        this.rarity = rarity;
        this.color = color;
        this.features = features;
        this.level = level;
        this.cost = cost;
        this.power = power;
        this.soul = soul;
        this.trigger = trigger;
        this.effect = effect;
        this.pronunciation = pronunciation;
        this.cardNameJP = cardNameJP;
        this.productJP = productJP;
        this.seriesJP = seriesJP;
        this.cardTypeJP = cardTypeJP;
        this.colorJP = colorJP;
        this.featuresJP = featuresJP;
        this.effectJP = effectJP;
        this.expansionCode = expansionCode;
        this.expansionTitle = expansionTitle;
    }

    // Getters and Setters
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getCardUid() {
        return cardUid;
    }

    public void setCardUid(String cardUid) {
        this.cardUid = cardUid;
    }

    public String getUrlimage() {
        return urlimage;
    }

    public void setUrlimage(String urlimage) {
        this.urlimage = urlimage;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public List<String> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<String> specifications) {
        this.specifications = specifications;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
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

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
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

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getSoul() {
        return soul;
    }

    public void setSoul(String soul) {
        this.soul = soul;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getCardNameJP() {
        return cardNameJP;
    }

    public void setCardNameJP(String cardNameJP) {
        this.cardNameJP = cardNameJP;
    }

    public String getProductJP() {
        return productJP;
    }

    public void setProductJP(String productJP) {
        this.productJP = productJP;
    }

    public String getSeriesJP() {
        return seriesJP;
    }

    public void setSeriesJP(String seriesJP) {
        this.seriesJP = seriesJP;
    }

    public String getCardTypeJP() {
        return cardTypeJP;
    }

    public void setCardTypeJP(String cardTypeJP) {
        this.cardTypeJP = cardTypeJP;
    }

    public String getColorJP() {
        return colorJP;
    }

    public void setColorJP(String colorJP) {
        this.colorJP = colorJP;
    }

    public String getFeaturesJP() {
        return featuresJP;
    }

    public void setFeaturesJP(String featuresJP) {
        this.featuresJP = featuresJP;
    }

    public String getEffectJP() {
        return effectJP;
    }

    public void setEffectJP(String effectJP) {
        this.effectJP = effectJP;
    }

    public String getExpansionCode() {
        return expansionCode;
    }

    public void setExpansionCode(String expansionCode) {
        this.expansionCode = expansionCode;
    }

    public String getExpansionTitle() {
        return expansionTitle;
    }

    public void setExpansionTitle(String expansionTitle) {
        this.expansionTitle = expansionTitle;
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
