package com.geekstack.cards.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class OAuth1Helper {

    public static String generateAuthorizationHeader(
            String method, 
            String url,
            String consumerKey,
            String consumerSecret,
            String accessToken,
            String tokenSecret,
            Map<String, String> params) throws NoSuchAlgorithmException, InvalidKeyException {
        
        // Add OAuth parameters
        Map<String, String> oauthParams = new HashMap<>();
        oauthParams.put("oauth_consumer_key", consumerKey);
        oauthParams.put("oauth_nonce", generateNonce());
        oauthParams.put("oauth_signature_method", "HMAC-SHA1");
        oauthParams.put("oauth_timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        oauthParams.put("oauth_token", accessToken);
        oauthParams.put("oauth_version", "1.0");
        
        // Combine all parameters
        Map<String, String> allParams = new HashMap<>(params);
        allParams.putAll(oauthParams);
        
        // Generate signature
        String signature = generateSignature(method, url, allParams, consumerSecret, tokenSecret);
        oauthParams.put("oauth_signature", signature);
        
        // Build authorization header
        return "OAuth " + oauthParams.entrySet().stream()
            .map(e -> String.format("%s=\"%s\"", 
                encode(e.getKey()), 
                encode(e.getValue())))
            .collect(Collectors.joining(", "));
    }
    
    private static String generateSignature(
            String method, 
            String url,
            Map<String, String> params,
            String consumerSecret,
            String tokenSecret) throws NoSuchAlgorithmException, InvalidKeyException {
        
        String parameterString = params.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .map(e -> encode(e.getKey()) + "=" + encode(e.getValue()))
            .collect(Collectors.joining("&"));
        
        String signatureBaseString = method.toUpperCase() + "&" + 
            encode(url) + "&" + 
            encode(parameterString);
        
        String signingKey = encode(consumerSecret) + "&" + encode(tokenSecret);
        
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(signingKey.getBytes(StandardCharsets.UTF_8), "HmacSHA1"));
        byte[] signatureBytes = mac.doFinal(signatureBaseString.getBytes(StandardCharsets.UTF_8));
        
        return Base64.getEncoder().encodeToString(signatureBytes);
    }
    
    private static String generateNonce() {
        return Long.toHexString(Double.doubleToLongBits(Math.random()));
    }
    
    private static String encode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString())
                .replace("+", "%20")
                .replace("*", "%2A")
                .replace("%7E", "~");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}