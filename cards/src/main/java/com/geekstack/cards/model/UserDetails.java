package com.geekstack.cards.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import static com.geekstack.cards.utils.Constants.*;

public class UserDetails {

    @Id
    private String userId;

    @Field(F_UADECKS)
    private List<GenericDecklist> uadecks;

    @Field(F_OPDECKS)
    private List<GenericDecklist> opdecks;

    @Field(F_CRBDECKS)
    private List<GenericDecklist> crbdecks;

    @Field(F_DBZFWDECKS)
    private List<GenericDecklist> dbzfwdecks;

    @Field(F_DMDECKS)
    private List<GenericDecklist> dmdecks;

    @Field(F_GCGDECKS)
    private List<GenericDecklist> gcgdecks;

    @Field(F_HOCGDECKS)
    private List<GenericDecklist> hocgdecks;

    @Field(F_WSBDECKS)
    private List<GenericDecklist> wsbdecks;

    public UserDetails() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<GenericDecklist> getUadecks() {
        return uadecks;
    }

    public void setUadecks(List<GenericDecklist> uadecks) {
        this.uadecks = uadecks;
    }

    public List<GenericDecklist> getOpdecks() {
        return opdecks;
    }

    public void setOpdecks(List<GenericDecklist> opdecks) {
        this.opdecks = opdecks;
    }

    public List<GenericDecklist> getCrbdecks() {
        return crbdecks;
    }

    public void setCrbdecks(List<GenericDecklist> crbdecks) {
        this.crbdecks = crbdecks;
    }

    public List<GenericDecklist> getDbzfwdecks() {
        return dbzfwdecks;
    }

    public void setDbzfwdecks(List<GenericDecklist> dbzfwdecks) {
        this.dbzfwdecks = dbzfwdecks;
    }

    public List<GenericDecklist> getDmdecks() {
        return dmdecks;
    }

    public void setDmdecks(List<GenericDecklist> dmdecks) {
        this.dmdecks = dmdecks;
    }

    public List<GenericDecklist> getGcgdecks() {
        return gcgdecks;
    }

    public void setGcgdecks(List<GenericDecklist> gcgdecks) {
        this.gcgdecks = gcgdecks;
    }

    public List<GenericDecklist> getHocgdecks() {
        return hocgdecks;
    }

    public void setHocgdecks(List<GenericDecklist> hocgdecks) {
        this.hocgdecks = hocgdecks;
    }

    public List<GenericDecklist> getWsbdecks() {
        return wsbdecks;
    }

    public void setWsbdecks(List<GenericDecklist> wsbdecks) {
        this.wsbdecks = wsbdecks;
    }

    
}