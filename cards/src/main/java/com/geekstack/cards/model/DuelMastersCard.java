package com.geekstack.cards.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "CL_duelmasters")
public class DuelMastersCard {

    @Id
    private String id;
    private String cardUid;
    private String booster;
    private String cost;
    private String power;
    private String mana;
    private String illustrator;
    private List<String> race;
    private List<String> raceJP;
    private List<String> civilization;
    private List<String> civilizationJP;
    private String rarity;
    private String urlimage;
    private String detailurl;
    private String effects;
    private String effectsJP;
    @TextIndexed
    private String cardName;
    private String cardNameJP;
    private String type;
    private String typeJP;
    private int count;

    public DuelMastersCard(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getMana() {
        return mana;
    }

    public void setMana(String mana) {
        this.mana = mana;
    }

    public String getIllustrator() {
        return illustrator;
    }

    public void setIllustrator(String illustrator) {
        this.illustrator = illustrator;
    }

    public List<String> getRace() {
        return race;
    }

    public void setRace(List<String> race) {
        this.race = race;
    }

    public List<String> getRaceJP() {
        return raceJP;
    }

    public void setRaceJP(List<String> raceJP) {
        this.raceJP = raceJP;
    }

    public List<String> getCivilization() {
        return civilization;
    }

    public void setCivilization(List<String> civilization) {
        this.civilization = civilization;
    }

    public List<String> getCivilizationJP() {
        return civilizationJP;
    }

    public void setCivilizationJP(List<String> civilizationJP) {
        this.civilizationJP = civilizationJP;
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

    public String getDetailurl() {
        return detailurl;
    }

    public void setDetailurl(String detailurl) {
        this.detailurl = detailurl;
    }

    public String getEffects() {
        return effects;
    }

    public void setEffects(String effects) {
        this.effects = effects;
    }

    public String getEffectsJP() {
        return effectsJP;
    }

    public void setEffectsJP(String effectsJP) {
        this.effectsJP = effectsJP;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardNameJP() {
        return cardNameJP;
    }

    public void setCardNameJP(String cardNameJP) {
        this.cardNameJP = cardNameJP;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeJP() {
        return typeJP;
    }

    public void setTypeJP(String typeJP) {
        this.typeJP = typeJP;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
    
}