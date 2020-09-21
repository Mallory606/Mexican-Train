import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HumanPlayer extends Player{
    private boolean consoleGame;

    public HumanPlayer(int n, boolean console){
        super(n);
        consoleGame = console;
    }

    @Override
    public void makeMove(){
        if(consoleGame){
            try { makeMoveConsole(); }
            catch(IOException e){ e.printStackTrace(); }
        }
    }

    private void makeMoveConsole() throws IOException {
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
            if(input.equals("p")){}
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
}
