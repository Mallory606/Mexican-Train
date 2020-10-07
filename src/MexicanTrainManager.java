import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/******************************************************************************
 * Ashley Krattiger                                                           *
 *                                                                            *
 * MexicanTrainManager                                                        *
 *                                                                            *
 * Runs the game and organizes the objects needed for gameplay                *
 *****************************************************************************/
public class MexicanTrainManager{
    /**************************************************************************
     * Global Variables:                                                      *
     * boneyard - List which holds all the Dominos in the boneyard            *
     * players - List which holds all the Players in the game                 *
     * mexicanTrain - List which holds all the Dominos played on the Mexican  *
     *                Train                                                   *
     * playerTrains - List which holds all the players' personal trains       *
     * trainMarked - List which keeps track of whether a player's personal    *
     *               train is marked. 0 indicates the train is not marked, 1  *
     *               indicates that it is. The first integer(0) in the List is*
     *               used to determine if there is an open double on the board*
     *               The rest of the integers represent the personal train of *
     *               the player with the same number as its position in this  *
     *               List.                                                    *
     * consoleGame - boolean which is true if this game is played on the      *
     *               console, and false if it is played on GUI                *
     * newRound - boolean which keeps track of whether a new round is starting*
     *            for the purpose of sharing information with the GUI         *
     * numPlayers - holds the total number of players in the game             *
     * currPlayer - holds the number representing the player whose turn it is *
     * round - holds the number value of the double domino which is currently *
     *         at the center of the board. This is used to represent which    *
     *         round of the game is currently being played                    *
     * numSkips - tallies how many skips in a row have happened after the     *
     *            boneyard has been emptied. The game ends if this int reaches*
     *            4 skips in a row                                            *
     * currWinner - holds the name of the player who is in the lead at the end*
     *              of the last round of the game                             *
     *************************************************************************/
    private List<Domino> boneyard;
    private List<Player> players;
    private List<Domino> mexicanTrain;
    private List<List<Domino>> playerTrains;
    private List<Integer> trainMarked;
    private final boolean consoleGame;
    private boolean newRound;
    private int numPlayers;
    private int currPlayer;
    private int round;
    private int numSkips;
    private String currWinner;

    /**************************************************************************
     * MexicanTrainManager Constructor                                        *
     *                                                                        *
     * Initializes global variables and starts the procedure for the console  *
     * game if applicable.                                                    *
     *                                                                        *
     * Argument: console - tells us if the game is a console game             *
     *************************************************************************/
    public MexicanTrainManager(boolean console){
        consoleGame = console;
        round = 9;
        numSkips = 0;
        newRound = false;
        initializeBoneyard(round);
        if(consoleGame){
            try{ consoleGame(); }
            catch(IOException e){ e.printStackTrace(); }
        }
    }

    /**************************************************************************
     * initializeBoneyard                                                     *
     *                                                                        *
     * Initializes the List boneyard and fills it with Dominos                *
     *                                                                        *
     * Argument: round - indicates which double will be starting the Mexican  *
     *                   Train                                                *
     * Returns nothing                                                        *
     *************************************************************************/
    private void initializeBoneyard(int round){
        boneyard = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            for(int j = i; j < 10; j++){
                if(i == round && j == round) {
                    mexicanTrain = new ArrayList<>();
                    mexicanTrain.add(new Domino(round, round));
                }
                else{ boneyard.add(new Domino(i, j)); }
            }
        }
    }

    /**************************************************************************
     * initializePlayers                                                      *
     *                                                                        *
     * Initializes the Lists of players and playerTrains and passes on the    *
     * necessary Lists so the Player objects can access the trains. Also gives*
     * them a slot in trainMarked                                             *
     *                                                                        *
     * Argument: numComputers - the number of ComputerPlayers for this game   *
     * Returns nothing                                                        *
     *                                                                        *
     * Variable:                                                              *
     * playerNumber - used to give each player a numerical name               *
     *************************************************************************/
    public void initializePlayers(int numComputers){
        int playerNumber = 1;
        players = new ArrayList<>();
        playerTrains = new ArrayList<>();
        for(int i = 0; i < (numPlayers-numComputers); i++){
            players.add(new HumanPlayer(playerNumber++, consoleGame));
            playerTrains.add(new ArrayList<>());
        }
        for(int j = 0; j < numComputers; j++){
            players.add(new ComputerPlayer(playerNumber++, consoleGame));
            playerTrains.add(new ArrayList<>());
        }
        trainMarked = new ArrayList<>();
        for(int k = 0; k <= numPlayers; k++){ trainMarked.add(0); }
        initializeHands();
        for(Player p : players){
            p.giveTrains(boneyard, mexicanTrain, playerTrains, trainMarked);
        }
    }

    /**************************************************************************
     * initializeHands                                                        *
     *                                                                        *
     * Deals a hand of Dominos to each player from the boneyard               *
     *                                                                        *
     * Takes no arguments, returns nothing                                    *
     *                                                                        *
     * Variable:                                                              *
     * numTiles - holds the number of Dominos which need to be dealt to each  *
     *            player. This number is determined by the total number of    *
     *            players in the game                                         *
     *************************************************************************/
    private void initializeHands(){
        int numTiles;
        if(numPlayers == 2){ numTiles = 15; }
        else if(numPlayers == 3){ numTiles = 13; }
        else{ numTiles = 10; }
        for(int i = 0; i < numPlayers; i++){
            for(int j = 0; j < numTiles; j++){
                players.get(i).dealDomino(pullFromBoneyard());
            }
        }
    }

    /**************************************************************************
     * pullFromBoneyard                                                       *
     *                                                                        *
     * Returns a random Domino from the boneyard. Takes no arguments          *
     *                                                                        *
     * Variable:                                                              *
     * rand - holds the random integer that is generated                      *
     *************************************************************************/
    private Domino pullFromBoneyard(){
        int rand = (int)(Math.random()*boneyard.size());
        return boneyard.remove(rand);
    }

    /**************************************************************************
     * getBoneyard                                                            *
     *                                                                        *
     * Getter. Returns the boneyard. Takes no arguments                       *
     *************************************************************************/
    public List<Domino> getBoneyard(){ return boneyard; }

    /**************************************************************************
     * getPlayers                                                             *
     *                                                                        *
     * Getter. Returns List players. Takes no arguments                       *
     *************************************************************************/
    public List<Player> getPlayers(){ return players; }

    /**************************************************************************
     * getMexicanTrain                                                        *
     *                                                                        *
     * Getter. Returns the mexicanTrain. Takes no arguments                   *
     *************************************************************************/
    public List<Domino> getMexicanTrain(){ return mexicanTrain; }

    /**************************************************************************
     * getPlayerTrains                                                        *
     *                                                                        *
     * Getter. Returns List playerTrains. Takes no arguments                  *
     *************************************************************************/
    public List<List<Domino>> getPlayerTrains(){ return playerTrains; }

    /**************************************************************************
     * getTrainMarked                                                         *
     *                                                                        *
     * Getter. Returns List trainMarked. Takes no arguments                   *
     *************************************************************************/
    public List<Integer> getTrainMarked(){ return trainMarked; }

    /**************************************************************************
     * setNumPlayers                                                          *
     *                                                                        *
     * Setter. Sets numPlayers to value in arg num. Returns nothing           *
     *************************************************************************/
    public void setNumPlayers(int num){ numPlayers = num; }

    /**************************************************************************
     * setCurrPlayer                                                          *
     *                                                                        *
     * Setter. Sets currPlayer to value in arg curr. Returns nothing          *
     *************************************************************************/
    public void setCurrPlayer(int curr) { currPlayer = curr; }

    /**************************************************************************
     * getCurrPlayer                                                          *
     *                                                                        *
     * Getter. Returns the currPlayer. Takes no arguments                     *
     *************************************************************************/
    public int getCurrPlayer() { return currPlayer; }

    /**************************************************************************
     * isNewRound                                                             *
     *                                                                        *
     * Getter. Returns boolean newRound. Takes no arguments                   *
     *************************************************************************/
    public boolean isNewRound(){ return newRound; }

    /**************************************************************************
     * resetNewRound                                                          *
     *                                                                        *
     * Setter. Sets boolean newRound to false. Takes no args, returns nothing *
     *************************************************************************/
    public void resetNewRound(){ newRound = false; }

    /**************************************************************************
     * getCurrWinner                                                          *
     *                                                                        *
     * Getter. Returns currWinner. Takes no arguments                         *
     *************************************************************************/
    public String getCurrWinner(){ return currWinner; }

    /**************************************************************************
     * printGameState                                                         *
     *                                                                        *
     * Prints out the current state of the trains on the console              *
     *                                                                        *
     * Takes no arguments, returns nothing                                    *
     *************************************************************************/
    private void printGameState(){
        System.out.println("GameState:\n");
        for(int i = 0; i < numPlayers; i++){
            System.out.print(players.get(i).toString() + ": ");
            if(trainMarked.get(i+1) == 1){ System.out.print("* "); }
            printTrain(playerTrains.get(i), false);
            System.out.println("\n");
        }
        System.out.print("Mexican Train: ");
        printTrain(mexicanTrain, true);
        System.out.println("\n");
    }

    /**************************************************************************
     * printTrain                                                             *
     *                                                                        *
     * Prints out representations of each Domino in a given train. Requires   *
     * extra spaces to fix line spacing if the train is the Mexican Train     *
     *                                                                        *
     * Arguments: train - train that we want to print to console              *
     *            mexican - tells us if the given train is the Mexican Train  *
     * Returns nothing                                                        *
     *                                                                        *
     * Variables:                                                             *
     * line1 - holds the Dominos that will be printed in the first line       *
     * line2 - holds the Dominos that will be printed in the second line      *
     * line - cycles between 1 and 2 to determine if the next Domino should be*
     *        added to line1 or line2                                         *
     *************************************************************************/
    private void printTrain(List<Domino> train, boolean mexican){
        String line1 = "";
        String line2 = "      ";
        int line = 1;
        for(Domino d : train){
            if(line == 1){
                line1 += d.toString() + " ";
                line = 2;
            }
            else{
                line2 += d.toString() + " ";
                line = 1;
            }
        }
        if(mexican){ line2 = "              " + line2; }
        else{ line2 = "         " + line2; }
        System.out.println(line1);
        System.out.print(line2);
    }

    /**************************************************************************
     * newRound                                                               *
     *                                                                        *
     * Handles necessary steps to set up the game when a new round is starting*
     * Resets the boneyard, trains, and hands. Passes new Trains on to players*
     *                                                                        *
     * Takes no arguments, returns nothing                                    *
     *************************************************************************/
    private void newRound(){
        initializeBoneyard(--round);
        playerTrains = new ArrayList<>();
        for(int i = 0; i<numPlayers; i++){playerTrains.add(new ArrayList<>());}
        trainMarked = new ArrayList<>();
        for(int k = 0; k <= numPlayers; k++){ trainMarked.add(0); }
        for(Player p : players){
            p.newHand();
            p.giveTrains(boneyard, mexicanTrain, playerTrains, trainMarked);
        }
        initializeHands();
    }

    /**************************************************************************
     * roundOver                                                              *
     *                                                                        *
     * Checks to see if the round is over. Prints out current winner if it is *
     *                                                                        *
     * Returns true if round is over, and false if it isn't. Takes no args    *
     *                                                                        *
     * Variable:                                                              *
     * winner - holds name of current winner                                  *
     *************************************************************************/
    private boolean roundOver(){
        String winner;
        if(boneyard.size() == 0){ numSkips++; }
        for(Player p : players){
            if(p.handEmpty() || numSkips == numPlayers){
                winner = tallyScores();
                if(consoleGame){
                    System.out.println("Round over!");
                    if(round == 0){ System.out.println(winner + " wins!"); }
                    else{ System.out.println(winner + " is in the lead!"); }
                }
                else{ currWinner = winner; }
                return true;
            }
        }
        return false;
    }

    /**************************************************************************
     * tallyScores                                                            *
     *                                                                        *
     * Updates the scores for each player and returns the name of the person  *
     * with the lowest score. Takes no arguments                              *
     *                                                                        *
     * Variables:                                                             *
     * min - holds the lowest score                                           *
     * winner - holds the name of the person with the lowest score            *
     *************************************************************************/
    private String tallyScores(){
        int min = 100;
        String winner = "";
        for(Player p : players){
            p.updateScore();
            if(p.getScore() < min){ winner = p.toString(); }
        }
        return winner;
    }

    /**************************************************************************
     * consoleGame                                                            *
     *                                                                        *
     * Runs the console version of the game. Reads input from console to      *
     * interact with players. Checks for valid input and loops until useable  *
     * responses are given. Ends game when roundOver() returns true.          *
     *                                                                        *
     * Takes no arguments, returns nothing                                    *
     *                                                                        *
     * Throws IOException                                                     *
     *                                                                        *
     * Variables:                                                             *
     * in - BufferedReader which communicates with System.in                  *
     * input - holds input read in from System.in                             *
     * numberInput - holds numbers parsed from input                          *
     * validInput - keeps track of whether program was given valid input for a*
     *              given prompt                                              *
     *************************************************************************/
    private void consoleGame() throws IOException{
        BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
        String input;
        int numberInput = 0;
        boolean validInput = false;
        System.out.println("Let's play Mexican Train!");
        while(!validInput){
            System.out.println("How many players?");
            input = in.readLine();
            try{ numberInput = Integer.parseInt(input); }
            catch(NumberFormatException e){ numberInput = 0; }
            if(numberInput > 1 && numberInput < 5){ validInput = true; }
            else{ System.out.println("Invalid input! Try again!"); }
        }
        numPlayers = numberInput;
        validInput = false;
        while(!validInput){
            System.out.println("How many computers?");
            input = in.readLine();
            try { numberInput = Integer.parseInt(input); }
            catch(NumberFormatException e){ numberInput = 100; }
            if(numberInput <= numPlayers){ validInput = true; }
            else{ System.out.println("Invalid input! Try again!"); }
        }
        initializePlayers(numberInput);
        System.out.println("Starting game with " + numPlayers + " players and "
                + numberInput + " computers!");
        /*for(int i = 0; i < numPlayers; i++){
            System.out.println("Player "+(i+1)+": "+players.get(i).getHand());
        }*/
        currPlayer = 0;
        boolean gameRunning = true;
        while(gameRunning){
            printGameState();
            System.out.println("It's " + players.get(currPlayer).toString()
                    + "'s turn!\n");
            players.get(currPlayer).makeMove();
            if(currPlayer == numPlayers-1){ currPlayer = 0; }
            else{ currPlayer++; }
            if(roundOver()){
                if(round == 0){ gameRunning = false; }
                else{ newRound(); }
            }
        }
        System.out.println("GAME OVER");
    }

    /**************************************************************************
     * guiGame                                                                *
     *                                                                        *
     * Handles checking if the round is over and doing computerPlayers' turns *
     *                                                                        *
     * Returns true if the game is still running. Takes no arguments          *
     *************************************************************************/
    public boolean guiGame(){
        if(currPlayer == numPlayers-1){ currPlayer = 0; }
        else{ currPlayer++; }
        if(roundOver()){
            if(round == 0){ return false; }
            else{
                newRound = true;
                newRound();
            }
        }
        while(!(players.get(currPlayer).isHuman())){
            players.get(currPlayer).makeMove();
            if(currPlayer == numPlayers-1){ currPlayer = 0; }
            else{ currPlayer++; }
        }
        return true;
    }
}
