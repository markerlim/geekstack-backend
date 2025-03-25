package com.geekstack.cards.repository;

import static com.geekstack.cards.utils.Constants.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.geekstack.cards.model.CardPriceFULLA;

@Repository
public class PR_FullaheadRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public CardPriceFULLA findCardById(String id) {
        Criteria criteria = Criteria.where(PRICE_FULLA).is(id);
        Query query = new Query(criteria);

        CardPriceFULLA results =  mongoTemplate.findOne(query, CardPriceFULLA.class, C_FULLAHEAD);
        
        return results;
    }
}
