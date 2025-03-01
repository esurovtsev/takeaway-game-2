package com.justeattakeaway.codechallenge.domain;

public enum GameStatus {
    FINISHED,           // Game completed normally
    INVALID_MOVE,       // Someone made an invalid move
    ABANDONED          // Game abandoned (optional)
}
