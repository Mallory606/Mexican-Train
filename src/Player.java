import java.util.ArrayList;
import java.util.List;

/******************************************************************************
 * Ashley Krattiger                                                           *
 *                                                                            *
 * Player                                                                     *
 *                                                                            *
 * Holds all the information needed for gameplay and checks moves to make     *
 * sure they are valid                                                        *
 *****************************************************************************/
public abstract class Player{
    /**************************************************************************
     * Global Variables:                                                      *
     * name - int which represents the name of the player e.g "Player 1"      *
     * hand - List of Dominos the Player has and can play                     *
     * score - holds the Player's score                                       *
     * consoleGame - boolean which is true if this game is played on the      *
     *               console, and false if it is played on GUI                *
     * isHuman - boolean that tells us if the Player is a HumanPlayer         *
     * moveError - holds the error message if one is generated during a turn  *
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
     *************************************************************************/
    private final int name;
    private List<Domino> hand;
    private int score;
    private final boolean consoleGame;
    private final boolean isHuman;
    private String moveError;
    private List<Domino> boneyard;
    private List<Domino> mexicanTrain;
    private List<List<Domino>> playerTrains;
    private List<Integer> trainMarked;

    /**************************************************************************
     * Player Constructor                                                     *
     *                                                                        *
     * Initializes global variables                                           *
     *                                                                        *
     * Argument: n - the name of the Player                                   *
     *           console - tells us if the game is a console game             *
     *           human - tells us if the Player is a HumanPlayer              *
     *************************************************************************/
    public Player(int n, boolean console, boolean human){
        name = n;
        consoleGame = console;
        hand = new ArrayList<>();
        score = 0;
        isHuman = human;
        moveError = "";
    }

    /**************************************************************************
     * makeMove                                                               *
     *                                                                        *
     * Abstract method. Completes a turn for the Player                       *
     *                                                                        *
     * Takes no arguments, returns nothing                                    *
     *************************************************************************/
    public abstract void makeMove();

    /**************************************************************************
     * giveTrains                                                             *
     *                                                                        *
     * Takes Lists given by MexicanTrainManager and stores them in global     *
     * variables                                                              *
     *                                                                        *
     * Arguments: boneYd - boneyard                                           *
     *            mexTrain - mexicanTrain                                     *
     *            plTrain - playerTrains                                      *
     *            trainMrkd - trainMarked                                     *
     * Returns nothing                                                        *
     *************************************************************************/
    public void giveTrains(List<Domino> boneYd, List<Domino> mexTrain,
                           List<List<Domino>> plTrain,List<Integer> trainMrkd){
        boneyard = boneYd;
        mexicanTrain = mexTrain;
        playerTrains = plTrain;
        trainMarked = trainMrkd;
    }

    /**************************************************************************
     * dealDomino                                                             *
     *                                                                        *
     * Adds Domino given as argument to player's hand. Returns nothing        *
     *************************************************************************/
    public void dealDomino(Domino d){ hand.add(d); }

    /**************************************************************************
     * newHand                                                                *
     *                                                                        *
     * Sets List hand to new List. Takes no arguments, returns nothing        *
     *************************************************************************/
    public void newHand(){ hand = new ArrayList<>(); }

    /**************************************************************************
     * fromHand                                                               *
     *                                                                        *
     * Getter. Returns the Domino at index i. Takes argument: i               *
     *************************************************************************/
    public Domino fromHand(int i){ return hand.get(i); }

    /**************************************************************************
     * handSize                                                               *
     *                                                                        *
     * Getter. Returns the size of hand. Takes no arguments                   *
     *************************************************************************/
    public int handSize(){ return hand.size(); }

    /**************************************************************************
     * getName                                                                *
     *                                                                        *
     * Getter. Returns name. Takes no arguments                               *
     *************************************************************************/
    public int getName(){ return name; }

    /**************************************************************************
     * getScore                                                               *
     *                                                                        *
     * Getter. Returns score. Takes no arguments                              *
     *************************************************************************/
    public int getScore(){ return score; }

    /**************************************************************************
     * updateScore                                                            *
     *                                                                        *
     * Adds points from current hand to score.                                *
     * Takes no arguments, returns nothing                                    *
     *************************************************************************/
    public void updateScore(){
        int update = 0;
        for(Domino d : hand){ update += d.getScoreTotal(); }
        score += update;
    }

    /**************************************************************************
     * isConsoleGame                                                          *
     *                                                                        *
     * Getter. Returns consoleGame. Takes no arguments                        *
     *************************************************************************/
    public boolean isConsoleGame(){ return consoleGame; }

    /**************************************************************************
     * isHuman                                                                *
     *                                                                        *
     * Getter. Returns isHuman. Takes no arguments                            *
     *************************************************************************/
    public boolean isHuman() { return isHuman; }

    /**************************************************************************
     * pullFromBoneyard                                                       *
     *                                                                        *
     * Takes random Domino from boneyard and gives it to hand                 *
     * Takes no arguments, returns nothing                                    *
     *************************************************************************/
    public void pullFromBoneyard(){
        int rand = (int)(Math.random()*boneyard.size());
        hand.add(boneyard.remove(rand));
    }

    /**************************************************************************
     * toString                                                               *
     *                                                                        *
     * Returns String representation of Player's name. Takes no arguments     *
     *************************************************************************/
    public String toString(){ return "Player " + name; }

    /**************************************************************************
     * getHand                                                                *
     *                                                                        *
     * Return String representation of hand. Takes no arguments               *
     *************************************************************************/
    public String getHand(){
        String handRep = "";
        for(Domino d : hand){
            handRep += d.toString()+" ";
        }
        return handRep;
    }

    /**************************************************************************
     * getHandGUI                                                             *
     *                                                                        *
     * Getter. Returns hand for use in the GUI. Takes no arguments            *
     *************************************************************************/
    public List<Domino> getHandGUI(){ return hand; }

    /**************************************************************************
     * getDomino                                                              *
     *                                                                        *
     * Getter. Returns Domino i in hand. Takes argument i                     *
     *************************************************************************/
    public Domino getDomino(int i){ return hand.get(i); }

    /**************************************************************************
     * handEmpty                                                              *
     *                                                                        *
     * Getter. Returns true if hand is empty. Takes no arguments              *
     *************************************************************************/
    public boolean handEmpty(){ return hand.size() == 0; }

    /**************************************************************************
     * getMoveError                                                           *
     *                                                                        *
     * Getter. Returns moveError. Takes no arguments                          *
     *************************************************************************/
    public String getMoveError(){ return moveError; }

    /**************************************************************************
     * setMoveError                                                           *
     *                                                                        *
     * Setter. Takes argument err and sets moveError to err. Returns nothing  *
     *************************************************************************/
    public void setMoveError(String err){ moveError = err; }

    /**************************************************************************
     * resetMoveError                                                         *
     *                                                                        *
     * Getter. Resets moveError to empty String.                              *
     * Takes no arguments, returns nothing                                    *
     *************************************************************************/
    public void resetMoveError(){ moveError = ""; }

    /**************************************************************************
     * getBoneyardSize                                                        *
     *                                                                        *
     * Getter. Returns the size of boneyard. Takes no arguments               *
     *************************************************************************/
    public int getBoneyardSize(){ return boneyard.size(); }

    /**************************************************************************
     * getPlayerTrainsSize                                                    *
     *                                                                        *
     * Getter. Returns the size of playerTrains. Takes no arguments           *
     *************************************************************************/
    public int getPlayerTrainsSize(){ return playerTrains.size(); }

    /**************************************************************************
     * setTrainMarked                                                         *
     *                                                                        *
     * Setter. Sets this player's train as marked.                            *
     * Takes no arguments, returns nothing                                    *
     *************************************************************************/
    public void setTrainMarked(){ trainMarked.set(name, 1); }

    /**************************************************************************
     * validMove                                                              *
     *                                                                        *
     * Checks proposed move to see if it is valid with the rules of the game. *
     * Sends out error message for humans if a move is invalid so they know   *
     * why it failed. If it is valid, completes the move                      *
     *                                                                        *
     * Arguments: domInd - index of domino in hand that they want to play     *
     *            trainInd - index of the train that they want to play on. 0  *
     *                       means Mexican Train, all other numbers represent *
     *                       the name of the player whose train is being used *
     * Returns true if the move was made, and false if it was invalid.        *
     *                                                                        *
     * Variables:                                                             *
     * train - train that the player wants to play their domino on            *
     * domino - domino that the player wants to play                          *
     * caboose - last domino held by train and/or the domino the proposed move*
     *           must be compared to                                          *
     *************************************************************************/
    public boolean validMove(int domInd, int trainInd){
        List<Domino> train;
        Domino domino = hand.get(domInd);
        Domino caboose;
        if(trainInd == 0){
            train = mexicanTrain;
            caboose = train.get(train.size()-1);
        }
        else{
            train = playerTrains.get(trainInd-1);
            if(train.size() == 0){
                if(trainInd == name){ caboose = mexicanTrain.get(0); }
                else{
                    if(isHuman){ startTrainError(); }
                    return false;
                }
            }
            else{ caboose = train.get(train.size()-1); }
            if(trainMarked.get(trainInd) == 0 && trainInd != name){
                if(isHuman){ unmarkedTrainError(); }
                return false;
            }
        }
        if(!caboose.isDouble() && trainMarked.get(0) == 1){
            if(isHuman){ openDoubleError(); }
            return false;
        }
        if(domino.getLeft() == caboose.getRight()){
            if(trainInd == name){ trainMarked.set(trainInd, 0); }
            train.add(hand.remove(domInd));
            return true;
        }
        else{ return false; }
    }

    /**************************************************************************
     * checkOpenDouble                                                        *
     *                                                                        *
     * Updates index 0 of trainMarked to reflect whether there is an open     *
     * on the field. 0 means no open double, 1 means there is one             *
     * Takes no arguments, returns nothing                                    *
     *************************************************************************/
    public void checkOpenDouble(){
        trainMarked.set(0, 0);
        if(mexicanTrain.get(mexicanTrain.size()-1).isDouble()){
            trainMarked.set(0, 1);
        }
        for(List<Domino> pTrain : playerTrains){
            if(pTrain.size() != 0){
                if(pTrain.get(pTrain.size()-1).isDouble()){
                    trainMarked.set(0, 1);
                }
            }
        }
    }

    /**************************************************************************
     * unmarkedTrainError                                                     *
     *                                                                        *
     * Prints error or sets moveError for case that player is trying to play  *
     * on an unmarked train. Takes no arguments, returns nothing              *
     *************************************************************************/
    private void unmarkedTrainError(){
        if(consoleGame){
            System.out.println("This player's train isn't marked!");
        }
        else{ moveError = "This player's train isn't marked!"; }
    }

    /**************************************************************************
     * openDoubleError                                                        *
     *                                                                        *
     * Prints error or sets moveError for case that player is trying to play  *
     * while there is an open double. Takes no arguments, returns nothing     *
     *************************************************************************/
    private void openDoubleError(){
        if(consoleGame){
            System.out.println("You have to handle the open double before " +
                    "you can play here.");
        }
        else{ moveError = "You have to handle the open double before you can" +
                " play here."; }
    }

    /**************************************************************************
     * startTrainError                                                        *
     *                                                                        *
     * Prints error or sets moveError for case that player is trying to start *
     * another player's train. Takes no arguments, returns nothing            *
     *************************************************************************/
    private void startTrainError(){
        if(consoleGame){
            System.out.println("You can't start another player's train!");
        }
        else{ moveError = "You can't start another player's train!"; }
    }
}
