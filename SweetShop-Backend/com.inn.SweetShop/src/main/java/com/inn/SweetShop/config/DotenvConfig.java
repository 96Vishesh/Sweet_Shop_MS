package com.inn.SweetShop.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

public class DotenvConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Dotenv dotenv = Dotenv.configure()
                .directory("./")  // Root directory
                .ignoreIfMissing()
                .load();

        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        Map<String, Object> envMap = new HashMap<>();

        // Load all .env entries into Spring environment
        dotenv.entries().forEach(entry -> {
            envMap.put(entry.getKey(), entry.getValue());
        });

        environment.getPropertySources().addFirst(new MapPropertySource("dotenvProperties", envMap));
    }
}