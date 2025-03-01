package com.justeattakeaway.codechallenge.repository;

import java.util.List;
import java.util.Optional;

import com.justeattakeaway.codechallenge.domain.GameEvent;

public interface EventStore {
    void save(GameEvent event);

    List<GameEvent> getAllMoves(String gameId);
    Optional<GameEvent> getLastMove(String gameId);
}