package com.c208.sleephony.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient fastApiWebClient(
            @Value("${fastapi.base-url}") String baseUrl
    ) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}
