package com.geekstack.cards.service;

import java.io.StringReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;

    @Value("${reporting.email}")
    private String emailAddress;

    @Async
    public void sendReportEmail(String payload) {

        try {
            JsonReader reader = Json.createReader(new StringReader(payload));
            JsonObject jobject = reader.readObject();
            String cardUid = jobject.getString("cardUid");
            String userId = jobject.getString("userId");
            String errorMsg = jobject.getString("errorMsg");
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(emailAddress);
            helper.setSubject("Error report for: " + cardUid);
            helper.setText(buildEmailBody(userId, cardUid, errorMsg), true); // true to enable HTML
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String buildEmailBody(String userId, String cardUid, String errorMsg) {
        return "<html>" +
                "<body>" +
                "<h2>Error Report</h2>" +
                "<p><strong>User ID:</strong> " + userId + "</p>" +
                "<p><strong>Card ID:</strong> " + cardUid + "</p>" +
                "<p>An error was reported for the above card. Please investigate.</p>" +
                "<p>Error Msg:</p>" + errorMsg +
                "<p>Timestamp: " + java.time.LocalDateTime.now() + "</p>" +
                "</body>" +
                "</html>";
    }
}