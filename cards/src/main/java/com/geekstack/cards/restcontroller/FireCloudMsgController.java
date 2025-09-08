package com.geekstack.cards.restcontroller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.geekstack.cards.model.FCMTokenRequest;
import com.geekstack.cards.model.FirebaseUser;
import com.geekstack.cards.service.FirebaseCloudMessagingService;

@RestController
@RequestMapping("/api/fcm")
public class FireCloudMsgController {

    private final static Logger logger = LoggerFactory.getLogger(FireCloudMsgController.class);

    @Autowired
    private FirebaseCloudMessagingService firebaseCloudMessagingService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> updateFCMtoken(@RequestParam String token, @AuthenticationPrincipal FirebaseUser.Principal user) {
        Map<String, Object> response = new HashMap<>();
        String userId = user.getUid();
        
        logger.info(userId);
        logger.info(token);
        try {
            if (token == null || token.isEmpty()) {
                response.put("message", "Token unavailable");
                return ResponseEntity.status(400).body(response);
            }

            firebaseCloudMessagingService.updateFCMToken(userId, token);

            response.put("message", "Notification enabled");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping
    public ResponseEntity<Map<String, Object>> removeFCMToken(@RequestParam(required = false) String token,
            @AuthenticationPrincipal FirebaseUser.Principal user) {
        Map<String, Object> response = new HashMap<>();
        String userId = user.getUid();
        logger.info(userId);
        if (token != null) {
            firebaseCloudMessagingService.deleteFCMToken(token);

        } else if (userId != null) {
            firebaseCloudMessagingService.deleteFCMTokenByUserId(userId);

        } else {
            response.put("message", "Must provide either UserId or Token");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        response.put("message", "Notification disabled");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
