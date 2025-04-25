package com.geekstack.cards.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "CL_duelmasters")
public class DuelMastersCard {

    @Id
    private String _id;
    private String cardUid;
    private String cardId;
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

    private String effects2;
    private String effects2JP;

    @TextIndexed
    private String cardName2;
    private String cardName2JP;

    private String type2;
    private String type2JP;
    private List<String> race2;
    private List<String> race2JP;
    private List<String> civilization2;
    private List<String> civilization2JP;
    private String cost2;
    private String power2;
    private String mana2;

    private int count;

    public DuelMastersCard(){}

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

    public String getEffects2() {
        return effects2;
    }

    public void setEffects2(String effects2) {
        this.effects2 = effects2;
    }

    public String getEffects2JP() {
        return effects2JP;
    }

    public void setEffects2JP(String effects2jp) {
        effects2JP = effects2jp;
    }

    public String getCardName2() {
        return cardName2;
    }

    public void setCardName2(String cardName2) {
        this.cardName2 = cardName2;
    }

    public String getCardName2JP() {
        return cardName2JP;
    }

    public void setCardName2JP(String cardName2JP) {
        this.cardName2JP = cardName2JP;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getType2JP() {
        return type2JP;
    }

    public void setType2JP(String type2jp) {
        type2JP = type2jp;
    }

    public List<String> getRace2() {
        return race2;
    }

    public void setRace2(List<String> race2) {
        this.race2 = race2;
    }

    public List<String> getRace2JP() {
        return race2JP;
    }

    public void setRace2JP(List<String> race2jp) {
        race2JP = race2jp;
    }

    public List<String> getCivilization2() {
        return civilization2;
    }

    public void setCivilization2(List<String> civilization2) {
        this.civilization2 = civilization2;
    }

    public List<String> getCivilization2JP() {
        return civilization2JP;
    }

    public void setCivilization2JP(List<String> civilization2jp) {
        civilization2JP = civilization2jp;
    }

    public String getCost2() {
        return cost2;
    }

    public void setCost2(String cost2) {
        this.cost2 = cost2;
    }

    public String getPower2() {
        return power2;
    }

    public void setPower2(String power2) {
        this.power2 = power2;
    }

    public String getMana2() {
        return mana2;
    }

    public void setMana2(String mana2) {
        this.mana2 = mana2;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

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
    
    
}