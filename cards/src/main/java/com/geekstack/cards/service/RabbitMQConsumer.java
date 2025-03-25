package com.geekstack.cards.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geekstack.cards.model.Notification;
import com.geekstack.cards.repository.NotificationRepository;
import com.geekstack.cards.repository.UserPostMongoRepository;

@Service
public class RabbitMQConsumer {

    @Autowired
    private UserPostMongoRepository userPostMongoRepository;

    @Autowired
    private NotificationRepository notificationRepository;

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
            String[] array = entry.split(":");
            String userId = array[1];
            List<String> list = holder.computeIfAbsent(userId, k -> new ArrayList<>());
            list.add(array[0]);
            holder.put(userId, list);
        }

        for (String userId : holder.keySet()) {
            userPostMongoRepository.likeMultiplePosts(holder.get(userId), userId);
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

        if (!notificationList.isEmpty()) {
            notificationRepository.batchWriteNotification(notificationList);
            System.out.println("Batch writing " + notificationList.size() + " notifications to database.");
        }
    }
}
