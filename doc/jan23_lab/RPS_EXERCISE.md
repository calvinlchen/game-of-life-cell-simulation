# Rock Paper Scissors Lab Discussion
#### Kyaira Boughton (keb125), Jessica Chen (jc939), Calvin Chen (clc162)


### High Level Design Goals

Game: 
- The game class controls keeping track of the player, gestures, and rules for this game. 
- Rule<List> add new a rule

Rules:
- Rules needs to be able to know the gesture and determine the interactions between the pairs of gestures.

Player:
- Needs to be able to select a gesture and keep track of its score.

Gesture:
- The gesture needs to be able to see the rules and determine their interaction from these rules.

### CRC Card Classes
The purpose of rules is to model the interactions between gestures:

|Rules| |
|---|---|
|void addRule(new Gesture, List of Gestures it beats, List of Gestures that beat it)         |Gesture|
|Gesture checkWin(Gesture 1, Gesture 2) ||
|void updateRule(to be updated Gesture, List of Gestures it beats, List of Gestures that beat it) ||

The purpose of gestures is to store what the gestures should be -- but we can also make it a enum if its just in text form.

The purpose of players is to keep track of their gesture choice and their personal score.
|Players| |
|---|---|
|Gesture choseGesture()        |Gesture|
|void updateScore(amount to update) ||
|int getScore() ||
|void setScore(amunt to set) ||

The purpose of game is to store the currents rules and player and the interactions betweeen them.
|Game| |
|---|---|
|Game(number of players, rules) and also Game(number of players)       |Player|
|void scoreRound() # get each players gestures and compare them two by two, whoever has the most add score to that player |Gestures|
|void resetScores() |Rules|



### Use Cases

 * A new game is started with five players, their scores are reset to 0.
 ```java
 Game game = new Game(5);
 game.resetScores();
 // resetScores would call for all the players created setScore(0)
 ```

 * A player chooses his RPS "weapon" with which he wants to play for this round.
 ```java
 Player player = new Player();
 player.chooseGesture();
 ```

 * Given three players' choices, one player wins the round, and their scores are updated.
 ```java
 // imagine in a game you call for all players player.chooseGesture

 game.scoreRound();
 // for each pair of gestures, the game calls the rules.checkWin, whichever player has the most wins between these gets the final point
 ```

 * A new choice is added to an existing game and its relationship to all the other choices is updated.
 ```java
 // we have an existing rules
 rules.addRule(newGesture, [rock, paper], [scissors])
 ```

 * A new game is added to the system, with its own relationships for its all its "weapons".
 ```java
 // and the rules were already predefined in rules
 Game game2 = new Game(5, rules);
 ```
