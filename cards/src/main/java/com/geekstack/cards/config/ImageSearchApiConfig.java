package com.geekstack.cards.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ImageSearchApiConfig {
    
    @Bean
    public RestTemplate imageSearchRestTemplate() {
        return new RestTemplate();
    }
}
