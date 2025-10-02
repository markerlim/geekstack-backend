package com.geekstack.cards.repository;

import static com.geekstack.cards.utils.Constants.C_CHANGELOG;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.geekstack.cards.model.ChangeLog;

import jakarta.annotation.Nullable;

@Repository
public class ChangeLogRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public ChangeLog getLatestChange() {
        Query query = new Query()
                .with(Sort.by(Sort.Direction.DESC, "timestamp"))
                .limit(1);
        return mongoTemplate.findOne(query, ChangeLog.class, C_CHANGELOG);
    }

    public ChangeLog getLatestChangeByParams(@Nullable String target, @Nullable String type, @Nullable String tcg) {
        Query query = new Query();
        if (target != null) {
            query.addCriteria(Criteria.where("target").is(target));
        }
        if (type != null) {
            query.addCriteria(Criteria.where("type").is(type));
        }
        if (tcg != null) {
            query.addCriteria(Criteria.where("tcg").is(tcg));
        }
        query.with(Sort.by(Sort.Direction.DESC, "timestamp"))
                .limit(1);
        return mongoTemplate.findOne(query, ChangeLog.class, C_CHANGELOG);
    }

}
