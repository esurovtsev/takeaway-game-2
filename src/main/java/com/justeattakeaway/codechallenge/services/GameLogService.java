package com.justeattakeaway.codechallenge.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.justeattakeaway.codechallenge.domain.GameEvent;
import com.justeattakeaway.codechallenge.repository.EventStore;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameLogService {
    private final EventStore eventStore;
    
    public List<GameEvent> getGameLog(@NonNull String gameId) {
        return eventStore.getAllMoves(gameId);
    }
    
    public void printGameLog(@NonNull String gameId) {
        List<GameEvent> allEvents = getGameLog(gameId);
        
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\n\n===  new game sesion: %s\n", gameId));
        
        for (GameEvent event: allEvents) {
            sb.append(String.format("  Player %d: (%4d + %2d) / 3 = %d\n",
                event.playerId(),
                event.initialValue(),
                event.moveValue(),
                event.remainingValue()
            ));
        }
        sb.append("==========================================================\n");
        
        log.info(sb.toString());
    }
}
