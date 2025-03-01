package com.justeattakeaway.codechallenge.domain;

import com.google.common.annotations.VisibleForTesting;
import org.springframework.stereotype.Component;

/***
 * The class represents a Player Entity from the task and is responsible for reacting on new game move events by making
 * new moves back.
 *
 * Impl. Details: Technically, since every server/service represents a single playing side/Player entity, there should be only one
 * instance of type Player per Application, so for simplicity of configuration it's exposed as a Spring bean for all
 * components which need to operate with Player.
 */
@Component
public class MoveStrategy {
    /***
     * Proposes a new move in the Game based on given current Result/State of the game. Contains all Player's related
     * business logic.
     */
    public Move makeMove(int result) {
        GameRules.resultShouldBeCorrect(result);
        int added = calculateAdded(result);
        return applyMove(result, added);
    }

    @VisibleForTesting
    int calculateAdded(int result) {
        return GameRules
                .possibleAddedValues()
                .filter(possibleAdd -> GameRules.isValidMove(result, possibleAdd))
                .findAny()
                .orElseThrow(() -> new ValidationException(
                        String.format("Not possible to make a move for game result " + result)));
    }

    @VisibleForTesting
    Move applyMove(int result, int added) {
        if (GameRules.isGameFinished(result)) {
            throw new ValidationException("Not possible to make a move. The game was finished already.");
        }
        GameRules.resultShouldBeDividedByThree(result, added);

        return new Move((result + added) / 3, added);
    }
}