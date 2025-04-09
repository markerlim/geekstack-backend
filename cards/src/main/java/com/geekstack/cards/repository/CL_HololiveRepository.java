package com.geekstack.cards.repository;

import static com.geekstack.cards.utils.Constants.C_HOLOLIVE;
import static com.geekstack.cards.utils.Constants.F_BOOSTER;
import static com.geekstack.cards.utils.Constants.F_CARDUID;
import static com.geekstack.cards.utils.Constants.QuerySorting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.geekstack.cards.model.HololiveCard;

@Repository
public class CL_HololiveRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<HololiveCard> getCards() {
        return mongoTemplate.findAll(HololiveCard.class, C_HOLOLIVE);
    }

    public List<HololiveCard> getCardsByBooster(String booster) {
        Criteria criteria = Criteria.where(F_BOOSTER).is(booster);
        Query query = new Query(criteria);

        QuerySorting(query, F_CARDUID, true);

        List<HololiveCard> results = mongoTemplate.find(query, HololiveCard.class, C_HOLOLIVE);
        return results;
    }
}
