package com.geekstack.cards.service;

import java.time.LocalDateTime;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.geekstack.cards.model.Notification;

@Service
public class RabbitMQProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue likesQueue;

    @Autowired
    private Queue unlikesQueue;

    @Autowired
    private Queue notificationsQueue;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendLikeEvent(String postId, String userId) {
        String message = postId + ":" + userId;
        this.rabbitTemplate.convertAndSend(likesQueue.getName(), message);
        System.out.printf("Queued like event: %s%n", message);
    }

    public void sendUnlikeEvent(String postId, String userId) {
        String message = postId + ":" + userId;
        this.rabbitTemplate.convertAndSend(unlikesQueue.getName(), message);
        System.out.printf("Queued unlike event: %s%n", message);
    }

    // userId refers to the user that will be receiving the notification
    public void sendNotificationEvent(String postId, String userId, LocalDateTime time, String message, 
    String senderId, String senderName, String senderDp) {
        this.rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter(objectMapper));
        Notification noti = new Notification();
        noti.setUserId(userId);
        noti.setPostId(postId);
        noti.setIsRead(false);
        noti.setTimestamp(time);
        noti.setMessage(message);
        noti.setSenderId(senderId);
        noti.setSenderName(senderName);
        noti.setSenderDp(senderDp);
        rabbitTemplate.convertAndSend(notificationsQueue.getName(), noti);
        System.out.printf("Queued Notification event: %s%n", noti.toString());
    }
}
