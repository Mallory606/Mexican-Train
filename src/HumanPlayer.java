import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HumanPlayer extends Player{
    public HumanPlayer(int n, boolean console){ super(n, console); }

    @Override
    public void makeMove(){
        if(consoleGame){
            try { makeMoveConsole(); }
            catch(IOException e){ e.printStackTrace(); }
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
            if(input.equals("p")){ turnOver = playDomino(in); }
            else if(input.equals("d")){
                if(!pulled){
                    pullFromBoneyard();
                    pulled = true;
                }
                else{
                    trainMarked.set(name, 1);
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
                if(numInput > -1 && numInput < hand.size()){
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
                hand.get(dominoInd).flip();
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
                    if(numInput > 0 && numInput <= playerTrains.size()){
                        if(trainMarked.get(numInput) == 1 || numInput == name){
                            trainInd = numInput;
                            validInput = true;
                        }
                        else{
                            System.out.println("You can't play on that train!");
                        }
                    }
                    else{ System.out.println("Invalid input! Try Again!"); }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Try Again!");
                }
            }
        }
        dom = hand.get(dominoInd);
        moveMade = validMove(dominoInd, trainInd);
        if(moveMade && dom.isDouble()){ moveMade = playDomino(in); }
        return moveMade;
    }
}
