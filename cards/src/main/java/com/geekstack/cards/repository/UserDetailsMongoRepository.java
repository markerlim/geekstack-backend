package com.geekstack.cards.repository;

import static com.geekstack.cards.utils.Constants.*;

import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.ObjectOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.geekstack.cards.model.CookieRunDecklist;
import com.geekstack.cards.model.DragonballzFWDecklist;
import com.geekstack.cards.model.DuelMasterDecklist;
import com.geekstack.cards.model.GenericDecklist;
import com.geekstack.cards.model.GundamDecklist;
import com.geekstack.cards.model.OnePieceDecklist;
import com.geekstack.cards.model.UnionArenaDecklist;
import com.geekstack.cards.model.UserDetails;
import com.geekstack.cards.model.WeibSchwarzBlauDecklist;

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

        public List<GenericDecklist> loadDecklist(String userId, String deckfield) {
                Aggregation aggregation = Aggregation.newAggregation(
                                Aggregation.match(Criteria.where(F_USERID).is(userId)),
                                Aggregation.unwind(deckfield),
                                // Project the fields from the unwound array
                                Aggregation.project()
                                                .and(deckfield + ".deckuid").as("deckuid")
                                                .and(deckfield + ".deckName").as("deckName")
                                                .and(deckfield + ".image").as("image"));

                return mongoTemplate.aggregate(aggregation, C_USER, GenericDecklist.class)
                                .getMappedResults();
        }

        public String createUnionArenaDecklist(GenericDecklist decklist, String userId) {
                Query query = new Query(Criteria.where(F_USERID).is(userId));
                String uuid = UUID.randomUUID().toString();
                decklist.setDeckuid(uuid);
                Update update = new Update().push(F_UADECKS, decklist);
                mongoTemplate.updateFirst(query, update, UserDetails.class, C_USER);
                return uuid;
        }

        public String updateUnionArenaDecklist(GenericDecklist decklist, String userId, String deckuid) {
                Query query = new Query(
                                Criteria.where(F_USERID).is(userId).and(F_UADECKS)
                                                .elemMatch(Criteria.where(F_DECKUID).is(deckuid)));
                decklist.setDeckuid(deckuid);
                Update update = new Update().set(F_UADECKS + ".$", decklist);
                mongoTemplate.updateFirst(query, update, UserDetails.class, C_USER);
                return deckuid;
        }

        public UnionArenaDecklist loadUnionArenaDecklist(String userId, String deckUid) {
                String fieldCat = F_UADECKS;
                String collectionCat = C_UNIONARENA;
                Aggregation aggregation = Aggregation.newAggregation(
                                Aggregation.match(Criteria.where(F_USERID).is(userId)),
                                Aggregation.unwind(fieldCat),
                                // Add match condition for specific deckuid
                                Aggregation.match(Criteria.where(fieldCat + ".deckuid").is(deckUid)),
                                Aggregation.unwind(fieldCat + ".listofcards", true),

                                // Store the count and card ID
                                Aggregation.project()
                                                .and(fieldCat).as(fieldCat)
                                                .and(fieldCat + ".listofcards.count").as("tempCount")
                                                .and(fieldCat + ".listofcards._id").as("tempCardId"),

                                Aggregation.lookup(
                                                collectionCat,
                                                "tempCardId",
                                                "_id",
                                                "cardDetails"),

                                Aggregation.unwind("cardDetails", true),

                                // Merge the count field into the cardDetails document using ObjectOperators
                                Aggregation.project()
                                                .and(ObjectOperators.MergeObjects.merge(
                                                                "$cardDetails",
                                                                new Document("count", "$tempCount")))
                                                .as("mergedCard")
                                                .and(fieldCat).as(fieldCat),

                                // Group by deck fields and collect the merged cards
                                Aggregation.group(
                                                Fields.from(
                                                                Fields.field("deckuid", "$" + fieldCat + ".deckuid"),
                                                                Fields.field("deckName", "$" + fieldCat + ".deckName"),
                                                                Fields.field("image", "$" + fieldCat + ".image")))
                                                .push("$mergedCard")
                                                .as("listofcards"),

                                // Project final structure
                                Aggregation.project()
                                                .and("_id.deckuid").as("deckuid")
                                                .and("_id.deckName").as("deckName")
                                                .and("_id.image").as("image")
                                                .and("listofcards").as("listofcards"));

                // Use getUniqueMappedResult() to get a single result
                return mongoTemplate.aggregate(aggregation, C_USER, UnionArenaDecklist.class)
                                .getUniqueMappedResult();
        }

        /*
         * Code for returning entire list with all information
         * public List<UnionArenaDecklist> loadUnionArenaDecklist(String userId) {
         * Aggregation aggregation = Aggregation.newAggregation(
         * Aggregation.match(Criteria.where(F_USERID).is(userId)),
         * Aggregation.unwind(F_UADECKS),
         * Aggregation.unwind(F_UADECKS + ".listofcards"),
         * 
         * // Store the count and card ID
         * Aggregation.project()
         * .and(F_UADECKS).as(F_UADECKS)
         * .and(F_UADECKS + ".listofcards.count").as("tempCount")
         * .and(F_UADECKS + ".listofcards._id").as("tempCardId"),
         * 
         * Aggregation.lookup(
         * C_UNIONARENA,
         * "tempCardId",
         * "_id",
         * "cardDetails"),
         * 
         * Aggregation.unwind("cardDetails"),
         * 
         * // Merge the count field into the cardDetails document using ObjectOperators
         * Aggregation.project()
         * .and(ObjectOperators.MergeObjects.merge(
         * "$cardDetails",
         * new Document("count", "$tempCount")))
         * .as("mergedCard")
         * .and(F_UADECKS).as(F_UADECKS),
         * 
         * // Group by deck fields and collect the merged cards
         * Aggregation.group(
         * Fields.from(
         * Fields.field("deckuid", "$" + F_UADECKS + ".deckuid"),
         * Fields.field("deckName", "$" + F_UADECKS + ".deckName"),
         * Fields.field("image", "$" + F_UADECKS + ".image")))
         * .push("$mergedCard")
         * .as("listofcards"),
         * 
         * // Project final structure
         * Aggregation.project()
         * .and("deckuid").as("deckuid")
         * .and("deckName").as("deckName")
         * .and("image").as("image")
         * .and("listofcards").as("listofcards"));
         * 
         * return mongoTemplate.aggregate(aggregation, C_USER, UnionArenaDecklist.class)
         * .getMappedResults();
         * }
         */

        /**
         * V1 of loading deck
         * public List<UnionArenaDecklist> loadUnionArenaDecklist(String userId) {
         * Query query = new Query();
         * query.addCriteria(Criteria.where(F_USERID).is(userId));
         * query.fields().include(F_UADECKS);
         * UserDetails user = mongoTemplate.findOne(query, UserDetails.class, C_USER);
         * if (user == null || user.getUadecks() == null) {
         * return List.of();
         * }
         * return user.getUadecks();
         * }
         */

        public String createOnePieceDecklist(GenericDecklist decklist, String userId) {
                Query query = new Query(Criteria.where(F_USERID).is(userId));
                String uuid = UUID.randomUUID().toString();
                decklist.setDeckuid(uuid);
                Update update = new Update().push(F_OPDECKS, decklist);
                mongoTemplate.updateFirst(query, update, UserDetails.class, C_USER);
                return uuid;
        }

        public String updateOnePieceDecklist(GenericDecklist decklist, String userId, String deckuid) {
                Query query = new Query(
                                Criteria.where(F_USERID).is(userId).and(F_OPDECKS)
                                                .elemMatch(Criteria.where(F_DECKUID).is(deckuid)));
                decklist.setDeckuid(deckuid);
                Update update = new Update().set(F_OPDECKS + ".$", decklist);
                mongoTemplate.updateFirst(query, update, UserDetails.class, C_USER);
                return deckuid;
        }

        public OnePieceDecklist loadOnePieceDecklist(String userId, String deckUid) {
                String fieldCat = F_OPDECKS;
                String collectionCat = C_ONEPIECE;
                Aggregation aggregation = Aggregation.newAggregation(
                                Aggregation.match(Criteria.where(F_USERID).is(userId)),
                                Aggregation.unwind(fieldCat),
                                // Add match condition for specific deckuid
                                Aggregation.match(Criteria.where(fieldCat + ".deckuid").is(deckUid)),
                                Aggregation.unwind(fieldCat + ".listofcards", true),

                                // Store the count and card ID
                                Aggregation.project()
                                                .and(fieldCat).as(fieldCat)
                                                .and(fieldCat + ".listofcards.count").as("tempCount")
                                                .and(fieldCat + ".listofcards._id").as("tempCardId"),

                                Aggregation.lookup(
                                                collectionCat,
                                                "tempCardId",
                                                "_id",
                                                "cardDetails"),

                                Aggregation.unwind("cardDetails", true),

                                // Merge the count field into the cardDetails document using ObjectOperators
                                Aggregation.project()
                                                .and(ObjectOperators.MergeObjects.merge(
                                                                "$cardDetails",
                                                                new Document("count", "$tempCount")))
                                                .as("mergedCard")
                                                .and(fieldCat).as(fieldCat),

                                // Group by deck fields and collect the merged cards
                                Aggregation.group(
                                                Fields.from(
                                                                Fields.field("deckuid", "$" + fieldCat + ".deckuid"),
                                                                Fields.field("deckName", "$" + fieldCat + ".deckName"),
                                                                Fields.field("image", "$" + fieldCat + ".image")))
                                                .push("$mergedCard")
                                                .as("listofcards"),

                                // Project final structure
                                Aggregation.project()
                                                .and("_id.deckuid").as("deckuid")
                                                .and("_id.deckName").as("deckName")
                                                .and("_id.image").as("image")
                                                .and("listofcards").as("listofcards"));

                // Use getUniqueMappedResult() to get a single result
                return mongoTemplate.aggregate(aggregation, C_USER, OnePieceDecklist.class)
                                .getUniqueMappedResult();
        }

        /*
         * V1 load
         * public List<OnePieceDecklist> loadOnePieceDecklist(String userId) {
         * Query query = new Query();
         * query.addCriteria(Criteria.where(F_USERID).is(userId));
         * query.fields().include(F_OPDECKS);
         * UserDetails user = mongoTemplate.findOne(query, UserDetails.class, C_USER);
         * if (user == null || user.getOpdecks() == null) {
         * return List.of();
         * }
         * return user.getOpdecks();
         * }
         **/

        public String createCookieRunDecklist(GenericDecklist decklist, String userId) {
                Query query = new Query(Criteria.where(F_USERID).is(userId));
                String uuid = UUID.randomUUID().toString();
                decklist.setDeckuid(uuid);
                Update update = new Update().push(F_CRBDECKS, decklist);
                mongoTemplate.updateFirst(query, update, UserDetails.class, C_USER);
                return uuid;
        }

        public String updateCookieRunDecklist(GenericDecklist decklist, String userId, String deckuid) {
                Query query = new Query(
                                Criteria.where(F_USERID).is(userId).and(F_CRBDECKS)
                                                .elemMatch(Criteria.where(F_DECKUID).is(deckuid)));
                decklist.setDeckuid(deckuid);
                Update update = new Update().set(F_CRBDECKS + ".$", decklist);
                mongoTemplate.updateFirst(query, update, UserDetails.class, C_USER);
                return deckuid;
        }

        public CookieRunDecklist loadCookieRunDecklist(String userId, String deckUid) {
                String fieldCat = F_CRBDECKS;
                String collectionCat = C_COOKIERUN;
                Aggregation aggregation = Aggregation.newAggregation(
                                Aggregation.match(Criteria.where(F_USERID).is(userId)),
                                Aggregation.unwind(fieldCat),
                                // Add match condition for specific deckuid
                                Aggregation.match(Criteria.where(fieldCat + ".deckuid").is(deckUid)),
                                Aggregation.unwind(fieldCat + ".listofcards", true),

                                // Store the count and card ID
                                Aggregation.project()
                                                .and(fieldCat).as(fieldCat)
                                                .and(fieldCat + ".listofcards.count").as("tempCount")
                                                .and(fieldCat + ".listofcards._id").as("tempCardId"),

                                Aggregation.lookup(
                                                collectionCat,
                                                "tempCardId",
                                                "_id",
                                                "cardDetails"),

                                Aggregation.unwind("cardDetails", true),

                                // Merge the count field into the cardDetails document using ObjectOperators
                                Aggregation.project()
                                                .and(ObjectOperators.MergeObjects.merge(
                                                                "$cardDetails",
                                                                new Document("count", "$tempCount")))
                                                .as("mergedCard")
                                                .and(fieldCat).as(fieldCat),

                                // Group by deck fields and collect the merged cards
                                Aggregation.group(
                                                Fields.from(
                                                                Fields.field("deckuid", "$" + fieldCat + ".deckuid"),
                                                                Fields.field("deckName", "$" + fieldCat + ".deckName"),
                                                                Fields.field("image", "$" + fieldCat + ".image")))
                                                .push("$mergedCard")
                                                .as("listofcards"),

                                // Project final structure
                                Aggregation.project()
                                                .and("_id.deckuid").as("deckuid")
                                                .and("_id.deckName").as("deckName")
                                                .and("_id.image").as("image")
                                                .and("listofcards").as("listofcards"));

                // Use getUniqueMappedResult() to get a single result
                return mongoTemplate.aggregate(aggregation, C_USER, CookieRunDecklist.class)
                                .getUniqueMappedResult();
        }

        /*
         * V1 load
         * public List<CookieRunDecklist> loadCookieRunDecklist(String userId) {
         * Query query = new Query();
         * query.addCriteria(Criteria.where(F_USERID).is(userId));
         * query.fields().include(F_CRBDECKS);
         * UserDetails user = mongoTemplate.findOne(query, UserDetails.class, C_USER);
         * if (user == null || user.getCrbdecks() == null) {
         * return List.of();
         * }
         * return user.getCrbdecks();
         * }
         */

        public String createDragonballzFWDecklist(GenericDecklist decklist, String userId) {
                Query query = new Query(Criteria.where(F_USERID).is(userId));
                String uuid = UUID.randomUUID().toString();
                decklist.setDeckuid(uuid);
                Update update = new Update().push(F_DBZFWDECKS, decklist);
                mongoTemplate.updateFirst(query, update, UserDetails.class, C_USER);
                return uuid;
        }

        public String updateDragonballzFWDecklist(GenericDecklist decklist, String userId, String deckuid) {
                Query query = new Query(
                                Criteria.where(F_USERID).is(userId).and(F_DBZFWDECKS)
                                                .elemMatch(Criteria.where(F_DECKUID).is(deckuid)));
                decklist.setDeckuid(deckuid);
                Update update = new Update().set(F_DBZFWDECKS + ".$", decklist);
                mongoTemplate.updateFirst(query, update, UserDetails.class, C_USER);
                return deckuid;
        }

        public DragonballzFWDecklist loadDragonballzFWDecklist(String userId, String deckUid) {
                String fieldCat = F_DBZFWDECKS;
                String collectionCat = C_DRAGONBALLZFW;
                Aggregation aggregation = Aggregation.newAggregation(
                                Aggregation.match(Criteria.where(F_USERID).is(userId)),
                                Aggregation.unwind(fieldCat),
                                // Add match condition for specific deckuid
                                Aggregation.match(Criteria.where(fieldCat + ".deckuid").is(deckUid)),
                                Aggregation.unwind(fieldCat + ".listofcards", true),

                                // Store the count and card ID
                                Aggregation.project()
                                                .and(fieldCat).as(fieldCat)
                                                .and(fieldCat + ".listofcards.count").as("tempCount")
                                                .and(fieldCat + ".listofcards._id").as("tempCardId"),

                                Aggregation.lookup(
                                                collectionCat,
                                                "tempCardId",
                                                "_id",
                                                "cardDetails"),

                                Aggregation.unwind("cardDetails", true),

                                // Merge the count field into the cardDetails document using ObjectOperators
                                Aggregation.project()
                                                .and(ObjectOperators.MergeObjects.merge(
                                                                "$cardDetails",
                                                                new Document("count", "$tempCount")))
                                                .as("mergedCard")
                                                .and(fieldCat).as(fieldCat),

                                // Group by deck fields and collect the merged cards
                                Aggregation.group(
                                                Fields.from(
                                                                Fields.field("deckuid", "$" + fieldCat + ".deckuid"),
                                                                Fields.field("deckName", "$" + fieldCat + ".deckName"),
                                                                Fields.field("image", "$" + fieldCat + ".image")))
                                                .push("$mergedCard")
                                                .as("listofcards"),

                                // Project final structure
                                Aggregation.project()
                                                .and("_id.deckuid").as("deckuid")
                                                .and("_id.deckName").as("deckName")
                                                .and("_id.image").as("image")
                                                .and("listofcards").as("listofcards"));

                // Use getUniqueMappedResult() to get a single result
                return mongoTemplate.aggregate(aggregation, C_USER, DragonballzFWDecklist.class)
                                .getUniqueMappedResult();
        }

        /*
         * public List<DragonballzFWDecklist> loadDragonballzFWDecklist(String userId) {
         * Query query = new Query();
         * query.addCriteria(Criteria.where(F_USERID).is(userId));
         * query.fields().include(F_DBZFWDECKS);
         * UserDetails user = mongoTemplate.findOne(query, UserDetails.class, C_USER);
         * if (user == null || user.getDbzfwdecks() == null) {
         * return List.of();
         * }
         * return user.getDbzfwdecks();
         * }
         */

        public String createDuelMasterDecklist(GenericDecklist decklist, String userId) {
                Query query = new Query(Criteria.where(F_USERID).is(userId));
                String uuid = UUID.randomUUID().toString();
                decklist.setDeckuid(uuid);
                Update update = new Update().push(F_DMDECKS, decklist);
                mongoTemplate.updateFirst(query, update, UserDetails.class, C_USER);
                return uuid;
        }

        public String updateDuelMasterDecklist(GenericDecklist decklist, String userId, String deckuid) {
                Query query = new Query(
                                Criteria.where(F_USERID).is(userId).and(F_DMDECKS)
                                                .elemMatch(Criteria.where(F_DECKUID).is(deckuid)));
                decklist.setDeckuid(deckuid);
                Update update = new Update().set(F_DMDECKS + ".$", decklist);
                mongoTemplate.updateFirst(query, update, UserDetails.class, C_USER);
                return deckuid;
        }

        public DuelMasterDecklist loadDuelMasterDecklist(String userId, String deckUid) {
                String fieldCat = F_DMDECKS;
                String collectionCat = C_DUELMASTER;
                Aggregation aggregation = Aggregation.newAggregation(
                                Aggregation.match(Criteria.where(F_USERID).is(userId)),
                                Aggregation.unwind(fieldCat),
                                // Add match condition for specific deckuid
                                Aggregation.match(Criteria.where(fieldCat + ".deckuid").is(deckUid)),
                                Aggregation.unwind(fieldCat + ".listofcards", true),

                                // Store the count and card ID
                                Aggregation.project()
                                                .and(fieldCat).as(fieldCat)
                                                .and(fieldCat + ".listofcards.count").as("tempCount")
                                                .and(fieldCat + ".listofcards._id").as("tempCardId"),

                                Aggregation.lookup(
                                                collectionCat,
                                                "tempCardId",
                                                "_id",
                                                "cardDetails"),

                                Aggregation.unwind("cardDetails", true),

                                // Merge the count field into the cardDetails document using ObjectOperators
                                Aggregation.project()
                                                .and(ObjectOperators.MergeObjects.merge(
                                                                "$cardDetails",
                                                                new Document("count", "$tempCount")))
                                                .as("mergedCard")
                                                .and(fieldCat).as(fieldCat),

                                // Group by deck fields and collect the merged cards
                                Aggregation.group(
                                                Fields.from(
                                                                Fields.field("deckuid", "$" + fieldCat + ".deckuid"),
                                                                Fields.field("deckName", "$" + fieldCat + ".deckName"),
                                                                Fields.field("image", "$" + fieldCat + ".image")))
                                                .push("$mergedCard")
                                                .as("listofcards"),

                                // Project final structure
                                Aggregation.project()
                                                .and("_id.deckuid").as("deckuid")
                                                .and("_id.deckName").as("deckName")
                                                .and("_id.image").as("image")
                                                .and("listofcards").as("listofcards"));

                // Use getUniqueMappedResult() to get a single result
                return mongoTemplate.aggregate(aggregation, C_USER, DuelMasterDecklist.class)
                                .getUniqueMappedResult();
        }

        /*
         * public List<DuelMasterDecklist> loadDuelMasterDecklist(String userId) {
         * Query query = new Query();
         * query.addCriteria(Criteria.where(F_USERID).is(userId));
         * query.fields().include(F_DMDECKS);
         * UserDetails user = mongoTemplate.findOne(query, UserDetails.class, C_USER);
         * if (user == null || user.getDmdecks() == null) {
         * return List.of();
         * }
         * return user.getDmdecks();
         * }
         */

        public String createGundamDecklist(GenericDecklist decklist, String userId) {
                Query query = new Query(Criteria.where(F_USERID).is(userId));
                String uuid = UUID.randomUUID().toString();
                decklist.setDeckuid(uuid);
                Update update = new Update().push(F_GCGDECKS, decklist);
                mongoTemplate.updateFirst(query, update, UserDetails.class, C_USER);
                return uuid;
        }

        public String updateGundamDecklist(GenericDecklist decklist, String userId, String deckuid) {
                Query query = new Query(
                                Criteria.where(F_USERID).is(userId).and(F_GCGDECKS)
                                                .elemMatch(Criteria.where(F_DECKUID).is(deckuid)));
                decklist.setDeckuid(deckuid);
                Update update = new Update().set(F_GCGDECKS + ".$", decklist);
                mongoTemplate.updateFirst(query, update, UserDetails.class, C_USER);
                return deckuid;
        }

        public GundamDecklist loadGundamDecklist(String userId, String deckUid) {
                String fieldCat = F_GCGDECKS;
                String collectionCat = C_GUNDAM;
                Aggregation aggregation = Aggregation.newAggregation(
                                Aggregation.match(Criteria.where(F_USERID).is(userId)),
                                Aggregation.unwind(fieldCat),
                                // Add match condition for specific deckuid
                                Aggregation.match(Criteria.where(fieldCat + ".deckuid").is(deckUid)),
                                Aggregation.unwind(fieldCat + ".listofcards", true),

                                // Store the count and card ID
                                Aggregation.project()
                                                .and(fieldCat).as(fieldCat)
                                                .and(fieldCat + ".listofcards.count").as("tempCount")
                                                .and(fieldCat + ".listofcards._id").as("tempCardId"),

                                Aggregation.lookup(
                                                collectionCat,
                                                "tempCardId",
                                                "_id",
                                                "cardDetails"),

                                Aggregation.unwind("cardDetails", true),

                                // Merge the count field into the cardDetails document using ObjectOperators
                                Aggregation.project()
                                                .and(ObjectOperators.MergeObjects.merge(
                                                                "$cardDetails",
                                                                new Document("count", "$tempCount")))
                                                .as("mergedCard")
                                                .and(fieldCat).as(fieldCat),

                                // Group by deck fields and collect the merged cards
                                Aggregation.group(
                                                Fields.from(
                                                                Fields.field("deckuid", "$" + fieldCat + ".deckuid"),
                                                                Fields.field("deckName", "$" + fieldCat + ".deckName"),
                                                                Fields.field("image", "$" + fieldCat + ".image")))
                                                .push("$mergedCard")
                                                .as("listofcards"),

                                // Project final structure
                                Aggregation.project()
                                                .and("_id.deckuid").as("deckuid")
                                                .and("_id.deckName").as("deckName")
                                                .and("_id.image").as("image")
                                                .and("listofcards").as("listofcards"));

                // Use getUniqueMappedResult() to get a single result
                return mongoTemplate.aggregate(aggregation, C_USER, GundamDecklist.class)
                                .getUniqueMappedResult();
        }

        /*
         * public List<GundamDecklist> loadGundamDecklist(String userId) {
         * Query query = new Query();
         * query.addCriteria(Criteria.where(F_USERID).is(userId));
         * query.fields().include(F_GCGDECKS);
         * UserDetails user = mongoTemplate.findOne(query, UserDetails.class, C_USER);
         * if (user == null || user.getGcgdecks() == null) {
         * return List.of();
         * }
         * return user.getGcgdecks();
         * }
         */

        public String createWSBDecklist(GenericDecklist decklist, String userId) {
                Query query = new Query(Criteria.where(F_USERID).is(userId));
                String uuid = UUID.randomUUID().toString();
                decklist.setDeckuid(uuid);
                Update update = new Update().push(F_WSBDECKS, decklist);
                mongoTemplate.updateFirst(query, update, UserDetails.class, C_USER);
                return uuid;
        }

        public String updateWSBDecklist(GenericDecklist decklist, String userId, String deckuid) {
                Query query = new Query(
                                Criteria.where(F_USERID).is(userId).and(F_WSBDECKS)
                                                .elemMatch(Criteria.where(F_DECKUID).is(deckuid)));
                decklist.setDeckuid(deckuid);
                Update update = new Update().set(F_WSBDECKS + ".$", decklist);
                mongoTemplate.updateFirst(query, update, UserDetails.class, C_USER);
                return deckuid;
        }

        public WeibSchwarzBlauDecklist loadWSBDecklist(String userId, String deckUid) {
                String fieldCat = F_WSBDECKS;
                String collectionCat = C_WSBLAU;
                Aggregation aggregation = Aggregation.newAggregation(
                                Aggregation.match(Criteria.where(F_USERID).is(userId)),
                                Aggregation.unwind(fieldCat),
                                // Add match condition for specific deckuid
                                Aggregation.match(Criteria.where(fieldCat + ".deckuid").is(deckUid)),
                                Aggregation.unwind(fieldCat + ".listofcards", true),

                                // Store the count and card ID
                                Aggregation.project()
                                                .and(fieldCat).as(fieldCat)
                                                .and(fieldCat + ".listofcards.count").as("tempCount")
                                                .and(fieldCat + ".listofcards._id").as("tempCardId"),

                                Aggregation.lookup(
                                                collectionCat,
                                                "tempCardId",
                                                "_id",
                                                "cardDetails"),

                                Aggregation.unwind("cardDetails", true),

                                // Merge the count field into the cardDetails document using ObjectOperators
                                Aggregation.project()
                                                .and(ObjectOperators.MergeObjects.merge(
                                                                "$cardDetails",
                                                                new Document("count", "$tempCount")))
                                                .as("mergedCard")
                                                .and(fieldCat).as(fieldCat),

                                // Group by deck fields and collect the merged cards
                                Aggregation.group(
                                                Fields.from(
                                                                Fields.field("deckuid", "$" + fieldCat + ".deckuid"),
                                                                Fields.field("deckName", "$" + fieldCat + ".deckName"),
                                                                Fields.field("image", "$" + fieldCat + ".image")))
                                                .push("$mergedCard")
                                                .as("listofcards"),

                                // Project final structure for deck (image field refers to deckcover)
                                Aggregation.project()
                                                .and("_id.deckuid").as("deckuid")
                                                .and("_id.deckName").as("deckName")
                                                .and("_id.image").as("image")
                                                .and("listofcards").as("listofcards"));

                // Use getUniqueMappedResult() to get a single result
                return mongoTemplate.aggregate(aggregation, C_USER, WeibSchwarzBlauDecklist.class)
                                .getUniqueMappedResult();
        }

        public void deleteDecklist(String tcg, String userId, String deckId) {

                System.out.println("Received tcg: " + tcg);

                String field = "";
                if (T_UA.trim().equals(tcg)) {
                        field = F_UADECKS;
                } else if (T_OP.equals(tcg)) {
                        field = F_OPDECKS;
                } else if (T_GCG.equals(tcg)) {
                        field = F_GCGDECKS;
                } else if (T_DM.equals(tcg)) {
                        field = F_DMDECKS;
                } else if (T_CRB.equals(tcg)) {
                        field = F_CRBDECKS;
                } else if (T_DBZ.equals(tcg)) {
                        field = F_DBZFWDECKS;
                } else if (T_WSB.equals(tcg)) {
                        field = F_WSBDECKS;
                }

                if (field.isEmpty()) {
                        throw new IllegalArgumentException("Invalid tcg value, field cannot be empty");
                }

                Query query = new Query(Criteria.where(F_USERID).is(userId));
                Update update = new Update().pull(field, Query.query(Criteria.where(F_DECKUID).is(deckId)));
                mongoTemplate.updateFirst(query, update, UserDetails.class, C_USER);
        }
}
