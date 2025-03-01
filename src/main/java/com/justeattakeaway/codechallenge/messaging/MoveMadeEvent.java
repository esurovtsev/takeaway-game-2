package com.justeattakeaway.codechallenge.messaging;

import java.time.LocalDateTime;

import com.justeattakeaway.codechallenge.domain.GameEvent;

import lombok.NonNull;

public record MoveMadeEvent(
                LocalDateTime timestamp,
                String gameId,
                int playerId,
                int initialValue,
                int moveValue,
                int remainingValue) {

        public GameEvent toGameEvent() {
                return new GameEvent(
                                timestamp,
                                gameId,
                                initialValue,
                                moveValue,
                                remainingValue,
                                true,
                                playerId);
        }

        public MoveMadeEvent(@NonNull GameEvent gameEvent) {
                this(
                                gameEvent.timestamp(),
                                gameEvent.gameId(),
                                gameEvent.playerId(),
                                gameEvent.initialValue(),
                                gameEvent.moveValue(),
                                gameEvent.remainingValue());
        }
}
