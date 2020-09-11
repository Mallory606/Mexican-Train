import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MexicanTrainManager{
    private List<Domino> boneyard;
    private List<Player> players;
    private List<Domino> mexicanTrain;
    private List<List<Domino>> playerTrains;
    private boolean consoleGame;
    private int numPlayers;

    public MexicanTrainManager(boolean console){
        consoleGame = console;
        initializeBoneyard();
        if(consoleGame){
            try{ consoleGame(); }
            catch(IOException e){ e.printStackTrace(); }
        }
    }

    private void initializeBoneyard(){
        boneyard = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                boneyard.add(new Domino(i, j));
            }
        }
    }

    private Domino pullFromBoneyard(){
        int rand = (int)(Math.random()*boneyard.size());
        return boneyard.remove(rand);
    }

    private void consoleGame() throws IOException{
        BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
        String input;
        int numberInput;
        System.out.println("Let's play Mexican Train!");
        System.out.println("How many players?");
        input = in.readLine();
        numberInput = Integer.parseInt(input);
        numPlayers = numberInput;
        System.out.println(numberInput + " players!");
    }
}
