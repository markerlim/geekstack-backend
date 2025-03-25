package com.geekstack.cards.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "cardprices_yyt")
public class CardPriceYYT {

    @Id
    private ObjectId id;
    private float pricejpy;
    private String rarity;
    private String cardcode;
    private int stock;
    private String url;
    private String animecode;

    @Field("price_yyt_id")
    private String priceYytId;
    private Map<String, List<PriceData>> pricedata;

    public CardPriceYYT(){

    }

    public CardPriceYYT(ObjectId id, float pricejpy, String rarity, String cardcode, int stock, String url,
            String animecode, String priceYytId, Map<String, List<PriceData>> pricedata) {
        this.id = id;
        this.pricejpy = pricejpy;
        this.rarity = rarity;
        this.cardcode = cardcode;
        this.stock = stock;
        this.url = url;
        this.animecode = animecode;
        this.priceYytId = priceYytId;
        this.pricedata = pricedata;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public float getPricejpy() {
        return pricejpy;
    }

    public void setPricejpy(float pricejpy) {
        this.pricejpy = pricejpy;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getCardcode() {
        return cardcode;
    }

    public void setCardcode(String cardcode) {
        this.cardcode = cardcode;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAnimecode() {
        return animecode;
    }

    public void setAnimecode(String animecode) {
        this.animecode = animecode;
    }

    public String getPriceYytId() {
        return priceYytId;
    }

    public void setPriceYytId(String priceYytId) {
        this.priceYytId = priceYytId;
    }

    public Map<String, List<PriceData>> getPricedata() {
        return pricedata;
    }

    public void setPricedata(Map<String, List<PriceData>> pricedata) {
        this.pricedata = pricedata;
    }

    public static class PriceData {
        private LocalDateTime date;
        private float pricejpydaily;
        private int stock; // Assuming stock is an integer
        private float priceChange;

        public PriceData(){
            
        }

        public PriceData(LocalDateTime date, float pricejpydaily, int stock, float priceChange) {
            this.date = date;
            this.pricejpydaily = pricejpydaily;
            this.stock = stock;
            this.priceChange = priceChange;
        }

        public LocalDateTime getDate() {
            return date;
        }

        public void setDate(LocalDateTime date) {
            this.date = date;
        }

        public float getPricejpydaily() {
            return pricejpydaily;
        }

        public void setPricejpydaily(float pricejpydaily) {
            this.pricejpydaily = pricejpydaily;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public float getPriceChange() {
            return priceChange;
        }

        public void setPriceChange(float priceChange) {
            this.priceChange = priceChange;
        }
    }
}
