package com.amanefer.telegram.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

@Configuration
@PropertySource("classpath:application.properties")
@Data
public class BotConfig {
    @Value("${bot.name}")
    String botName;
    @Value("${bot.token}")
    String botToken;

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
