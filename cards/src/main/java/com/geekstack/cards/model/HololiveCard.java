package com.geekstack.cards.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "CL_hololiveocg")
public class HololiveCard {
    @Id
    private String _id;
    @TextIndexed
    private String cardName;
    private String urlimage;
    private String detailUrl;
    private String cardNameJP;
    private String cardType;
    private String cardTypeJP;
    private String rarity;
    private String includedProducts;
    private String color;
    private String life;
    private String hp;
    private String bloomLevel;
    private String passingBaton;
    private String spArts;
    private List<String> tags;
    private String skillJP;
    private String skill;
    private String keywordJP;
    private String keyword;
    private String spSkillJP;
    private String spSkill;
    private String illustrator;
    private String cardId;
    private String cardUid;
    private String booster;
    
    public HololiveCard(){

    }

    public HololiveCard(String _id, String cardName, String urlimage, String detailUrl, String cardNameJP,
            String cardType, String cardTypeJP, String rarity, String includedProducts, String color, String life,
            String hp, String bloomLevel, String passingBaton, String spArts, List<String> tags, String skillJP,
            String skill, String keywordJP, String keyword, String spSkillJP, String spSkill, String illustrator,
            String cardId, String cardUid, String booster) {
        this._id = _id;
        this.cardName = cardName;
        this.urlimage = urlimage;
        this.detailUrl = detailUrl;
        this.cardNameJP = cardNameJP;
        this.cardType = cardType;
        this.cardTypeJP = cardTypeJP;
        this.rarity = rarity;
        this.includedProducts = includedProducts;
        this.color = color;
        this.life = life;
        this.hp = hp;
        this.bloomLevel = bloomLevel;
        this.passingBaton = passingBaton;
        this.spArts = spArts;
        this.tags = tags;
        this.skillJP = skillJP;
        this.skill = skill;
        this.keywordJP = keywordJP;
        this.keyword = keyword;
        this.spSkillJP = spSkillJP;
        this.spSkill = spSkill;
        this.illustrator = illustrator;
        this.cardId = cardId;
        this.cardUid = cardUid;
        this.booster = booster;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getUrlimage() {
        return urlimage;
    }

    public void setUrlimage(String urlimage) {
        this.urlimage = urlimage;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public String getCardNameJP() {
        return cardNameJP;
    }

    public void setCardNameJP(String cardNameJP) {
        this.cardNameJP = cardNameJP;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardTypeJP() {
        return cardTypeJP;
    }

    public void setCardTypeJP(String cardTypeJP) {
        this.cardTypeJP = cardTypeJP;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getIncludedProducts() {
        return includedProducts;
    }

    public void setIncludedProducts(String includedProducts) {
        this.includedProducts = includedProducts;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLife() {
        return life;
    }

    public void setLife(String life) {
        this.life = life;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getBloomLevel() {
        return bloomLevel;
    }

    public void setBloomLevel(String bloomLevel) {
        this.bloomLevel = bloomLevel;
    }

    public String getPassingBaton() {
        return passingBaton;
    }

    public void setPassingBaton(String passingBaton) {
        this.passingBaton = passingBaton;
    }

    public String getSpArts() {
        return spArts;
    }

    public void setSpArts(String spArts) {
        this.spArts = spArts;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getSkillJP() {
        return skillJP;
    }

    public void setSkillJP(String skillJP) {
        this.skillJP = skillJP;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getKeywordJP() {
        return keywordJP;
    }

    public void setKeywordJP(String keywordJP) {
        this.keywordJP = keywordJP;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSpSkillJP() {
        return spSkillJP;
    }

    public void setSpSkillJP(String spSkillJP) {
        this.spSkillJP = spSkillJP;
    }

    public String getSpSkill() {
        return spSkill;
    }

    public void setSpSkill(String spSkill) {
        this.spSkill = spSkill;
    }

    public String getIllustrator() {
        return illustrator;
    }

    public void setIllustrator(String illustrator) {
        this.illustrator = illustrator;
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

    public String getBooster() {
        return booster;
    }

    public void setBooster(String booster) {
        this.booster = booster;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }


}