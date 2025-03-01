package com.justeattakeaway.codechallenge.domain;

public record GameResult(
        GameStatus status,
        Integer winningPlayer,
        String gameId) {
}