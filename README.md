# Combat-game

The Squid Game Final Duel is a turn-based console game developed in Java, where a human player (Player 1) battles an AI-controlled opponent (Player 2)

in a series of strategic matches. The game is inspired by the psychological and tactical elements of the "Squid Game" series, emphasizing decision-making and anticipation of the opponent’s moves.
# Squid Game: Final Duel (Java Game with AI)

This is a console-based Java game inspired by the Squid Game series, featuring a *Final Duel* between the player and an AI opponent. The AI uses a *backtracking algorithm* to select its moves intelligently based on future outcomes.

 Game Features

-  Real-time console battle simulation
-   AI opponent with backtracking decision-making
-  Health bar visualizations
-  Sound effects for attacks, dodges, blocks, and victories
-  ASCII-based fight animation



# Tech Stack

- *Language*: Java (JDK 8+)
- *Audio*: javax.sound.sampled API for .wav sound playback
- *User Input*: Java Scanner class
- *AI*: Minimax-style backtracking to find optimal moves

 # Gameplay Instructions

Choose the number of matches to play.

In each round, you can enter one of the following moves:

- ATTACK

- BLOCK

- DODGE

- NOTHING


The AI will respond based on backtracking logic.

First to reduce the opponent's health to 0 wins the match.

Sound effects and ASCII animations enhance the experience
