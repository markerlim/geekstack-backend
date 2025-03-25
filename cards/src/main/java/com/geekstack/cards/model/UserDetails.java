package com.geekstack.cards.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import static com.geekstack.cards.utils.Constants.*;

public class UserDetails {
    
    @Id
    private String userId;

    @Field(F_UADECKS)
    private List<UnionArenaDecklist> uadecks;

    @Field(F_OPDECKS)
    private List<OnePieceDecklist> opdecks;

    @Field(F_CRBDECKS)
    private List<CookieRunDecklist> crbdecks;

    @Field(F_DBZFWDECKS)
    private List<DragonballzFWDecklist> dbzfwdecks;

    public UserDetails(){
        
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<UnionArenaDecklist> getUadecks() {
        return uadecks;
    }

    public void setUadecks(List<UnionArenaDecklist> uadecks) {
        this.uadecks = uadecks;
    }

    public List<OnePieceDecklist> getOpdecks() {
        return opdecks;
    }

    public void setOpdecks(List<OnePieceDecklist> opdecks) {
        this.opdecks = opdecks;
    }

    public List<CookieRunDecklist> getCrbdecks() {
        return crbdecks;
    }

    public void setCrbdecks(List<CookieRunDecklist> crbdecks) {
        this.crbdecks = crbdecks;
    }

    public List<DragonballzFWDecklist> getDbzfwdecks() {
        return dbzfwdecks;
    }

    public void setDbzfwdecks(List<DragonballzFWDecklist> dbzfwdecks) {
        this.dbzfwdecks = dbzfwdecks;
    }


}
