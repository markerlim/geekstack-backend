package com.geekstack.cards.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geekstack.cards.model.Notification;
import com.geekstack.cards.repository.NotificationRepository;
import com.geekstack.cards.repository.UserDetailsMySQLRepository;
import com.geekstack.cards.repository.UserPostMongoRepository;
import com.geekstack.cards.repository.UserPostMySQLRepository;
import com.google.firebase.messaging.FirebaseMessaging;

@Service
public class RabbitMQConsumer {

    private final static Logger logger = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @Autowired
    private UserPostMongoRepository userPostMongoRepository;

    @Autowired
    private UserPostMySQLRepository userPostMySQLRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserDetailsMySQLRepository userDetailsMySQLRepository;

    @Autowired
    private FirebaseCloudMessagingService firebaseCloudMessagingService;

    @Autowired
    private ObjectMapper objectMapper;

    private final List<String> likeBuffer = new ArrayList<>();
    private final List<String> notificationBuffer = new ArrayList<>();

    @RabbitListener(queues = "likeQueue")
    @RabbitHandler
    public void receiveLike(String message) {
        synchronized (likeBuffer) {
            likeBuffer.add(message);
        }
    }

    @RabbitListener(queues = "notificationsQueue")
    @RabbitHandler
    public void receiveNotification(String notification) {
        System.out.println("RECEIVED: " + notification);
        synchronized (notificationBuffer) {
            notificationBuffer.add(notification);
        }

    }

    @Scheduled(fixedRate = 5000)
    public void processLikes() {
        if (!likeBuffer.isEmpty()) {
            System.out.println("Processing " + likeBuffer.size() + " likes...");
            bulkInsertIntoDatabase(likeBuffer);
            likeBuffer.clear();
        }
    }

    @Scheduled(fixedRate = 60000)
    public void processNotifications() {
        if (!notificationBuffer.isEmpty()) {
            System.out.println("Processing " + notificationBuffer.size() + " notifications...");
            bulkInsertNotificationsIntoDatabase(notificationBuffer);
            notificationBuffer.clear();
        }
    }

    private void bulkInsertIntoDatabase(List<String> likes) {

        Map<String, List<String>> holder = new HashMap<>();

        for (String entry : likes) {
            logger.info(entry);
            String[] array = entry.replace("\"", "").split(":");
            String userId = array[1];
            holder.putIfAbsent(userId, new ArrayList<>());
            holder.get(userId).add(array[0]);
        }

        for (String userId : holder.keySet()) {
            userPostMongoRepository.likeMultiplePosts(holder.get(userId), userId);
            userPostMySQLRepository.likeMultiplePosts(holder.get(userId),userId);
        }

        System.out.println("Batch writing " + likes.size() + " likes to database.");
    }

    private void bulkInsertNotificationsIntoDatabase(List<String> notifications) {
        List<Notification> notificationList = new ArrayList<>();
        System.out.println("starting to add notification:");

        for (String entry : notifications) {
            try {
                Notification notification = objectMapper.readValue(entry, Notification.class);
                notificationList.add(notification);
            } catch (Exception e) {
                System.out.println("Error deserializing notification: " + e.getMessage());
            }
        }

        List<String> userIds = notificationList.stream()
                .map(Notification::getUserId)
                .distinct()
                .collect(Collectors.toList());

        List<Map<String, Object>> tokens = userDetailsMySQLRepository.batchGetTokens(userIds);

        if (!notificationList.isEmpty()) {
            notificationRepository.batchWriteNotification(notificationList);
            System.out.println("Batch writing " + notificationList.size() + " notifications to database.");
        }

        for (Notification notification : notificationList) {
            String userId = notification.getUserId();
            List<String> userTokens = tokens.stream()
            .filter(map -> userId.equals(map.get("user_id")))
            .map(map -> (String) map.get("token"))
            .collect(Collectors.toList());

            if (!userTokens.isEmpty()) {
                System.out.println("Sending notification to " + userTokens.size() + " devices for userId: " + userId);
                
                for (String token : userTokens) {
                    firebaseCloudMessagingService.sendFcmNotification(
                        token,
                        notification.getPostId(),
                        userId,
                        notification.getSenderName(),
                        notification.getMessage()
                    );
                }
             } else {
                System.out.println("No FCM token found for userId: " + userId);
            }
        }
    }

}
