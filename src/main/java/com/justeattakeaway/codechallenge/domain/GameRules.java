package com.justeattakeaway.codechallenge.domain;

import lombok.experimental.UtilityClass;

import java.util.stream.IntStream;

/**
 * The utility class represents Game Specification and is responsible for performing Game's related validation.
 * We extracted this logic into separate class because these rules are being reused between several entities.
 */
@UtilityClass
public class GameRules {
    private static final int MIN_ADDED = -1;
    private static final int MAX_ADDED = 1;

    /***
     * Checks that a new move can be made with given Result and Added values. Throws {@link ValidationException} in case
     * the move cannot be made.
     */
    public void resultShouldBeDividedByThree(int result, int added) {
        GameRules.addedShouldBeInProperRange(added);
        if ((result + added) % 3 != 0) {
            throw new ValidationException("Result of the move should be divided by 3");
        }
    }

    /***
     * A replica for {@link #resultShouldBeDividedByThree(int, int)}. The only difference is this method does not throw
     * any exceptions and return false in case the validation fails. Otherwise the method returns true.
     *
     * Impl. Details: In general from performance perspective it might be not a good idea to check validity by generating
     * exceptions and catching them, but for simplicity of test solution we are using this approach here.
     */
    public boolean isValidMove(int result, int added) {
        try {
            GameRules.resultShouldBeDividedByThree(result, added);
            return true;

        } catch (ValidationException e) {
            return false;
        }
    }

    /***
     * Returns a range of all added values which in general (regardless of the current game result) theoretically could
     * be used for making a new move.
     */
    public IntStream possibleAddedValues() {
        return IntStream.range(MIN_ADDED, MAX_ADDED + 1);
    }

    /***
     * Decides if a Game could be considered as finished having current result. Normally we're saying that game is
     * finished when the result is 3 / 3 = 1
     */
    public boolean isGameFinished(int result) {
        return result == 1;
    }

    /***
     * Checks if provided value can be a correct Result/State of the game. Result could be correct if it is equal to 1
     * (=game finished) or be a number which sum with one of possible added values {@link #possibleAddedValues()} gives
     * another number which is divided by 3. Meaning a number >= 2 (2 + 1 = 3, 3 + 0 = 3, 4 - 1 = 3, etc). So the final
     * correct range is [1..infinite)
     */
    public void resultShouldBeCorrect(int result) {
        if (result < 1) {
            throw new ValidationException("Result should be more 2 or more");
        }
    }

    /***
     * Checks if provided input could be used as a Added value based on rules of the game, meaning if the value is in
     * correct range.
     */
    public void addedShouldBeInProperRange(int added) {
        if (added < MIN_ADDED || added > MAX_ADDED) {
            throw new ValidationException("An added should be one of [-1,0,1]");
        }
    }
}
