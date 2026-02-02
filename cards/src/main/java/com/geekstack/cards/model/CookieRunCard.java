package com.geekstack.cards.model;

import static com.geekstack.cards.utils.Constants.C_COOKIERUN;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = C_COOKIERUN)
public class CookieRunCard {

    @Id
    private String _id;

    @Field("card_idx")
    private int cardIdx;

    @Field("site_lang")
    private String siteLang;

    @Field("card_name")
    @TextIndexed()
    private String cardName;

    @Field("card_artist_title")
    private String cardArtistTitle;

    @Field("card_product_title")
    private String cardProductTitle;

    @Field("card_skill_text")
    private String cardSkillText;

    @Field("card_attack_text")
    private String cardAttackText;

    @Field("card_flip")
    private String cardFlip;

    @Field("card_rare")
    private String cardRare;

    @Field("card_grade")
    private String cardGrade;

    @Field("card_hp")
    private String cardHp;

    @Field("card_level")
    private String cardLevel;

    @Field("card_type")
    private String cardType;

    @Field("card_energy_type")
    private String cardEnergyType;

    @Field("card_color")
    private String cardColor;

    @Field("cardUid")
    @TextIndexed
    private String cardUid;

    @Field("cardId")
    @TextIndexed
    private String cardId;

    @Field("booster")
    private String booster;

    @Field("urlimage")
    private String urlimage;

    private int count;

    // Used in the frontend so when deck saving need this,
    // Might include this in db directly
    private String tcg;

    public CookieRunCard() {
    }

    public CookieRunCard(String _id, int cardIdx, String siteLang, String cardName, String cardArtistTitle, 
            String cardProductTitle, String cardSkillText, String cardAttackText, String cardFlip, 
            String cardRare, String cardGrade, String cardHp, String cardLevel, String cardType, 
            String cardEnergyType, String cardColor, String cardUid, String cardId, String booster, String urlimage) {
        this._id = _id;
        this.cardIdx = cardIdx;
        this.siteLang = siteLang;
        this.cardName = cardName;
        this.cardArtistTitle = cardArtistTitle;
        this.cardProductTitle = cardProductTitle;
        this.cardSkillText = cardSkillText;
        this.cardAttackText = cardAttackText;
        this.cardFlip = cardFlip;
        this.cardRare = cardRare;
        this.cardGrade = cardGrade;
        this.cardHp = cardHp;
        this.cardLevel = cardLevel;
        this.cardType = cardType;
        this.cardEnergyType = cardEnergyType;
        this.cardColor = cardColor;
        this.cardUid = cardUid;
        this.cardId = cardId;
        this.booster = booster;
        this.urlimage = urlimage;
    }

    public String getBooster() {
        return booster;
    }

    public void setBooster(String booster) {
        this.booster = booster;
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

    public int getCardIdx() {
        return cardIdx;
    }

    public void setCardIdx(int cardIdx) {
        this.cardIdx = cardIdx;
    }

    public String getSiteLang() {
        return siteLang;
    }

    public void setSiteLang(String siteLang) {
        this.siteLang = siteLang;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardArtistTitle() {
        return cardArtistTitle;
    }

    public void setCardArtistTitle(String cardArtistTitle) {
        this.cardArtistTitle = cardArtistTitle;
    }

    public String getCardProductTitle() {
        return cardProductTitle;
    }

    public void setCardProductTitle(String cardProductTitle) {
        this.cardProductTitle = cardProductTitle;
    }

    public String getCardSkillText() {
        return cardSkillText;
    }

    public void setCardSkillText(String cardSkillText) {
        this.cardSkillText = cardSkillText;
    }

    public String getCardAttackText() {
        return cardAttackText;
    }

    public void setCardAttackText(String cardAttackText) {
        this.cardAttackText = cardAttackText;
    }

    public String getCardFlip() {
        return cardFlip;
    }

    public void setCardFlip(String cardFlip) {
        this.cardFlip = cardFlip;
    }

    public String getCardRare() {
        return cardRare;
    }

    public void setCardRare(String cardRare) {
        this.cardRare = cardRare;
    }

    public String getCardGrade() {
        return cardGrade;
    }

    public void setCardGrade(String cardGrade) {
        this.cardGrade = cardGrade;
    }

    public String getCardHp() {
        return cardHp;
    }

    public void setCardHp(String cardHp) {
        this.cardHp = cardHp;
    }

    public String getCardLevel() {
        return cardLevel;
    }

    public void setCardLevel(String cardLevel) {
        this.cardLevel = cardLevel;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardEnergyType() {
        return cardEnergyType;
    }

    public void setCardEnergyType(String cardEnergyType) {
        this.cardEnergyType = cardEnergyType;
    }

    public String getCardColor() {
        return cardColor;
    }

    public void setCardColor(String cardColor) {
        this.cardColor = cardColor;
    }

    public String getCardUid() {
        return cardUid;
    }

    public void setCardUid(String cardUid) {
        this.cardUid = cardUid;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getUrlimage() {
        return urlimage;
    }

    public void setUrlimage(String urlimage) {
        this.urlimage = urlimage;
    }

    public String getTcg() {
        return tcg;
    }

    public void setTcg(String tcg) {
        this.tcg = tcg;
    }

}
