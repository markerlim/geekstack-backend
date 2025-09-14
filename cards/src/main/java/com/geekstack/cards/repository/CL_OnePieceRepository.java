package com.geekstack.cards.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Repository;

import com.geekstack.cards.model.OnePieceCard;
import com.geekstack.cards.model.UnionArenaCard;

import static com.geekstack.cards.utils.Constants.*;

@Repository
public class CL_OnePieceRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<OnePieceCard> getCards() {
        return mongoTemplate.findAll(OnePieceCard.class, C_ONEPIECE);
    }

    public List<OnePieceCard> getCardsByBooster(String booster) {
        Criteria criteria = Criteria.where(F_BOOSTER).is(booster.toUpperCase());

        Query query = new Query(criteria);

        QuerySorting(query, F_CARDUID, true);

        List<OnePieceCard> results = mongoTemplate.find(query, OnePieceCard.class, C_ONEPIECE);

        return results;
    }

    public List<OnePieceCard> getCardsIsLeader(int page, int size) {

        Criteria criteria = Criteria.where(F_CATEGORY).is(T_LEADER.toUpperCase());

        Query query = new Query(criteria);

        QuerySorting(query, F_CARDUID, true);

        query.skip((long) page * size);
        query.limit(size);

        return mongoTemplate.find(query, OnePieceCard.class, C_ONEPIECE);
    }

    public List<OnePieceCard> getCardsIsLeader(int page, int size, String searchTerm) {
        // Create base criteria for leaders
        Criteria leaderCriteria = Criteria.where(F_CATEGORY).is(T_LEADER.toUpperCase());

        // Create text search criteria if searchTerm is provided
        TextCriteria textCriteria = null;
        if (searchTerm != null && !searchTerm.isEmpty()) {
            textCriteria = TextCriteria.forDefaultLanguage().matchingPhrase(searchTerm);
        }

        // Build query combining both criteria
        Query query;
        if (textCriteria != null) {
            query = TextQuery.queryText(textCriteria)
                    .sortByScore()
                    .addCriteria(leaderCriteria);
        } else {
            query = new Query(leaderCriteria);
        }

        // Apply your existing sorting
        QuerySorting(query, F_BOOSTER, true, F_CARDUID, true);

        // Apply pagination
        query.skip((long) page * size);
        query.limit(size);

        return mongoTemplate.find(query, OnePieceCard.class, C_ONEPIECE);
    }

    public List<OnePieceCard> searchForCards(String term) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage()
                .matchingPhrase(term);

        TextQuery textQuery = new TextQuery(textCriteria);

        TextQuerySorting(textQuery, F_BOOSTER, true, F_CARDUID, true);

        List<OnePieceCard> results = mongoTemplate.find(textQuery, OnePieceCard.class, C_ONEPIECE);

        return results;
    }

    public List<String> getDistinctCategory(String booster) {

        Criteria criteria = Criteria.where(F_BOOSTER).is(booster);
        Query query = new Query(criteria);
        query.fields().include(F_CATEGORY);

        return mongoTemplate.findDistinct(query, F_CATEGORY, C_ONEPIECE, String.class);
    }

    public List<String> getDistinctColor(String booster) {

        Criteria criteria = Criteria.where(F_BOOSTER).is(booster);
        Query query = new Query(criteria);
        query.fields().include(F_COLOR);

        return mongoTemplate.findDistinct(query, F_COLOR, C_ONEPIECE, String.class);
    }

    public List<String> getDistinctRarity(String booster) {

        Criteria criteria = Criteria.where(F_BOOSTER).is(booster);
        Query query = new Query(criteria);
        query.fields().include(F_RARITY);

        return mongoTemplate.findDistinct(query, F_RARITY, C_ONEPIECE, String.class);
    }

    public List<OnePieceCard> getCardsByBatchUids(List<String> uids) {
        Criteria criteria = Criteria.where(F_CARDUID).in(uids);
        Query query = new Query(criteria);
        List<OnePieceCard> cards = mongoTemplate.find(query, OnePieceCard.class, C_ONEPIECE);
        return cards;
    }
}
