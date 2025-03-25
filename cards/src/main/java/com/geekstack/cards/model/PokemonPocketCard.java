package com.geekstack.cards.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "CL_pokemonpocket")
public class PokemonPocketCard {
    @Id
    private ObjectId _id;
    private String cardId;
    private String rarity;
    private String booster;
    private String cardname;
    private String element;
    private String hp;
    private String attackinfo;
    private String weakness;
    private String retreat;
    private String illustrator;
    private String urlimage;

    public PokemonPocketCard(){

    }
    
    public PokemonPocketCard(ObjectId _id, String cardId, String rarity, String booster, String cardname,
            String element, String hp, String attackinfo, String weakness, String retreat, String illustrator,
            String urlimage) {
        this._id = _id;
        this.cardId = cardId;
        this.rarity = rarity;
        this.booster = booster;
        this.cardname = cardname;
        this.element = element;
        this.hp = hp;
        this.attackinfo = attackinfo;
        this.weakness = weakness;
        this.retreat = retreat;
        this.illustrator = illustrator;
        this.urlimage = urlimage;
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

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getBooster() {
        return booster;
    }

    public void setBooster(String booster) {
        this.booster = booster;
    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getAttackinfo() {
        return attackinfo;
    }

    public void setAttackinfo(String attackinfo) {
        this.attackinfo = attackinfo;
    }

    public String getWeakness() {
        return weakness;
    }

    public void setWeakness(String weakness) {
        this.weakness = weakness;
    }

    public String getRetreat() {
        return retreat;
    }

    public void setRetreat(String retreat) {
        this.retreat = retreat;
    }

    public String getIllustrator() {
        return illustrator;
    }

    public void setIllustrator(String illustrator) {
        this.illustrator = illustrator;
    }

    public String getUrlimage() {
        return urlimage;
    }

    public void setUrlimage(String urlimage) {
        this.urlimage = urlimage;
    }

    
}
