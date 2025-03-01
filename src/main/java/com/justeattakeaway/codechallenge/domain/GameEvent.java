package com.justeattakeaway.codechallenge.domain;

import java.time.LocalDateTime;

public record GameEvent(
    LocalDateTime timestamp,
    String gameId,
    int initialValue,
    int moveValue,
    int remainingValue,
    boolean success,
    int playerId
) {
}