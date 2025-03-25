package com.geekstack.cards.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;

@Document(collection = "BT_unionarena")
public class FiltersButton {

    @Id
    private String id; 
    private String code;

    @Field("param")
    private String param;
    private ArrayList<String> listofboosters;
    private ArrayList<String> listofcolors;
    private ArrayList<String> listofrarities;

    
    public FiltersButton() {
        
    }


    public FiltersButton(String id, String code, String param, ArrayList<String> listofboosters,
            ArrayList<String> listofcolors, ArrayList<String> listofrarities) {
        this.id = id;
        this.code = code;
        this.param = param;
        this.listofboosters = listofboosters;
        this.listofcolors = listofcolors;
        this.listofrarities = listofrarities;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public String getcode() {
        return code;
    }


    public void setcode(String code) {
        this.code = code;
    }


    public String getParam() {
        return param;
    }


    public void setParam(String param) {
        this.param = param;
    }


    public ArrayList<String> getlistofboosters() {
        return listofboosters;
    }


    public void setlistofboosters(ArrayList<String> listofboosters) {
        this.listofboosters = listofboosters;
    }


    public ArrayList<String> getlistofcolors() {
        return listofcolors;
    }


    public void setlistofcolors(ArrayList<String> listofcolors) {
        this.listofcolors = listofcolors;
    }


    public ArrayList<String> getlistofrarities() {
        return listofrarities;
    }


    public void setlistofrarities(ArrayList<String> listofrarities) {
        this.listofrarities = listofrarities;
    }

    
}
