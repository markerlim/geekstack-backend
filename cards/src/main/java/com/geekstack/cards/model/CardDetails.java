package com.geekstack.cards.model;

import org.bson.types.ObjectId;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CardDetails {

    @JsonProperty("_id")
    private String _id;

    @JsonProperty("imageSrc")
    private String imageSrc;

    @JsonProperty("cardName")
    private String cardName;

    @JsonProperty("count")
    private int count;

    public CardDetails() {}

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }


    public int getCount() {
        return count;
    }


    public void setCount(int count) {
        this.count = count;
    }
}
