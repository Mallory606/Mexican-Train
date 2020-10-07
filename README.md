CS 351 Project 2

Mexican Train

Ashley Krattiger

Main is in MexicanTrainMaster.
This program takes one argument: either "console" for a console game or
                                 "GUI" for a GUI game

Console Game:
Upon initializing this game the program will print to the console and prompt
you for the number of players and computers. After receiving valid answers
(2-4 for players and 0-# of players for number of computers) the game begins.
With each turn, the console will print the state of all trains in the game, the
current player's hand, and a menu with options [p] play domino
                                               [d] draw from boneyard
You respond by entering "p" or "d". If you draw from the boneyard, the [d] 
option switches to [d] pass. The game will continue by asking for prompts in a 
similar fashion. If the game receives invalid input, it will tell you and prompt
you to try again. It will also inform you when you make an invalid move or if
you can play another domino in the same turn. You choose dominoes from your hand
based on their numbering from 0-size of hand-1. When your turn is over, the 
computer players will make their moves without printing anything out. When a 
round ends it will print out a message telling you the round is over and another
that gives the name of the person that is currently winning. When the game ends,
it will instead name them the winner. The game ends when either the players 
complete the last round or the players go through a full round of play where 
everyone skips after the boneyard has been emptied. Console game is set up to
reject incorrect inputs and will loop until a proper response is given.

GUI Game:
Upon initializing this game the program will open a pop up window prompting you
to choose the number of players and computers from their respective lists. For
the GUI game, it is required that one of the players be human. Once both fields
have been chosen, the "Start" button will close the pop up and start the game.
The GUI has two main sections: the left which holds the visual representation of
the board and the current player's hand, and the right which has the interface 
the human players will use to play the game. The interface will inform the user
of whose turn it is, that player's score, and how many dominoes are left in the 
boneyard. Beneath the top labels are two selection boxes which allow you to pick
the domino in your hand (which are labeled numerically again, this time starting
from 1) and the train you want to play on. There are buttons below that allow 
you to flip the chosen domino, draw from the boneyard, and play the chosen 
domino on the chosen train. If the move you try to make fails, a pop up window
will tell you why. If it just says "Invalid move", that means your move was 
illegal based on the set rules. If you pull from the boneyard, the "pull from 
boneyard" button will be replaced by a "pass" button. Computers will again 
complete their turns on their own without changing the interface. When a round 
is over, another pop up will appear and tell you who is in the lead. When the
game is over, it will inform you of who won. The GUI will appear unresponsive 
if necessary fields are left empty. The scroll bar at the bottom of the screen
will reveal the rest of the game board if dominoes are concealed by the edge.

I left some debugging code in MexicanTrainManager and ComputerPlayer. It is 
commented out and serves to show what the computer is doing behind the scenes.