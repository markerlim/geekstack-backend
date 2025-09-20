package com.geekstack.cards.repository;

import static com.geekstack.cards.utils.Constants.C_GUNDAM;
import static com.geekstack.cards.utils.Constants.F_BOOSTER;
import static com.geekstack.cards.utils.Constants.F_CARDUID;
import static com.geekstack.cards.utils.Constants.F_COLOR;
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
import com.geekstack.cards.model.OnePieceCard;

import jakarta.annotation.Nullable;

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

    public List<GundamCard> getCardsByColor(List<String> color) {
        Criteria criteria = Criteria.where("color").in(color);
        Query query = new Query(criteria);

        QuerySorting(query, F_CARDUID, true);

        return mongoTemplate.find(query, GundamCard.class, C_GUNDAM);
    }

    public List<GundamCard> searchForCards(String term) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage()
                .matchingPhrase(term);

        TextQuery textQuery = new TextQuery(textCriteria);

        TextQuerySorting(textQuery, F_SERIES, true, F_CARDUID, true);

        List<GundamCard> results = mongoTemplate.find(textQuery, GundamCard.class, C_GUNDAM);

        return results;
    }

    // Search by text and color
    public List<GundamCard> searchForCards(String term, @Nullable List<String> color) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage()
                .matching(term);

        TextQuery textQuery = new TextQuery(textCriteria);

        // Optional: filter by color
        if (color != null && !color.isEmpty()) {
            textQuery.addCriteria(Criteria.where(F_COLOR).in(color));
        }

        TextQuerySorting(textQuery, F_SERIES, true, F_CARDUID, true);

        List<GundamCard> results = mongoTemplate.find(textQuery, GundamCard.class, C_GUNDAM);

        return results;
    }

    public List<GundamCard> getCardsByBatchUids(List<String> uids) {
        Criteria criteria = Criteria.where(F_CARDUID).in(uids);
        Query query = new Query(criteria);
        List<GundamCard> cards = mongoTemplate.find(query, GundamCard.class, C_GUNDAM);
        return cards;
    }
}
