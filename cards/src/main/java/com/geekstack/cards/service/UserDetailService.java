package com.geekstack.cards.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.geekstack.cards.model.Notification;
import com.geekstack.cards.repository.NotificationRepository;
import com.geekstack.cards.repository.UserDetailsMongoRepository;
import com.geekstack.cards.repository.UserDetailsMySQLRepository;

@Service
public class UserDetailService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailService.class);

    @Autowired
    private UserDetailsMongoRepository userDetailsMongoRepository;

    @Autowired
    private UserDetailsMySQLRepository userDetailsMySQLRepository;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Transactional
    public int createUser(String userId, String name, String displaypic, String email) {
        try {
            if (userDetailsMySQLRepository.userExists(userId).isEmpty()) {
                userDetailsMySQLRepository.createUser(userId, name, displaypic, email);
                userDetailsMongoRepository.createUser(userId);
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

    public List<Notification> listNotifications(String payload, String limit) throws Exception {
        String userId = firebaseService.verifyIdToken(payload).getUid();
        logger.info(userId);
        List<Notification> list = notificationRepository.getNotification(userId, Integer.parseInt(limit));
        logger.info(list.toString());
        return list;
    }
}
