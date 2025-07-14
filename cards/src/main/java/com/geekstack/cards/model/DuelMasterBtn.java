package com.geekstack.cards.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "NewList")
public class DuelMasterBtn {
    
    @Id
    private String _id;

    @Field("urlimage")
    private String imageSrc;

    @Field("booster")
    private String pathname;

    private String japtext;
    private LocalDateTime timestamp;
    private String category;
    private String detailLink;

    public DuelMasterBtn(){

    }
    
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
    public String getPathname() {
        return pathname;
    }
    public void setPathname(String pathname) {
        this.pathname = pathname;
    }
    public String getJaptext() {
        return japtext;
    }
    public void setJaptext(String japtext) {
        this.japtext = japtext;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public String getDetailLink() {
        return detailLink;
    }
    public void setDetailLink(String detailLink) {
        this.detailLink = detailLink;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    
 
}
