package com.justeattakeaway.codechallenge.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    // for move requests (e.g., "make your move")
    public static final String GAME_COMMANDS_TOPIC = "game-commands";
    // for actual moves made
    public static final String GAME_EVENTS_TOPIC = "game-events";

    @Bean
    public NewTopic gameCommandsTopic() {
        return TopicBuilder.name(GAME_COMMANDS_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic gameEventsTopic() {
        return TopicBuilder.name(GAME_EVENTS_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }
}