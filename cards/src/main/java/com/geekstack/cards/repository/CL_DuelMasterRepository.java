package com.geekstack.cards.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Repository;

import com.geekstack.cards.model.DuelMastersCard;

import jakarta.annotation.Nullable;

import static com.geekstack.cards.utils.Constants.*;

@Repository
public class CL_DuelMasterRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<DuelMastersCard> getCards() {
        return mongoTemplate.findAll(DuelMastersCard.class, C_DUELMASTER);
    }

    public List<DuelMastersCard> getCardsByBooster(String booster) {
        Criteria criteria = Criteria.where(F_BOOSTER).is(booster);
        Query query = new Query(criteria);

        QuerySorting(query, F_CARDUID, true);

        List<DuelMastersCard> results = mongoTemplate.find(query, DuelMastersCard.class, C_DUELMASTER);
        return results;
    }

    public List<DuelMastersCard> getCardsByColor(List<String> color) {
        Criteria criteria = Criteria.where(F_CIVILIZATION).in(color);
        Query query = new Query(criteria);

        QuerySorting(query, F_CARDUID, true);

        List<DuelMastersCard> results = mongoTemplate.find(query, DuelMastersCard.class, C_DUELMASTER);
        return results;
    }

    public List<DuelMastersCard> searchForCards(String term) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage()
                .matchingPhrase(term);

        TextQuery textQuery = new TextQuery(textCriteria);

        TextQuerySorting(textQuery, F_BOOSTER, false);

        List<DuelMastersCard> results = mongoTemplate.find(textQuery, DuelMastersCard.class, C_DUELMASTER);

        return results;
    }

    public List<DuelMastersCard> searchForCards(String term, @Nullable List<String> color) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage()
                .matchingPhrase(term);

        TextQuery textQuery = new TextQuery(textCriteria);

        if (color != null && !color.isEmpty()) {
            textQuery.addCriteria(Criteria.where(F_COLOR).in(color));
        }

        TextQuerySorting(textQuery, F_BOOSTER, false);

        List<DuelMastersCard> results = mongoTemplate.find(textQuery, DuelMastersCard.class, C_DUELMASTER);

        return results;
    }

    public List<String> getDistinctBooster() {
        return mongoTemplate.findDistinct(new Query(), F_BOOSTER, C_DUELMASTER, String.class);
    }

    public List<String> getDistinctCivilization(String booster) {
        System.out.println("booster: " + booster);
        Criteria criteria = Criteria.where(F_BOOSTER).is(booster);
        Query query = new Query(criteria);

        return mongoTemplate.findDistinct(query, F_CIVILIZATION, C_DUELMASTER, String.class);
    }

    public List<String> getDistinctCardType(String booster) {

        Criteria criteria = Criteria.where(F_BOOSTER).is(booster);
        Query query = new Query(criteria);

        return mongoTemplate.findDistinct(query, F_CARDTYPE, C_DUELMASTER, String.class);
    }

    public List<DuelMastersCard> getCardsByBatchUids(List<String> uids) {
        Criteria criteria = Criteria.where(F_CARDUID).in(uids);
        Query query = new Query(criteria);
        List<DuelMastersCard> cards = mongoTemplate.find(query, DuelMastersCard.class, C_DUELMASTER);
        return cards;
    }

}
