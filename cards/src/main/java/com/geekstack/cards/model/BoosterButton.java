package com.geekstack.cards.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "BoosterList")
public class BoosterButton {
    @Id
    private ObjectId _id;
    private String pathname;
    private String alt;
    private String imageSrc;
    private String tcg;
    private Integer order;

    public BoosterButton(){

    }

    public BoosterButton(ObjectId _id, String pathname, String alt, String imageSrc, String tcg, Integer order) {
        this._id = _id;
        this.pathname = pathname;
        this.alt = alt;
        this.imageSrc = imageSrc;
        this.tcg = tcg;
        this.order = order;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getPathname() {
        return pathname;
    }

    public void setPathname(String pathname) {
        this.pathname = pathname;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }

    public String getTcg() {
        return tcg;
    }

    public void setTcg(String tcg) {
        this.tcg = tcg;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    
}
