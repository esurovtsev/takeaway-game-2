package com.justeattakeaway.codechallenge.web;

import com.justeattakeaway.codechallenge.domain.GameResult;
import com.justeattakeaway.codechallenge.services.GameLogService;
import com.justeattakeaway.codechallenge.services.GameSession;
import com.justeattakeaway.codechallenge.services.Player;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequiredArgsConstructor
public class GameController {
    private final GameSession gameSession;
    private final GameLogService gameLogService;
    private final List<Player> players;

    /***
     * "Start a new game" request. This is blocking request which returns final results once the game is finished.
     * The initial number which is used to start a game will be selected randomly from 2 to 1000.
     *
     * Impl. Details: The method accepts a GET request in order to make it easy demonstrable from web browser.
     */
    @GetMapping("/play")
    public GameResult play() {
        return play(ThreadLocalRandom.current().nextInt(2, 1000));
    }

    /***
     * "Start a new game" request. This is blocking request which returns final results once the game is finished.
     * The initial number which is used to start a game should be provided as a parameter.
     *
     * Impl. Details: The method accepts a GET request in order to make it easy demonstrable from web browser.
     */
    @GetMapping("/play/{initialValue}")
    public GameResult play(@PathVariable("initialValue") int initialValue) {
        // in real life we would prefer to have maybe a pool of player and randomly select 2 player from it.
        GameResult result = gameSession.playNewGame(
            players.get(0).getPlayerId(),
            players.get(1).getPlayerId(),
            initialValue);

        // Optionally print history
        gameLogService.printGameLog(result.gameId());

        return result;
    }
}