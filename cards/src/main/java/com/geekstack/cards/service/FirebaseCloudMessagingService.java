package com.geekstack.cards.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.geekstack.cards.repository.UserDetailsMySQLRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.Notification;

@Service
public class FirebaseCloudMessagingService {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseCloudMessagingService.class);

    @Autowired(required = false)
    private FirebaseMessaging firebaseMessaging;

    @Autowired
    private UserDetailsMySQLRepository userDetailsMySQLRepository;

    @Value("${fcm.title}")
    private String notficationTitle;

    @Value("${fcm.icon}")
    private String notificationImageUrl;

    public void sendFcmNotification(String token, String postId, String userId, String sender, String action) {
        String body = String.format("%s has %s",sender,action);

        Notification notificationPayload = Notification.builder()
                .setTitle(notficationTitle)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setNotification(notificationPayload)
                .setToken(token)
                .putData("icon", notificationImageUrl)
                .putData("clickAction", "/stacks/" + postId)
                .build();

        try {
            String response = firebaseMessaging.send(message);
            logger.info("Successfully sent FCM: {}",response);
        } catch (FirebaseMessagingException e) {
            logger.error("Failed to send FCM alert to {} for {}: Code={}, Message={}");

            MessagingErrorCode errorCode = e.getMessagingErrorCode();
            if (errorCode == MessagingErrorCode.UNREGISTERED || errorCode == MessagingErrorCode.INVALID_ARGUMENT) {
                logger.warn("FCM Token for user {} is invalid or unregistered. Removing token from DB.",userId);
                try {
                    userDetailsMySQLRepository.removeFcmToken(userId);
                } catch (Exception removeEx) {
                    logger.error("Failed to remove invalid FCM token for user {}: {}", userId,
                            removeEx.getMessage());
                }
            }
        } catch (Exception e) {
            logger.error("Unexpected error sending FCM alert to {} for post {}: {}", userId, postId,
                    e.getMessage());
        }
    }

    public void updateFCMToken(String userId, String token){
        userDetailsMySQLRepository.updateFcmToken(userId, token);
    }

    public void deleteFCMToken(String token){
        userDetailsMySQLRepository.removeFcmToken(token);
    }

    public void deleteFCMTokenByUserId(String userId){
        userDetailsMySQLRepository.removeFcmTokensByUserId(userId);
    }
}
