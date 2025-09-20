package com.geekstack.cards.repository;

import static com.geekstack.cards.utils.Constants.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Repository;

import com.geekstack.cards.model.DragonBallzFWCard;

import jakarta.annotation.Nullable;

@Repository
public class CL_DragonBallzFWRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<DragonBallzFWCard> getCards() {
        return mongoTemplate.findAll(DragonBallzFWCard.class, C_DRAGONBALLZFW);
    }

    public List<DragonBallzFWCard> getCardsByBooster(String booster) {
        Criteria criteria = Criteria.where(F_BOOSTER).is(booster.toUpperCase());
        Query query = new Query(criteria);

        QuerySorting(query, F_CARDUID, true);

        List<DragonBallzFWCard> results = mongoTemplate.find(query, DragonBallzFWCard.class, C_DRAGONBALLZFW);
        return results;
    }

    public List<DragonBallzFWCard> getCardsByColor(List<String> color) {
        Criteria criteria = Criteria.where(F_COLOR).in(color);
        Query query = new Query(criteria);

        QuerySorting(query, F_CARDUID, true);

        List<DragonBallzFWCard> results = mongoTemplate.find(query, DragonBallzFWCard.class, C_DRAGONBALLZFW);
        return results;
    }

    public List<DragonBallzFWCard> searchForCards(String term) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage()
                .matchingPhrase(term);
        TextQuery textQuery = new TextQuery(textCriteria);

        TextQuerySorting(textQuery, F_BOOSTER, true, F_CARDUID, true);

        List<DragonBallzFWCard> results = mongoTemplate.find(textQuery, DragonBallzFWCard.class, C_DRAGONBALLZFW);
        return results;
    }

    // Search by text and color exclude card type
    public List<DragonBallzFWCard> searchForCards(String term, @Nullable List<String> color,
            @Nullable String excludeCardType) {
        TextCriteria textCriteria = TextCriteria.forDefaultLanguage()
                .matching(term);

        TextQuery textQuery = new TextQuery(textCriteria);

        // Optional: filter by color
        if (color != null && !color.isEmpty()) {
            textQuery.addCriteria(Criteria.where(F_COLOR).in(color));
        }

        // Optional: exclude card type
        if (excludeCardType != null && !excludeCardType.isEmpty()) {
            textQuery.addCriteria(Criteria.where(F_CARDTYPE).ne(excludeCardType));
        }

        // Keep your sorting logic
        TextQuerySorting(textQuery, F_BOOSTER, true, F_CARDUID, true);

        return mongoTemplate.find(textQuery, DragonBallzFWCard.class, C_DRAGONBALLZFW);
    }

    public List<String> getDistinctCardtype(String booster) {

        Criteria criteria = Criteria.where(F_BOOSTER).is(booster.toUpperCase());
        Query query = new Query(criteria);
        query.fields().include(F_CARDTYPE.toLowerCase());

        return mongoTemplate.findDistinct(query, F_CARDTYPE.toLowerCase(), C_DRAGONBALLZFW, String.class);
    }

    public List<String> getDistinctColor(String booster) {

        Criteria criteria = Criteria.where(F_BOOSTER).is(booster.toUpperCase());
        Query query = new Query(criteria);
        query.fields().include(F_COLOR);

        return mongoTemplate.findDistinct(query, F_COLOR, C_DRAGONBALLZFW, String.class);
    }

    public List<String> getDistinctRarity(String booster) {

        Criteria criteria = Criteria.where(F_BOOSTER).is(booster.toUpperCase());
        Query query = new Query(criteria);
        query.fields().include(F_RARITY);

        return mongoTemplate.findDistinct(query, F_RARITY, C_DRAGONBALLZFW, String.class);
    }

    public List<DragonBallzFWCard> getCardsByBatchUids(List<String> uids) {
        Criteria criteria = Criteria.where(F_CARDUID).in(uids);
        Query query = new Query(criteria);
        List<DragonBallzFWCard> cards = mongoTemplate.find(query, DragonBallzFWCard.class, C_DRAGONBALLZFW);
        return cards;
    }

}
