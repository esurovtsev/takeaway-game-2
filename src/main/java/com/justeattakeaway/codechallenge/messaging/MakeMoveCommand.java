package com.justeattakeaway.codechallenge.messaging;

// command to make a move
public record MakeMoveCommand(
    String gameId,
    int playerId,
    int currentValue
) {
}
