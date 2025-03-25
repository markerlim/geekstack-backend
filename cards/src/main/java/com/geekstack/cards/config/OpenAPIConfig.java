package com.geekstack.cards.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenAPIConfig {
    
    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI().info(
            new Info()
            .title("Geekstack API")
            .description("Testing API using OpenAPI public interface")
            .version("1.0")
        );
    }
}
