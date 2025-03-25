package com.geekstack.cards.model;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "BT_unionarena")
public class UnionArenaBooster {

    private String _id;
    private String animecode;
    private String currentAnime;
    private List<String> listofboosters;
    private List<String> listofcolors;
    private List<String> listofrarities;

    public UnionArenaBooster(){

    }
    
    public UnionArenaBooster(String _id, String animecode, String currentAnime, List<String> listofboosters,
            List<String> listofcolors, List<String> listofrarities) {
        this._id = _id;
        this.animecode = animecode;
        this.currentAnime = currentAnime;
        this.listofboosters = listofboosters;
        this.listofcolors = listofcolors;
        this.listofrarities = listofrarities;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAnimecode() {
        return animecode;
    }

    public void setAnimecode(String animecode) {
        this.animecode = animecode;
    }

    public String getCurrentAnime() {
        return currentAnime;
    }

    public void setCurrentAnime(String currentAnime) {
        this.currentAnime = currentAnime;
    }

    public List<String> getListofboosters() {
        return listofboosters;
    }

    public void setListofboosters(List<String> listofboosters) {
        this.listofboosters = listofboosters;
    }

    public List<String> getListofcolors() {
        return listofcolors;
    }

    public void setListofcolors(List<String> listofcolors) {
        this.listofcolors = listofcolors;
    }

    public List<String> getListofrarities() {
        return listofrarities;
    }

    public void setListofrarities(List<String> listofrarities) {
        this.listofrarities = listofrarities;
    }


    
}
