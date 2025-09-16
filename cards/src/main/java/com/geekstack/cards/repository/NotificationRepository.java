package com.geekstack.cards.repository;

import static com.geekstack.cards.utils.Constants.C_NOTIFICATION;
import static com.geekstack.cards.utils.Constants.F_USERID_REAL;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.aggregation.StringOperators.Concat;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.geekstack.cards.model.Notification;
import com.geekstack.cards.model.NotificationMerged;
import com.mongodb.BasicDBObject;

@Repository
public class NotificationRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Notification> getNotification(String userId, int limit) {
        Query query = new Query(new Criteria().orOperator(
                Criteria.where(F_USERID_REAL).is(userId),
                Criteria.where(F_USERID_REAL).is("NOTIFICATION-TO-ALL")));
        return mongoTemplate.find(query.with(Sort.by(Sort.Direction.DESC, "timestamp")).limit(limit),
                Notification.class, C_NOTIFICATION);
    }

    public List<NotificationMerged> getMergedNotifications(String userId, int limit) {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(
                        new Criteria().orOperator(
                                Criteria.where(F_USERID_REAL).is(userId),
                                Criteria.where(F_USERID_REAL).is("NOTIFICATION-TO-ALL"))),
                Aggregation.sort(Sort.Direction.DESC, "timestamp"),
                Aggregation.group("postId", "message")
                        // Push sender object with name + dp
                        .push(new BasicDBObject("name", "$sender").append("dp", "$senderDp"))
                        .as("allSenders")
                        .first("timestamp").as("timestamp")
                        .first("postId").as("postId")
                        .first("message").as("message"),
                // Project only latest 1 sender and calculate others count
                Aggregation.project("postId", "message", "timestamp")
                        .and(ArrayOperators.Slice.sliceArrayOf("allSenders").itemCount(1)).as("latestSender")
                        .and(ArithmeticOperators.Subtract.valueOf(ArrayOperators.Size.lengthOfArray("allSenders"))
                                .subtract(1))
                        .as("othersCount"),
                // Compute "and N others" string if there are more
                Aggregation.project("postId", "message", "timestamp", "latestSender")
                        .and(ConditionalOperators.when(Criteria.where("othersCount").gt(0))
                                .then(Concat.valueOf("and ").concatValueOf("othersCount").concat(" others"))
                                .otherwise(""))
                        .as("others"));

        AggregationResults<NotificationMerged> results = mongoTemplate.aggregate(agg, C_NOTIFICATION,
                NotificationMerged.class);
        return results.getMappedResults();
    }

    public void batchWriteNotification(List<Notification> notifications) {
        if (notifications == null || notifications.isEmpty()) {
            return;
        }
        mongoTemplate.insert(notifications, Notification.class);
    }

    public Integer checkNumOfUnread(String userId, ZonedDateTime lastSeen) {
        Query query = new Query(new Criteria().andOperator(
                new Criteria().orOperator(
                        Criteria.where("userId").is(userId),
                        Criteria.where("userId").is("NOTIFICATION-TO-ALL")),
                Criteria.where("timestamp").gt(lastSeen.toInstant())));
        return (int) mongoTemplate.count(query, Notification.class, C_NOTIFICATION);
    }

}
