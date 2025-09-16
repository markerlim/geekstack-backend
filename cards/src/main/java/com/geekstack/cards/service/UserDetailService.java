package com.geekstack.cards.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geekstack.cards.model.Notification;
import com.geekstack.cards.model.NotificationMerged;
import com.geekstack.cards.repository.NotificationRepository;
import com.geekstack.cards.repository.UserDetailsMongoRepository;
import com.geekstack.cards.repository.UserDetailsMySQLRepository;

@Service
public class UserDetailService {

    @Autowired
    private UserDetailsMongoRepository userDetailsMongoRepository;

    @Autowired
    private UserDetailsMySQLRepository userDetailsMySQLRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Transactional
    public int createUser(String userId, String name, String displaypic, String email) {
        try {
            if (userDetailsMySQLRepository.userExists(userId).isEmpty()) {
                userDetailsMySQLRepository.createUser(userId, name, displaypic, email);// MySql
                userDetailsMongoRepository.createUser(userId);// Mongo
                return 1;
            }
            return 0;

        } catch (Exception e) {

            return 2;

        }
    }

    public Map<String, Object> getOneUser(String userId) {
        Map<String, Object> holder = new HashMap<>();
        holder.put("gsMongoUser", userDetailsMongoRepository.getOneUser(userId));
        holder.put("gsSQLUser", userDetailsMySQLRepository.userExists(userId));
        return holder;
    }

    public boolean updateUserName(String name, String userId) {
        return userDetailsMySQLRepository.updateUserName(name, userId);
    }

    public boolean updateDisplaypic(String displaypic, String userId) {
        return userDetailsMySQLRepository.updateDisplayPic(displaypic, userId);
    }

    public boolean updatePreference(Map<String, Object> preferences, String userId) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String preferencesJson = objectMapper.writeValueAsString(preferences);
            return userDetailsMySQLRepository.updatePreference(preferencesJson, userId);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting preferences to JSON", e);
        }
    }

    public List<Notification> listNotifications(String userId, String limit) throws Exception {
        List<Notification> list = notificationRepository.getNotification(userId, Integer.parseInt(limit));
        String lastSeen = userDetailsMySQLRepository.getLastSeenNotification(userId);
        ZonedDateTime lastSeenZoned = null;

        if (lastSeen != null && !lastSeen.isEmpty()) {
            LocalDateTime lastSeenDateTime = LocalDateTime.parse(lastSeen, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            lastSeenZoned = lastSeenDateTime.atZone(ZoneId.systemDefault());
            for (Notification n : list) {
                if (lastSeenZoned != null) {
                    // If the notification timestamp is after lastSeen, the user has NOT seen it.
                    // Therefore, set isRead to false. Otherwise, set isRead to true.
                    n.setIsRead(!n.getTimestamp().isAfter(lastSeenZoned));
                } else {
                    // If lastSeen is null (user never checked notifications), treat all
                    // notifications as read.
                    n.setIsRead(true);
                }
            }
        }
        userDetailsMySQLRepository.updateLastSeenNotification(userId);
        return list;
    }

    public List<NotificationMerged> listNotificationsMerge(String userId, String limit) throws Exception {
        List<NotificationMerged> list = notificationRepository.getMergedNotifications(userId, Integer.parseInt(limit));
        String lastSeen = userDetailsMySQLRepository.getLastSeenNotification(userId);
        ZonedDateTime lastSeenZoned = null;

        if (lastSeen != null && !lastSeen.isEmpty()) {
            LocalDateTime lastSeenDateTime = LocalDateTime.parse(lastSeen, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            lastSeenZoned = lastSeenDateTime.atZone(ZoneId.systemDefault());
            for (NotificationMerged n : list) {
                if (lastSeenZoned != null) {
                    // If the notification timestamp is after lastSeen, the user has NOT seen it.
                    // Therefore, set isRead to false. Otherwise, set isRead to true.
                    n.setRead(!n.getTimestamp().isAfter(lastSeenZoned));
                } else {
                    // If lastSeen is null (user never checked notifications), treat all
                    // notifications as read.
                    n.setRead(true);
                }
            }
        }
        userDetailsMySQLRepository.updateLastSeenNotification(userId);
        return list;
    }

    public Integer checkNumOfUnread(String userId) {
        String lastSeen = userDetailsMySQLRepository.getLastSeenNotification(userId);
        ZonedDateTime lastSeenZoned = null;

        if (lastSeen != null && !lastSeen.isEmpty()) {
            LocalDateTime lastSeenDateTime = LocalDateTime.parse(lastSeen, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            lastSeenZoned = lastSeenDateTime.atZone(ZoneId.systemDefault());
            return notificationRepository.checkNumOfUnread(userId,lastSeenZoned);
        }
        return 0;
    }
}
