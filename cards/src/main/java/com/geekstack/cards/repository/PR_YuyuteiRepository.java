package com.geekstack.cards.repository;

import static com.geekstack.cards.utils.Constants.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.geekstack.cards.model.CardPriceYYT;

@Repository
public class PR_YuyuteiRepository{

    @Autowired
    private MongoTemplate mongoTemplate;

    public CardPriceYYT findCardById(String id) {
        Criteria criteria = Criteria.where(PRICE_YYT).is(id);
        Query query = new Query(criteria);

        CardPriceYYT results =  mongoTemplate.findOne(query, CardPriceYYT.class, C_YUYUTEI);
        ;
        return results;
    }

} 