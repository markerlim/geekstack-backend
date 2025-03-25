package com.geekstack.cards.repository;

import static com.geekstack.cards.utils.Constants.C_NOTIFICATION;
import static com.geekstack.cards.utils.Constants.F_USERID_REAL;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.geekstack.cards.model.Notification;

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

    public void batchWriteNotification(List<Notification> notifications) {
        if (notifications == null || notifications.isEmpty()) {
            return;
        }
        mongoTemplate.insert(notifications, Notification.class);
    }
}
