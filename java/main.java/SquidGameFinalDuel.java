import java.io.*;
import java.util.*;
import javax.sound.sampled.*;

public class SquidGameFinalDuel {
    static int p1Health, p2Health;
    static int player1Wins = 0;
    static int player2Wins = 0;
    static int draws = 0;

    static String[] moves = {"ATTACK", "BLOCK", "DODGE", "NOTHING"};

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Squid Game Final Duel with AI ===");
        System.out.print("How many matches do you want to play? ");
        int totalMatches = sc.nextInt();

        for (int match = 1; match <= totalMatches; match++) {
            p1Health = 100;
            p2Health = 100;
            int round = 1;

            System.out.println("\n===============================");
            System.out.println("MATCH " + match + " START");
            System.out.println("Player 1 vs AI (Backtracking)");
            System.out.println("Actions: ATTACK, BLOCK, DODGE, NOTHING");

            while (p1Health > 0 && p2Health > 0) {
                System.out.println("\n-------------------------------");
                System.out.println("ROUND " + round++);
                printHealthBars();

                System.out.print("Player 1, enter your move: ");
                String p1Move = sc.next().toUpperCase();

                if (!Arrays.asList(moves).contains(p1Move)) {
                    System.out.println("Invalid move. Try again.");
                    continue;
                }

                String p2Move = findBestMove(p1Move, 2);
                System.out.println("AI chooses: " + p2Move);

                animateFight(p1Move, p2Move);
                resolveRound(p1Move, p2Move);
            }

            printHealthBars();

            if (p1Health <= 0 && p2Health <= 0) {
                System.out.println("It's a Draw!");
                playSound("draw.wav");
                draws++;
            } else if (p1Health <= 0) {
                System.out.println("AI (Player 2) Wins this match!");
                playSound("win.wav");
                player2Wins++;
            } else {
                System.out.println("Player 1 Wins this match!");
                playSound("win.wav");
                player1Wins++;
            }
        }

        System.out.println("\n===============================");
        System.out.println("FINAL SCORE");
        System.out.println("Player 1 Wins: " + player1Wins);
        System.out.println("AI Wins:       " + player2Wins);
        System.out.println("Draws:         " + draws);
    }

    public static String findBestMove(String p1Move, int depth) {
        int bestHealth = Integer.MIN_VALUE;
        String bestMove = "NOTHING";

        for (String move : moves) {
            int origP1 = p1Health;
            int origP2 = p2Health;

            simulateRound(p1Move, move);
            int result = backtrack(depth - 1, false);

            if (result > bestHealth) {
                bestHealth = result;
                bestMove = move;
            }

            p1Health = origP1;
            p2Health = origP2;
        }
        return bestMove;
    }

    public static int backtrack(int depth, boolean isPlayer1Turn) {
        if (depth == 0 || p1Health <= 0 || p2Health <= 0) {
            return p2Health - p1Health;
        }

        int bestScore = isPlayer1Turn ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (String move : moves) {
            for (String oppMove : moves) {
                int origP1 = p1Health;
                int origP2 = p2Health;

                if (isPlayer1Turn)
                    simulateRound(move, oppMove);
                else
                    simulateRound(oppMove, move);

                int score = backtrack(depth - 1, !isPlayer1Turn);

                if (isPlayer1Turn)
                    bestScore = Math.max(bestScore, score);
                else
                    bestScore = Math.min(bestScore, score);

                p1Health = origP1;
                p2Health = origP2;
            }
        }
        return bestScore;
    }

    public static void simulateRound(String p1, String p2) {
        if (p1.equals("ATTACK") && p2.equals("ATTACK")) {
            p1Health -= 15;
            p2Health -= 15;
        } else if (p1.equals("ATTACK") && (p2.equals("BLOCK") || p2.equals("DODGE"))) {
            // Attack blocked or dodged — no damage
        } else if (p1.equals("ATTACK")) {
            p2Health -= 20;
        }

        if (p2.equals("ATTACK") && (p1.equals("BLOCK") || p1.equals("DODGE"))) {
            // Attack blocked or dodged — no damage
        } else if (p2.equals("ATTACK") && !p1.equals("ATTACK")) {
            p1Health -= 20;
        }
    }

    public static void resolveRound(String p1, String p2) {
        if (p1.equals("ATTACK") && p2.equals("ATTACK")) {
            System.out.println("Both attacked! Both lose 15 HP.");
            playSound("attack.wav");
        } else if (p1.equals("ATTACK") && p2.equals("BLOCK")) {
            System.out.println("AI blocked your attack.");
            playSound("block.wav");
        } else if (p1.equals("ATTACK") && p2.equals("DODGE")) {
            System.out.println("AI dodged your attack!");
            playSound("dodge.wav");
        } else if (p1.equals("ATTACK")) {
            System.out.println("You attacked! AI loses 20 HP.");
            playSound("attack.wav");
        } else if (p2.equals("ATTACK") && p1.equals("BLOCK")) {
            System.out.println("You blocked AI's attack.");
            playSound("block.wav");
        } else if (p2.equals("ATTACK") && p1.equals("DODGE")) {
            System.out.println("You dodged AI's attack!");
            playSound("dodge.wav");
        } else if (p2.equals("ATTACK")) {
            System.out.println("AI attacked! You lose 20 HP.");
            playSound("attack.wav");
        } else {
            System.out.println("No hits this round.");
        }

        // Simulate health after move
        simulateRound(p1, p2);
    }

    public static void printHealthBars() {
        System.out.println("Player 1: " + p1Health + " " + healthBar(p1Health));
        System.out.println("AI       : " + p2Health + " " + healthBar(p2Health));
    }

    public static String healthBar(int hp) {
        int bars = Math.max(hp, 0) / 5;
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < bars; i++) bar.append("#");
        for (int i = bars; i < 20; i++) bar.append("-");
        return "[" + bar + "]";
    }

    public static void playSound(String fileName) {
        File soundFile = new File("sound/" + fileName);
        if (!soundFile.exists()) {
            System.out.println("File not found: " + soundFile.getAbsolutePath());
            return;
        }

        try (AudioInputStream audio = AudioSystem.getAudioInputStream(soundFile);
            
        Clip clip = AudioSystem.getClip()) {

            clip.open(audio);
            clip.start();
            Thread.sleep(clip.getMicrosecondLength() / 1000);
            clip.close();

        } catch (Exception e) {
            System.out.println("Sound error: " + fileName);
        }
    }

    public static void animateFight(String p1Move, String p2Move) {
        String[] frames = {
            "P1         -->         P2",
            "P1       -->           P2",
            "P1     -->             P2",
            "P1   -->               P2",
            "P1 -->                 P2",
            "P1>>>>>               <<P2",
            "    *** IMPACT ***    "
        };

        System.out.println("\n--- Fight Animation ---");
        for (String frame : frames) {
            System.out.print("\r" + frame);
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println("\n>>> ACTION: P1 [" + p1Move + "] vs AI [" + p2Move + "] <<<\n");
    }
}