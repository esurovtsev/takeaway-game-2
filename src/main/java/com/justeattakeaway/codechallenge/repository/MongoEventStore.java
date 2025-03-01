package com.justeattakeaway.codechallenge.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.justeattakeaway.codechallenge.domain.GameEvent;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MongoEventStore implements EventStore {
    private final GameEventRepository repository;
    
    @Override
    public void save(@NonNull GameEvent event) {
        repository.save(GameEventDocument.fromGameEvent(event));
    }

    @Override
    public List<GameEvent> getAllMoves(@NonNull String gameId) {
        return repository.findByGameIdOrderByTimestampAsc(gameId)
                .stream()
                .map(GameEventDocument::getEvent)
                .toList();
    }

    @Override
    public Optional<GameEvent> getLastMove(@NonNull String gameId) {
        return repository.findFirstByGameIdOrderByTimestampDesc(gameId)
                .map(GameEventDocument::getEvent);
    }
}