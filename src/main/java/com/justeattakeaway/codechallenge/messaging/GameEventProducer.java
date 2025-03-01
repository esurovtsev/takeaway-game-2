package com.justeattakeaway.codechallenge.messaging;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.justeattakeaway.codechallenge.config.KafkaConfig;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GameEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMakeMoveCommand(MakeMoveCommand command) {
        kafkaTemplate.send(KafkaConfig.GAME_COMMANDS_TOPIC, command.gameId(), command);
    }

    public void sendMoveMadeEvent(MoveMadeEvent event) {
        kafkaTemplate.send(KafkaConfig.GAME_EVENTS_TOPIC, event.gameId(), event);
    }
}