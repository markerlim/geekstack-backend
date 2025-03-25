package com.geekstack.cards.repository;

import static com.geekstack.cards.utils.Constants.*;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.geekstack.cards.model.CookieRunDecklist;
import com.geekstack.cards.model.DragonballzFWDecklist;
import com.geekstack.cards.model.OnePieceDecklist;
import com.geekstack.cards.model.UnionArenaDecklist;
import com.geekstack.cards.model.UserDetails;

@Repository
public class UserDetailsMongoRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void createUser(String userId) {
        UserDetails userDetails = new UserDetails();
        userDetails.setUserId(userId);
        mongoTemplate.insert(userDetails, C_USER);
    }

    public UserDetails getOneUser(String userId) {
        Query query = new Query(Criteria.where(F_USERID).is(userId));
        UserDetails user = mongoTemplate.findOne(query, UserDetails.class, C_USER);
        return user;
    }

    public void createUnionArenaDecklist(UnionArenaDecklist decklist, String userId) {
        Query query = new Query(Criteria.where(F_USERID).is(userId));
        decklist.setDeckuid(UUID.randomUUID().toString());
        Update update = new Update().push(F_UADECKS, decklist);
        mongoTemplate.updateFirst(query, update, UserDetails.class, C_USER);
    }

    public void updateUnionArenaDecklist(UnionArenaDecklist decklist, String userId, String deckuid) {
        Query query = new Query(
                Criteria.where(F_USERID).is(userId).and(F_UADECKS).elemMatch(Criteria.where(F_DECKUID).is(deckuid)));
        decklist.setDeckuid(deckuid);
        Update update = new Update().set(F_UADECKS + ".$", decklist);
        mongoTemplate.updateFirst(query, update, UserDetails.class, C_USER);
    }

    public List<UnionArenaDecklist> loadUnionArenaDecklist(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(F_USERID).is(userId));
        query.fields().include(F_UADECKS);
        UserDetails user = mongoTemplate.findOne(query, UserDetails.class, C_USER);
        if (user == null || user.getUadecks() == null) {
            return List.of();
        }
        return user.getUadecks();
    }

    public void createOnePieceDecklist(OnePieceDecklist decklist, String userId) {
        Query query = new Query(Criteria.where(F_USERID).is(userId));
        decklist.setDeckuid(UUID.randomUUID().toString());
        Update update = new Update().push(F_OPDECKS, decklist);
        mongoTemplate.updateFirst(query, update, UserDetails.class, C_USER);
    }

    public void updateOnePieceDecklist(OnePieceDecklist decklist, String userId, String deckuid) {
        Query query = new Query(
                Criteria.where(F_USERID).is(userId).and(F_OPDECKS).elemMatch(Criteria.where(F_DECKUID).is(deckuid)));
        decklist.setDeckuid(deckuid);
        Update update = new Update().push(F_OPDECKS, decklist);
        mongoTemplate.updateFirst(query, update, UserDetails.class, C_USER);
    }

    public List<OnePieceDecklist> loadOnePieceDecklist(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(F_USERID).is(userId));
        query.fields().include(F_OPDECKS);
        UserDetails user = mongoTemplate.findOne(query, UserDetails.class, C_USER);
        if (user == null || user.getOpdecks() == null) {
            return List.of();
        }
        return user.getOpdecks();
    }

    public void createCookieRunDecklist(CookieRunDecklist decklist, String userId) {
        Query query = new Query(Criteria.where(F_USERID).is(userId));
        decklist.setDeckuid(UUID.randomUUID().toString());
        Update update = new Update().push(F_CRBDECKS, decklist);
        mongoTemplate.updateFirst(query, update, UserDetails.class, C_USER);
    }

    public void updateCookieRunDecklist(CookieRunDecklist decklist, String userId, String deckuid) {
        Query query = new Query(
                Criteria.where(F_USERID).is(userId).and(F_CRBDECKS).elemMatch(Criteria.where(F_DECKUID).is(deckuid)));
        decklist.setDeckuid(deckuid);
        Update update = new Update().push(F_CRBDECKS, decklist);
        mongoTemplate.updateFirst(query, update, UserDetails.class, C_USER);
    }

    public List<CookieRunDecklist> loadCookieRunDecklist(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(F_USERID).is(userId));
        query.fields().include(F_CRBDECKS);
        UserDetails user = mongoTemplate.findOne(query, UserDetails.class, C_USER);
        if (user == null || user.getCrbdecks() == null) {
            return List.of();
        }
        return user.getCrbdecks();
    }

    public void createDragonballzFWDecklist(DragonballzFWDecklist decklist, String userId) {
        Query query = new Query(Criteria.where(F_USERID).is(userId));
        decklist.setDeckuid(UUID.randomUUID().toString());
        Update update = new Update().push(F_DBZFWDECKS, decklist);
        mongoTemplate.updateFirst(query, update, UserDetails.class, C_USER);
    }

    public void updateDragonballzFWDecklist(DragonballzFWDecklist decklist, String userId, String deckuid) {
        Query query = new Query(
                Criteria.where(F_USERID).is(userId).and(F_DBZFWDECKS).elemMatch(Criteria.where(F_DECKUID).is(deckuid)));
        decklist.setDeckuid(deckuid);
        Update update = new Update().push(F_DBZFWDECKS, decklist);
        mongoTemplate.updateFirst(query, update, UserDetails.class, C_USER);
    }

    public List<DragonballzFWDecklist> loadDragonballzFWDecklist(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(F_USERID).is(userId));
        query.fields().include(F_DBZFWDECKS);
        UserDetails user = mongoTemplate.findOne(query, UserDetails.class, C_USER);
        if (user == null || user.getDbzfwdecks() == null) {
            return List.of();
        }
        return user.getDbzfwdecks();
    }

    public void deleteDecklist(String tcg, String userId, String deckId) {

        System.out.println("Received tcg: " + tcg);

        String field = "";
        if (T_UA.trim().equals(tcg)) {
            field = F_UADECKS;
        } else if (T_OP.equals(tcg)) {
            field = F_OPDECKS;
        } else if (T_CRB.equals(tcg)) {
            field = F_CRBDECKS;
        } else if (T_DBZ.equals(tcg)) {
            field = F_DBZFWDECKS;
        }

        if (field.isEmpty()) {
            throw new IllegalArgumentException("Invalid tcg value, field cannot be empty");
        }

        Query query = new Query(Criteria.where(F_USERID).is(userId));
        Update update = new Update().pull(field, Query.query(Criteria.where(F_DECKUID).is(deckId)));
        mongoTemplate.updateFirst(query, update, UserDetails.class, C_USER);
    }
}
