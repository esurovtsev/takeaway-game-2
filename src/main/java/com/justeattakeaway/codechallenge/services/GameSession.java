package com.justeattakeaway.codechallenge.services;

import com.justeattakeaway.codechallenge.domain.GameEvent;
import com.justeattakeaway.codechallenge.domain.GameResult;
import com.justeattakeaway.codechallenge.domain.GameRules;
import com.justeattakeaway.codechallenge.domain.GameStatus;
import com.justeattakeaway.codechallenge.messaging.GameEventProducer;
import com.justeattakeaway.codechallenge.messaging.MakeMoveCommand;
import com.justeattakeaway.codechallenge.repository.MongoEventStore;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/***
 * An agregate root that maintains consistency and coordinates the game flow
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GameSession {
    private final MongoEventStore eventStore;
    private final GameEventProducer producer;

    /**
     * Represents an active game with its players
     */
    private record ActiveGame(
            CompletableFuture<GameResult> result,
            List<Integer> players // just the list of players in the game
    ) {
        public int getOtherPlayer(int currentPlayer) {
            return players.get(0) == currentPlayer ? players.get(1) : players.get(0);
        }
    }

    // Map to store pending games and their completion futures
    private final Map<String, ActiveGame> pendingGames = Collections.synchronizedMap(new HashMap<>());

    public GameResult playNewGame(int player1Id, int player2Id, int initialValue) {
        String gameId = UUID.randomUUID().toString();

        // Create future for this game
        CompletableFuture<GameResult> resultFuture = new CompletableFuture<>();
        pendingGames.put(gameId, new ActiveGame(resultFuture, List.of(player1Id, player2Id)));

        try {
            // based on the task it seems as first player does not do a real move - the initial value is considered as a first move
            eventStore.save(new GameEvent(
                LocalDateTime.now(),
                gameId,
                0,
                0,
                initialValue,
                true,
                player1Id
            ));

            // Ask second player to move
            producer.sendMakeMoveCommand(new MakeMoveCommand(gameId, player2Id, initialValue));

            // Wait for game completion with timeout
            return waitForGameCompletion(gameId);

        } catch (Exception e) {
            log.error("Error during game execution", e);
            return new GameResult(GameStatus.ABANDONED, null, gameId);
        } finally {
            pendingGames.remove(gameId);
        }
    }

    private GameResult waitForGameCompletion(@NonNull String gameId) {
        ActiveGame activeGame = pendingGames.get(gameId);
        try {
            return activeGame.result.get(30, TimeUnit.SECONDS);

        } catch (TimeoutException e) {
            log.error("Game {} timed out", gameId);
            return new GameResult(GameStatus.ABANDONED, null, gameId);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Game {} was interrupted", gameId);
            return new GameResult(GameStatus.ABANDONED, null, gameId);

        } catch (ExecutionException e) {
            log.error("Error completing game {}", gameId, e);
            return new GameResult(GameStatus.ABANDONED, null, gameId);
        }
    }

    public void handleMoveMadeEvent(@NonNull GameEvent event) {
        // event sourcing
        eventStore.save(event);

        ActiveGame game = pendingGames.get(event.gameId());
        if (game == null) {
            log.warn("Received move for unknown game {}", event.gameId());
            return;
        }

        if (GameRules.isGameFinished(event.remainingValue())) {
            completeGame(event.gameId(), event.playerId());

        } else {
            // Simply get the other player and ask them to move
            producer.sendMakeMoveCommand(new MakeMoveCommand(
                    event.gameId(),
                    game.getOtherPlayer(event.playerId()),
                    event.remainingValue()));
        }
    }

    private void completeGame(@NonNull String gameId, int winnerId) {
        ActiveGame activeGame = pendingGames.get(gameId);
        if (activeGame != null && activeGame.result != null) {
            activeGame.result.complete(new GameResult(GameStatus.FINISHED, winnerId, gameId));
        }
    }
}