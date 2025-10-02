package com.geekstack.cards.model;

import static com.geekstack.cards.utils.Constants.C_CHANGELOG;

import java.time.ZonedDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = C_CHANGELOG)
public class ChangeLog {

    @Id
    private String _id;

    private String tcg;
    private String type;
    private String target;
    private String changeLogDetail;
    private ZonedDateTime timestamp;

    public ChangeLog() {
    }

    public ChangeLog(String tcg, String type, String target, String changeLogDetail, ZonedDateTime timestamp) {
        this.tcg = tcg;
        this.type = type;
        this.target = target;
        this.changeLogDetail = changeLogDetail;
        this.timestamp = timestamp;
    }
    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
    }
    public String getTcg() {
        return tcg;
    }
    public void setTcg(String tcg) {
        this.tcg = tcg;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getTarget() {
        return target;
    }
    public void setTarget(String target) {
        this.target = target;
    }
    public String getChangeLogDetail() {
        return changeLogDetail;
    }
    public void setChangeLogDetail(String changeLogDetail) {
        this.changeLogDetail = changeLogDetail;
    }
    public ZonedDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }


}
