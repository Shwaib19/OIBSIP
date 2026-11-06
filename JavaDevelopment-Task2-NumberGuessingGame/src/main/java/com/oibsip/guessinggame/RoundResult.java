package com.oibsip.guessinggame;

/**
 * Immutable record representing the outcome of a single round.
 *
 * @param roundNumber  the sequential round number
 * @param difficulty   the difficulty level chosen for the round
 * @param won          true if the user guessed the secret number
 * @param attemptsUsed number of attempts taken to guess or fail
 * @param targetNumber the secret number generated for this round
 */
public record RoundResult(
        int roundNumber,
        Difficulty difficulty,
        boolean won,
        int attemptsUsed,
        int targetNumber
) {
    /**
     * Formats the round outcome as required by the specifications.
     *
     * @return summary string for this round
     */
    public String getSummary() {
        if (won) {
            return "Round " + roundNumber + " [" + difficulty.getLabel() + "] - guessed in " + attemptsUsed + " attempt(s)";
        } else {
            return "Round " + roundNumber + " [" + difficulty.getLabel() + "] - failed after " + difficulty.getMaxAttempts() + " attempts (number was " + targetNumber + ")";
        }
    }
}
