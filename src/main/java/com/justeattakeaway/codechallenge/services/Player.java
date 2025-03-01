package com.justeattakeaway.codechallenge.services;

import java.time.LocalDateTime;

import com.justeattakeaway.codechallenge.domain.GameEvent;
import com.justeattakeaway.codechallenge.domain.MoveStrategy;
import com.justeattakeaway.codechallenge.domain.ValidationException;
import com.justeattakeaway.codechallenge.messaging.GameEventProducer;
import com.justeattakeaway.codechallenge.messaging.MoveMadeEvent;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Player {
    private final MoveStrategy moveStrategy;
    private final GameEventProducer producer;
    private final int playerId;

    public void makeMove(@NonNull String gameId, int currentValue) {
        GameEvent gameEvent = null;
        try {
            gameEvent = moveStrategy.makeMove(currentValue).toSuccessEvent(playerId, gameId, currentValue);

        } catch (ValidationException e) {
            log.error("Error while getting local response", e);
            gameEvent = new GameEvent(
                    LocalDateTime.now(),
                    gameId,
                    currentValue,
                    0,
                    0,
                    false,
                    playerId);
        }

        producer.sendMoveMadeEvent(new MoveMadeEvent(gameEvent));
    }

    public int getPlayerId() {
        return playerId;
    }
}