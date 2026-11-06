package com.oibsip.guessinggame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Orchestrates the Number Guessing Game session, input handling, and round management.
 */
public class NumberGuessingGame {

    private final Scanner scanner;
    private final Random random;
    private final List<RoundResult> scoreHistory;

    public NumberGuessingGame() {
        this.scanner = new Scanner(System.in);
        this.random = new Random();
        this.scoreHistory = new ArrayList<>();
    }

    /**
     * Starts the game session and handles the multi-round lifecycle.
     */
    public void start() {
        System.out.println("=========================================");
        System.out.println("   WELCOME TO NUMBER GUESSING GAME !    ");
        System.out.println("=========================================");

        int roundCounter = 0;
        boolean keepPlaying = true;

        // Phase 2: Main session loop ("Play Again")
        while (keepPlaying) {
            roundCounter++;
            System.out.println("\n-----------------------------------------");
            System.out.println("               ROUND " + roundCounter);
            System.out.println("-----------------------------------------");

            // Phase 4: Choose difficulty level
            Difficulty difficulty = selectDifficulty();

            // Phase 1: Play single round
            RoundResult result = playRound(roundCounter, difficulty);
            scoreHistory.add(result);

            // Phase 3: Intermediate round summary
            System.out.println("\n" + result.getSummary());

            // Phase 2: Ask player to play another round
            keepPlaying = askPlayAgain();
        }

        // Phase 3: Final game summary across all rounds
        displayScoreHistory();
        System.out.println("\nThank you for playing! Goodbye!");
        scanner.close();
    }

    /**
     * Phase 4: Prompts the user to select a difficulty level.
     * Defaults to Medium if the user presses Enter.
     */
    private Difficulty selectDifficulty() {
        System.out.println("Select difficulty level:");
        System.out.println("  1. Easy   (Range: 1 to 50,  10 attempts)");
        System.out.println("  2. Medium (Range: 1 to 100,  7 attempts) [Default]");
        System.out.println("  3. Hard   (Range: 1 to 200,  5 attempts)");

        while (true) {
            System.out.print("Your choice (1-3): ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    return Difficulty.EASY;
                case "2":
                case "": // Default when pressing enter
                    return Difficulty.MEDIUM;
                case "3":
                    return Difficulty.HARD;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    /**
     * Phase 1: Handles single-round guessing logic, hints, and attempts limit.
     */
    private RoundResult playRound(int roundNumber, Difficulty difficulty) {
        int targetNumber = random.nextInt(difficulty.getMax() - difficulty.getMin() + 1) + difficulty.getMin();
        int attempts = 0;
        boolean won = false;

        System.out.println("\nA number has been generated between " + difficulty.getMin() + " and " + difficulty.getMax() + ".");
        System.out.println("You have " + difficulty.getMaxAttempts() + " attempts. Good luck!\n");

        while (attempts < difficulty.getMaxAttempts()) {
            attempts++;
            System.out.print("[Attempt " + attempts + "/" + difficulty.getMaxAttempts() + "] Enter your guess: ");

            String input = scanner.nextLine().trim();
            int userGuess;

            // Safe parsing for non-numeric input
            try {
                userGuess = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid whole number.");
                attempts--; // Do not penalize attempt counter for typing mistakes
                continue;
            }

            // Guess validation and hints
            if (userGuess == targetNumber) {
                won = true;
                System.out.println(">> Correct! You guessed the number!");
                break;
            } else if (userGuess > targetNumber) {
                System.out.println(">> Too High!");
            } else {
                System.out.println(">> Too Low!");
            }

            int remaining = difficulty.getMaxAttempts() - attempts;
            if (remaining > 0) {
                System.out.println("Remaining attempt(s): " + remaining + "\n");
            }
        }

        if (!won) {
            System.out.println("\n>> You Lost! The secret number was: " + targetNumber);
        }

        return new RoundResult(roundNumber, difficulty, won, attempts, targetNumber);
    }

    /**
     * Phase 2: Prompts the user to decide whether to play another round.
     */
    private boolean askPlayAgain() {
        while (true) {
            System.out.print("\nDo you want to play again? (yes/no or y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("y") || response.equals("yes") || response.equals("oui") || response.equals("o")) {
                return true;
            } else if (response.equals("n") || response.equals("no") || response.equals("non")) {
                return false;
            } else {
                System.out.println("Please answer with 'yes' or 'no'.");
            }
        }
    }

    /**
     * Phase 3: Displays the final performance summary across all played rounds.
     */
    private void displayScoreHistory() {
        System.out.println("\n=========================================");
        System.out.println("             GAME SUMMARY                ");
        System.out.println("=========================================");

        if (scoreHistory.isEmpty()) {
            System.out.println("No rounds played.");
            return;
        }

        int totalRounds = scoreHistory.size();
        int wonRounds = 0;

        for (RoundResult result : scoreHistory) {
            System.out.println("- " + result.getSummary());
            if (result.won()) {
                wonRounds++;
            }
        }

        System.out.println("-----------------------------------------");
        System.out.println("Total rounds played : " + totalRounds);
        System.out.println("Rounds won          : " + wonRounds + "/" + totalRounds);
        System.out.println("=========================================");
    }
}
