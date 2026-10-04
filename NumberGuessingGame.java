import java.util.Random;
import java.util.Scanner;

/**
 * Number Guessing Game
 * --------------------
 * A console-based game where the player tries to guess a hidden number
 * between 1 and 100 within 7 attempts. The game supports multiple rounds,
 * a scoring system, input validation and a final results summary.
 */
public class NumberGuessingGame {

    // ---------- Game settings (constants never change while the program runs) ----------
    static final int MIN_NUMBER = 1;     // smallest possible secret number
    static final int MAX_NUMBER = 100;   // largest possible secret number
    static final int MAX_ATTEMPTS = 7;   // attempts allowed per round

    // ---------------------------------- main method ----------------------------------
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);  // reads keyboard input
        Random random = new Random();              // generates random numbers

        // Variables that track the whole game session
        int totalScore = 0;
        int roundsPlayed = 0;
        int roundsWon = 0;

        displayWelcome();

        boolean keepPlaying = true;

        // Each loop repetition is one round of the game
        while (keepPlaying) {
            roundsPlayed++;

            // Play one round; it returns the points earned (0 if the player lost)
            int pointsEarned = playRound(scanner, random, roundsPlayed);

            if (pointsEarned > 0) {
                roundsWon++;                 // a win always gives more than 0 points
            }
            totalScore += pointsEarned;

            System.out.println("Points this round: " + pointsEarned);
            System.out.println("Total score so far: " + totalScore);

            // Ask whether the player wants another round
            keepPlaying = askPlayAgain(scanner);
        }

        displayFinalResults(roundsPlayed, roundsWon, totalScore);
        scanner.close();
    }

    // ------------------------------ Round logic ------------------------------

    /**
     * Plays a single round of the game.
     *
     * @return the points earned in this round (0 if the player did not guess the number)
     */
    static int playRound(Scanner scanner, Random random, int roundNumber) {

        // nextInt(100) gives 0-99, so adding 1 gives 1-100.
        // This number is never printed, so it stays hidden from the player.
        int secretNumber = random.nextInt(MAX_NUMBER - MIN_NUMBER + 1) + MIN_NUMBER;

        System.out.println();
        System.out.println("========== ROUND " + roundNumber + " ==========");
        System.out.println("I'm thinking of a number between " + MIN_NUMBER
                + " and " + MAX_NUMBER + ".");
        System.out.println("You have " + MAX_ATTEMPTS + " attempts. Good luck!");

        // Go through attempts 1 to 7
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {

            int guess = getValidGuess(scanner, attempt);

            if (guess == secretNumber) {
                System.out.println("Correct! You guessed the number.");
                return calculatePoints(attempt);   // round won: leave the method with points
            } else if (guess > secretNumber) {
                System.out.println("Too High!");
            } else {
                System.out.println("Too Low!");
            }

            // Show remaining attempts after a wrong guess
            int attemptsLeft = MAX_ATTEMPTS - attempt;
            if (attemptsLeft > 0) {
                System.out.println("Attempts remaining: " + attemptsLeft);
            }
        }

        // The loop finished without a correct guess, so the round is lost
        System.out.println("Out of attempts! The correct number was " + secretNumber + ".");
        return 0;
    }

    /**
     * Keeps asking until the user enters a whole number from 1 to 100.
     * Text, empty input and out-of-range numbers are rejected with a message.
     */
    static int getValidGuess(Scanner scanner, int attemptNumber) {
        while (true) {
            System.out.print("Attempt " + attemptNumber + "/" + MAX_ATTEMPTS
                    + " - Enter your guess (" + MIN_NUMBER + "-" + MAX_NUMBER + "): ");
            String input = scanner.nextLine().trim();   // read the whole line, remove spaces

            try {
                int guess = Integer.parseInt(input);    // fails if input is not a number

                if (guess < MIN_NUMBER || guess > MAX_NUMBER) {
                    System.out.println("Invalid range! Please enter a number between "
                            + MIN_NUMBER + " and " + MAX_NUMBER + ".");
                } else {
                    return guess;                       // valid guess
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a whole number, not text.");
            }
        }
    }

    // ------------------------------ Scoring ------------------------------

    /**
     * Returns the points for winning on the given attempt number.
     * The fewer attempts used, the more points the player earns.
     */
    static int calculatePoints(int attemptsUsed) {
        if (attemptsUsed == 1) {
            return 100;
        } else if (attemptsUsed == 2) {
            return 80;
        } else if (attemptsUsed == 3) {
            return 60;
        } else if (attemptsUsed == 4) {
            return 40;
        } else if (attemptsUsed == 5) {
            return 30;
        } else if (attemptsUsed == 6) {
            return 20;
        } else if (attemptsUsed == 7) {
            return 10;
        } else {
            return 0;
        }
    }

    // ------------------------- Play again / Results -------------------------

    /**
     * Asks "Play again?" until the user types Y/y or N/n.
     *
     * @return true for yes, false for no
     */
    static boolean askPlayAgain(Scanner scanner) {
        while (true) {
            System.out.print("\nDo you want to play again? (Y/N): ");
            String answer = scanner.nextLine().trim();

            if (answer.equalsIgnoreCase("Y")) {
                return true;
            } else if (answer.equalsIgnoreCase("N")) {
                return false;
            } else {
                System.out.println("Invalid choice! Please enter Y for yes or N for no.");
            }
        }
    }

    /** Prints the welcome banner and rules. */
    static void displayWelcome() {
        System.out.println("=======================================");
        System.out.println("      WELCOME TO THE NUMBER GAME       ");
        System.out.println("=======================================");
        System.out.println("Guess the hidden number in " + MAX_ATTEMPTS + " attempts or fewer.");
        System.out.println("Fewer attempts = more points!");
        System.out.println("  1st: 100 | 2nd: 80 | 3rd: 60 | 4th: 40");
        System.out.println("  5th: 30  | 6th: 20 | 7th: 10 | Fail: 0");
    }

    /** Prints the final statistics when the player quits. */
    static void displayFinalResults(int roundsPlayed, int roundsWon, int totalScore) {
        int roundsLost = roundsPlayed - roundsWon;

        // Cast to double so the division keeps decimal places (e.g. 63.33, not 63)
        double averageScore = (double) totalScore / roundsPlayed;

        System.out.println();
        System.out.println("=======================================");
        System.out.println("             FINAL RESULTS             ");
        System.out.println("=======================================");
        System.out.println("Total rounds played : " + roundsPlayed);
        System.out.println("Rounds won          : " + roundsWon);
        System.out.println("Rounds lost         : " + roundsLost);
        System.out.println("Total score         : " + totalScore);
        System.out.printf("Average score/round : %.2f%n", averageScore);
        System.out.println("=======================================");
        System.out.println("Thanks for playing. Goodbye!");
    }
}