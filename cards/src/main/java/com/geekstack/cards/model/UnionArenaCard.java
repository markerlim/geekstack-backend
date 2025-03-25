package com.geekstack.cards.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "CL_unionarena_v2")
public class UnionArenaCard {

    @Id
    private String _id;

    @Field("anime")
    private String anime;

    @Field("animeCode")
    private String animeCode;

    private int apcost;
    private int banRatio;
    private String banWith;
    private String basicpower;
    private String booster;
    private String cardId;
    private String cardUid;

    @Field("cardName")
    @TextIndexed
    private String cardName;

    private String category;
    private String color;

    @TextIndexed
    private String effect;
    private int energycost;
    private String energygen;
    private String image;

    @Field("rarity")
    private String rarity;

    private String traits;
    private String trigger;
    private String triggerState;
    private String urlimage;

    @Field("rarityAct")
    private String rarityAct;

    private String cardcode;

    @Field("price_yyt_id")
    private String priceYytId;

    @Field("price_fulla_id")
    private String priceFullaId;

    private String YYT;
    private String FULLA;
    private int count;

    public UnionArenaCard() {
    }
    

    public UnionArenaCard(String _id, String anime, String animeCode, int apcost, int banRatio, String banWith,
            String basicpower, String booster, String cardId, String cardUid, String cardName, String category,
            String color, String effect, int energycost, String energygen, String image, String rarity, String traits,
            String trigger, String triggerState, String urlimage, String rarityAct, String cardcode, String priceYytId,
            String priceFullaId, String yYT, String fULLA) {
        this._id = _id;
        this.anime = anime;
        this.animeCode = animeCode;
        this.apcost = apcost;
        this.banRatio = banRatio;
        this.banWith = banWith;
        this.basicpower = basicpower;
        this.booster = booster;
        this.cardId = cardId;
        this.cardUid = cardUid;
        this.cardName = cardName;
        this.category = category;
        this.color = color;
        this.effect = effect;
        this.energycost = energycost;
        this.energygen = energygen;
        this.image = image;
        this.rarity = rarity;
        this.traits = traits;
        this.trigger = trigger;
        this.triggerState = triggerState;
        this.urlimage = urlimage;
        this.rarityAct = rarityAct;
        this.cardcode = cardcode;
        this.priceYytId = priceYytId;
        this.priceFullaId = priceFullaId;
        YYT = yYT;
        FULLA = fULLA;
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAnime() {
        return anime;
    }

    public void setAnime(String anime) {
        this.anime = anime;
    }

    public String getAnimeCode() {
        return animeCode;
    }

    public void setAnimeCode(String animeCode) {
        this.animeCode = animeCode;
    }

    public int getApcost() {
        return apcost;
    }

    public void setApcost(int apcost) {
        this.apcost = apcost;
    }

    public int getBanRatio() {
        return banRatio;
    }

    public void setBanRatio(int banRatio) {
        this.banRatio = banRatio;
    }

    public String getBasicpower() {
        return basicpower;
    }

    public void setBasicpower(String basicpower) {
        this.basicpower = basicpower;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public int getEnergycost() {
        return energycost;
    }

    public void setEnergycost(int energycost) {
        this.energycost = energycost;
    }

    public String getEnergygen() {
        return energygen;
    }

    public void setEnergygen(String energygen) {
        this.energygen = energygen;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getTraits() {
        return traits;
    }

    public void setTraits(String traits) {
        this.traits = traits;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getTriggerState() {
        return triggerState;
    }

    public void setTriggerState(String triggerState) {
        this.triggerState = triggerState;
    }

    public String getUrlimage() {
        return urlimage;
    }

    public void setUrlimage(String urlimage) {
        this.urlimage = urlimage;
    }

    public String getRarityAct() {
        return rarityAct;
    }

    public void setRarityAct(String rarityAct) {
        this.rarityAct = rarityAct;
    }

    public String getCardcode() {
        return cardcode;
    }

    public void setCardcode(String cardcode) {
        this.cardcode = cardcode;
    }

    public String getPriceYytId() {
        return priceYytId;
    }

    public void setPriceYytId(String priceYytId) {
        this.priceYytId = priceYytId;
    }

    public String getPriceFullaId() {
        return priceFullaId;
    }

    public void setPriceFullaId(String priceFullaId) {
        this.priceFullaId = priceFullaId;
    }

    public String getYYT() {
        return YYT;
    }

    public void setYYT(String yYT) {
        YYT = yYT;
    }

    public String getFULLA() {
        return FULLA;
    }

    public void setFULLA(String fULLA) {
        FULLA = fULLA;
    }

    public String getBanWith() {
        return banWith;
    }

    public void setBanWith(String banWith) {
        this.banWith = banWith;
    }


    public int getCount() {
        return count;
    }


    public void setCount(int count) {
        this.count = count;
    }

}
