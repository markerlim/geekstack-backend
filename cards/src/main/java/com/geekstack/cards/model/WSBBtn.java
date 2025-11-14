package com.geekstack.cards.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "BT_wsblau")
public class WSBBtn {

    @Id
    private String _id;

    @Field("pathname")
    private String pathname;

    @Field("title")
    private String title;

    @Field("category")
    private String category;

    @Field("release_date")
    private String releaseDate;

    @Field("url")
    private String url;

    @Field("alt")
    private String alt;

    @Field("imageSrc")
    private String imageSrc;

    @Field("titleJP")
    private String titleJP;

    @Field("categoryJP")
    private String categoryJP;

    @Field("altJP")
    private String altJP;

    // Constructors
    public WSBBtn() {
    }

    public WSBBtn(String _id, String pathname, String title, String category, String releaseDate,
            String url, String alt, String imageSrc, String titleJP, String categoryJP, String altJP) {
        this._id = _id;
        this.pathname = pathname;
        this.title = title;
        this.category = category;
        this.releaseDate = releaseDate;
        this.url = url;
        this.alt = alt;
        this.imageSrc = imageSrc;
        this.titleJP = titleJP;
        this.categoryJP = categoryJP;
        this.altJP = altJP;
    }

    // Getters and Setters
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPathname() {
        return pathname;
    }

    public void setPathname(String pathname) {
        this.pathname = pathname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getTitleJP() {
        return titleJP;
    }

    public void setTitleJP(String titleJP) {
        this.titleJP = titleJP;
    }

    public String getCategoryJP() {
        return categoryJP;
    }

    public void setCategoryJP(String categoryJP) {
        this.categoryJP = categoryJP;
    }

    public String getAltJP() {
        return altJP;
    }

    public void setAltJP(String altJP) {
        this.altJP = altJP;
    }
}