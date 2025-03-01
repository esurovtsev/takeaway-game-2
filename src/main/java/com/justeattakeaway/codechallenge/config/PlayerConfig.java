package com.justeattakeaway.codechallenge.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.justeattakeaway.codechallenge.domain.MoveStrategy;
import com.justeattakeaway.codechallenge.messaging.GameEventProducer;
import com.justeattakeaway.codechallenge.services.Player;

@Configuration
public class PlayerConfig {
    @Bean
    public Player player1(GameEventProducer producer, MoveStrategy moveStrategy) {
        return new Player(moveStrategy, producer, 1);
    }

    @Bean
    public Player player2(GameEventProducer producer, MoveStrategy moveStrategy) {
        return new Player(moveStrategy, producer, 2);
    }

    @Bean
    public List<Player> players(Player player1, Player player2) {
        List<Player> players = List.of(player1, player2);
        validatePlayerPool(players);
        return players;
    }

    private void validatePlayerPool(List<Player> players) {
        if (players.size() < 2) {
            throw new IllegalStateException("At least 2 players required in the pool");
        }
        
        // Check for duplicate player IDs
        long uniquePlayerIds = players.stream()
                .map(Player::getPlayerId)
                .distinct()
                .count();
                
        if (uniquePlayerIds != players.size()) {
            throw new IllegalStateException("All players must have unique IDs");
        }
    }
}