package com.justeattakeaway.codechallenge.messaging;

import java.util.List;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.justeattakeaway.codechallenge.config.KafkaConfig;
import com.justeattakeaway.codechallenge.services.GameSession;
import com.justeattakeaway.codechallenge.services.Player;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class GameEventConsumer {
    private final GameSession gameSession;
    private final List<Player> players;

    @KafkaListener(topics = KafkaConfig.GAME_COMMANDS_TOPIC)
    public void handleMakeMoveCommand(MakeMoveCommand command) {
        log.info("Received make move command: {}", command);
        // Find the target player
        players.stream()
            .filter(p -> p.getPlayerId() == command.playerId())
            .findAny()
            .ifPresent(player -> player.makeMove(command.gameId(), command.currentValue()));
    }

    @KafkaListener(topics = KafkaConfig.GAME_EVENTS_TOPIC)
    public void handleMoveMadeEvent(MoveMadeEvent event) {
        log.info("Received move made event: {}", event);
        gameSession.handleMoveMadeEvent(event.toGameEvent());
    }
}