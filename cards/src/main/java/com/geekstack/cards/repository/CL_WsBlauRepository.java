package com.geekstack.cards.repository;

import static com.geekstack.cards.utils.Constants.C_WSBLAU;
import static com.geekstack.cards.utils.Constants.F_BOOSTER;
import static com.geekstack.cards.utils.Constants.F_CARDUID;
import static com.geekstack.cards.utils.Constants.QuerySorting;
import static com.geekstack.cards.utils.Constants.TextQuerySorting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Repository;

import com.geekstack.cards.model.WeibSchwarzBlauCard;

@Repository
public class CL_WsBlauRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<WeibSchwarzBlauCard> getCards() {
        return mongoTemplate.findAll(WeibSchwarzBlauCard.class, C_WSBLAU);
    }

    public List<WeibSchwarzBlauCard> getCardsBySet(String booster) {

        Criteria criteria = Criteria.where(F_BOOSTER).is(booster);

        Query query = new Query(criteria);

        QuerySorting(query, F_CARDUID, true);

        List<WeibSchwarzBlauCard> results = mongoTemplate.find(query, WeibSchwarzBlauCard.class, C_WSBLAU);

        return results;
    }

    public List<WeibSchwarzBlauCard> searchForCardsFull(String term) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage()
                .matchingPhrase(term);

        TextQuery textQuery = new TextQuery(textCriteria);

        TextQuerySorting(textQuery, F_CARDUID, true);

        List<WeibSchwarzBlauCard> results = mongoTemplate.find(textQuery, WeibSchwarzBlauCard.class, C_WSBLAU);

        return results;
    }
}
