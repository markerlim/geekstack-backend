package com.geekstack.cards.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Repository;

import com.geekstack.cards.model.DuelMasterCard;

import static com.geekstack.cards.utils.Constants.*;

@Repository
public class CL_DuelMasterRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<DuelMasterCard> getCards() {
        return mongoTemplate.findAll(DuelMasterCard.class);
    }

    public List<DuelMasterCard> getCardsByBooster(String booster) {
        Criteria criteria = Criteria.where(F_BOOSTER).is(booster);
        Query query = new Query(criteria);

        QuerySorting(query, F_CARDUID, true);

        List<DuelMasterCard> results = mongoTemplate.find(query, DuelMasterCard.class, C_DUELMASTER);
        return results;
    }

    public List<DuelMasterCard> searchForCards(String term){
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage()
        .matchingPhrase(term);

        TextQuery textQuery = new TextQuery(textCriteria);

        TextQuerySorting(textQuery, F_BOOSTER, false);

        List<DuelMasterCard> results = mongoTemplate.find(textQuery, DuelMasterCard.class, C_DUELMASTER);

        return results;
    }
    public List<String> getDistinctBooster(){
        return mongoTemplate.findDistinct(new Query(),F_BOOSTER, C_DUELMASTER, String.class);
    }

    public List<String> getDistinctCivilization(String booster){
        System.out.println("booster: "+booster);
        Criteria criteria = Criteria.where(F_BOOSTER).is(booster);
        Query query = new Query(criteria);

        return mongoTemplate.findDistinct(query,F_CIVILIZATION, C_DUELMASTER, String.class);
    }

    public List<String> getDistinctCardType(String booster){

        Criteria criteria = Criteria.where(F_BOOSTER).is(booster);
        Query query = new Query(criteria);

        return mongoTemplate.findDistinct(query,F_CARDTYPE, C_DUELMASTER, String.class);
    }


}
