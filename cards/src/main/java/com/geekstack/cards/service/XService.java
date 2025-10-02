package com.geekstack.cards.service;

import org.springframework.stereotype.Service;

import com.google.api.client.util.Value;

@Service
public class XService {
    
    @Value("${x.api.key}")
    private String xApiKey;

    @Value("${x.api.url}")
    private String xApiUrl;

    @Value("${x.api.version}")
    private String xApiVersion;

    public String baseXAPI() {
        String url = xApiUrl + xApiVersion;
        return url;
    }

    public void getTop10RecentPosts() {
        String url = baseXAPI() + "/users/:id/tweets";
        System.out.println("X API URL: " + url);
        // Implement the logic to call the X API and fetch the top 10 recent posts
    }
}
