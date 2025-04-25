package com.geekstack.cards.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "NewList")
public class DuelMasterBtn {
    
    @Id
    private String _id;
    private String urlimage;
    private String booster;
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
    public String getUrlimage() {
        return urlimage;
    }
    public void setUrlimage(String urlimage) {
        this.urlimage = urlimage;
    }
    public String getBooster() {
        return booster;
    }
    public void setBooster(String booster) {
        this.booster = booster;
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
