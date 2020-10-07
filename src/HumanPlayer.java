import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/******************************************************************************
 * Ashley Krattiger                                                           *
 *                                                                            *
 * HumanPlayer                                                                *
 *                                                                            *
 * Extends Player. Handles move making for human users                        *
 *****************************************************************************/
public class HumanPlayer extends Player{
    /**************************************************************************
     * Global Variables:                                                      *
     * dominoInd - index in hand of the domino the user wants to play         *
     * trainInd - index of train that user wants to play on. 0 if Mexican     *
     *            Train, any other value is int representation of Player whose*
     *            train they want to play on                                  *
     *************************************************************************/
    private int dominoInd;
    private int trainInd;

    /**************************************************************************
     * Player Constructor                                                     *
     *                                                                        *
     * Calls super(). Initializes global variables                            *
     *                                                                        *
     * Argument: n - the name of the Player                                   *
     *           console - tells us if the game is a console game             *
     *************************************************************************/
    public HumanPlayer(int n, boolean console){
        super(n, console, true);
        dominoInd = -1;
        trainInd = -1;
    }

    /**************************************************************************
     * setDominoInd                                                           *
     *                                                                        *
     * Setter. Sets dominoInd to argument dom. Returns nothing                *
     *************************************************************************/
    public void setDominoInd(int dom){ dominoInd = dom; }

    /**************************************************************************
     * setTrainInd                                                            *
     *                                                                        *
     * Setter. Sets trainInd to argument train. Returns nothing               *
     *************************************************************************/
    public void setTrainInd(int train){ trainInd = train; }

    /**************************************************************************
     * makeMove                                                               *
     *                                                                        *
     * Overridden method. Completes a turn for the Player                     *
     *                                                                        *
     * Takes no arguments, returns nothing                                    *
     * Catches IOException                                                    *
     *                                                                        *
     * Variable:                                                              *
     * moveMade - keeps track of whether proposed move in GUI game is valid   *
     *************************************************************************/
    @Override
    public void makeMove(){
        boolean moveMade;
        if(isConsoleGame()){
            try { makeMoveConsole(); }
            catch(IOException e){ e.printStackTrace(); }
        }
        else{
            resetMoveError();
            moveMade = validMove(dominoInd, trainInd);
            checkOpenDouble();
            if(!moveMade){ setMoveError("Invalid move! Try again!"); }
        }
    }

    /**************************************************************************
     * makeMoveConsole                                                        *
     *                                                                        *
     * Takes input from user and prints out options for player on their move  *
     *                                                                        *
     * Takes no arguments, returns nothing                                    *
     * Throws IOException                                                     *
     *                                                                        *
     * Variable:                                                              *
     * in - BufferedReader which communicates with System.in                  *
     * input - holds input read in from System.in                             *
     * turnOver - keeps track of whether the user is done with their turn     *
     * pulled - keeps track of whether the user has pulled from the boneyard  *
     *************************************************************************/
    private void makeMoveConsole() throws IOException{
        BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
        String input;
        boolean turnOver = false;
        boolean pulled = false;
        while(!turnOver){
            System.out.println("Hand: " + getHand());
            System.out.println("[p] play domino");
            if(!pulled){ System.out.println("[d] draw from boneyard"); }
            else{ System.out.println("[d] pass"); }
            input = in.readLine();
            if(input.equals("p")){
                turnOver = playDomino(in);
                if(!turnOver){ System.out.println("Invalid move! Try again!");}
            }
            else if(input.equals("d")){
                if(!pulled && getBoneyardSize() > 0){
                    pullFromBoneyard();
                    pulled = true;
                }
                else{
                    setTrainMarked();
                    turnOver = true;
                }
            }
            else{ System.out.println("Invalid input! Try again!"); }
        }
    }

    /**************************************************************************
     * playDomino                                                             *
     *                                                                        *
     * Takes input from user for which domino they want to play and attempts  *
     * to make the move                                                       *
     *                                                                        *
     * Argument: in - BufferedReader that takes input from System.in          *
     * Returns true if the user succeeded in making a move                    *
     * Throws IOException                                                     *
     *                                                                        *
     * Variable:                                                              *
     * input - holds input read in from System.in                             *
     * numInput - holds numbers parsed from input                             *
     * dom - Domino which the user wants to play. Held so method can check if *
     *       a double Domino was successfully played                          *
     * validInput - keeps track of whether the method has read in a valid move*
     * moveMade - keeps track of whether the user's move was successfully made*
     *************************************************************************/
    private boolean playDomino(BufferedReader in) throws IOException{
        String input;
        int numInput;
        Domino dom;
        boolean validInput = false;
        boolean moveMade;
        while(!validInput){
            System.out.println("Which domino would you like to play?");
            input = in.readLine();
            try {
                numInput = Integer.parseInt(input);
                if(numInput > -1 && numInput < handSize()){
                    dominoInd = numInput;
                    validInput = true;
                }
                else{ System.out.println("Invalid input! Try Again!");}
            } catch(NumberFormatException e){
                System.out.println("Invalid input! Try Again!");
            }
        }
        validInput = false;
        while(!validInput){
            System.out.println("Would you like to flip " +
                    "the domino? [y/n]");
            input = in.readLine();
            if(input.equals("y")){
                fromHand(dominoInd).flip();
                validInput = true;
            }
            else if(input.equals("n")){ validInput = true; }
            else{ System.out.println("Invalid input! Try again!"); }
        }
        validInput = false;
        while(!validInput){
            System.out.println("Which train would you like to play on?");
            System.out.println("(Choose Mexican or any player's number)");
            input = in.readLine();
            if(input.toLowerCase().equals("mexican")){
                trainInd = 0;
                validInput = true;
            }
            else{
                try {
                    numInput = Integer.parseInt(input);
                    if(numInput > 0 && numInput <= getPlayerTrainsSize()){
                        trainInd = numInput;
                        validInput = true;
                    }
                    else{ System.out.println("Invalid input! Try Again!"); }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Try Again!");
                }
            }
        }
        dom = fromHand(dominoInd);
        moveMade = validMove(dominoInd, trainInd);
        checkOpenDouble();
        if(moveMade && dom.isDouble()){
            System.out.println("Play again!");
            makeMoveConsole();
        }
        return moveMade;
    }
}
