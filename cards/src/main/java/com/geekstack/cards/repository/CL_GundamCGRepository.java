package com.geekstack.cards.repository;

import static com.geekstack.cards.utils.Constants.C_GUNDAM;
import static com.geekstack.cards.utils.Constants.F_BOOSTER;
import static com.geekstack.cards.utils.Constants.F_CARDUID;
import static com.geekstack.cards.utils.Constants.F_PACKAGEID;
import static com.geekstack.cards.utils.Constants.F_SERIES;
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

import com.geekstack.cards.model.GundamCard;

@Repository
public class CL_GundamCGRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<GundamCard> getCards() {
        return mongoTemplate.findAll(GundamCard.class, C_GUNDAM);
    }

    public List<GundamCard> getCardsByBooster(String booster) {
        Criteria criteria = Criteria.where(F_SERIES).is(booster.toUpperCase());
        Query query = new Query(criteria);

        QuerySorting(query, F_CARDUID, true);

        List<GundamCard> results = mongoTemplate.find(query, GundamCard.class, C_GUNDAM);
        return results;
    }

    public List<GundamCard> searchForCards(String term) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage()
                .matchingPhrase(term);

        TextQuery textQuery = new TextQuery(textCriteria);

        TextQuerySorting(textQuery, F_SERIES, true, F_CARDUID, true);

        List<GundamCard> results = mongoTemplate.find(textQuery, GundamCard.class, C_GUNDAM);

        return results;
    }
}
