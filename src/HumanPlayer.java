import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HumanPlayer extends Player{
    private int dominoInd;
    private int trainInd;

    public HumanPlayer(int n, boolean console){
        super(n, console, true);
        dominoInd = -1;
        trainInd = -1;
    }

    public void setDominoInd(int dom){ dominoInd = dom; }

    public void setTrainInd(int train){ trainInd = train; }

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

    private boolean playDomino(BufferedReader in) throws IOException{
        String input;
        int numInput;
        int dominoInd = 0;
        int trainInd = -1;
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
