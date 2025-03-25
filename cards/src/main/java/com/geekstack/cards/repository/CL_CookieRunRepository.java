package com.geekstack.cards.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Repository;

import com.geekstack.cards.model.CookieRunCard;

import static com.geekstack.cards.utils.Constants.*;

@Repository
public class CL_CookieRunRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<CookieRunCard> getCards() {
        return mongoTemplate.findAll(CookieRunCard.class);
    }

    public List<CookieRunCard> getCardsByBooster(String booster) {
        Criteria criteria = Criteria.where(F_BOOSTER).is(booster);
        Query query = new Query(criteria);

        QuerySorting(query, F_CARDUID, true);

        List<CookieRunCard> results = mongoTemplate.find(query, CookieRunCard.class, C_COOKIERUN);
        return results;
    }

    public List<CookieRunCard> searchForCards(String term) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage()
                .matchingPhrase(term);

        TextQuery textQuery = TextQuery.queryText(textCriteria);

        TextQuerySorting(textQuery, F_BOOSTER, true, F_CARDUID, true);

        List<CookieRunCard> results = mongoTemplate.find(textQuery, CookieRunCard.class, C_COOKIERUN);

        return results;
    }

    public List<String> getDistinctBooster() {
        return mongoTemplate.findDistinct(new Query(), F_BOOSTER, C_COOKIERUN, String.class);
    }

}