package com.justeattakeaway.codechallenge.domain;

import java.time.LocalDateTime;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/***
 * Simple Value Object which keeps information about current move.
 */
@ToString
@EqualsAndHashCode
public class Move {
    private int result;
    private int added;

    public Move(int result, int added) {
        GameRules.resultShouldBeCorrect(result);
        GameRules.addedShouldBeInProperRange(added);
        this.result = result;
        this.added = added;
    }

    public GameEvent toSuccessEvent(int player, String gameId, int initialValue) {
        return new GameEvent(
                LocalDateTime.now(),
                gameId,
                initialValue,
                added,
                result,
                true,
                player);

    }
}
