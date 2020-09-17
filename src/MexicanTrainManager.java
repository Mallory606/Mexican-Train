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
                if(i == 9 && j == 9) {
                    mexicanTrain = new ArrayList<>();
                    mexicanTrain.add(new Domino(9, 9));
                }
                else{ boneyard.add(new Domino(i, j)); }
            }
        }
    }

    private void initializePlayers(int numComputers){
        players = new ArrayList<>();
        playerTrains = new ArrayList<>();
        for(int i = 0; i < (numPlayers-numComputers); i++){
            players.add(new HumanPlayer());
            playerTrains.add(new ArrayList<>());
        }
        for(int j = 0; j < numComputers; j++){
            players.add(new ComputerPlayer());
            playerTrains.add(new ArrayList<>());
        }
        initializeHands();
    }

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

    private Domino pullFromBoneyard(){
        int rand = (int)(Math.random()*boneyard.size());
        return boneyard.remove(rand);
    }

    private void consoleGame() throws IOException{
        BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
        String input;
        int numberInput = 0;
        boolean validInput = false;
        System.out.println("Let's play Mexican Train!");
        while(!validInput){
            System.out.println("How many players?");
            input = in.readLine();
            numberInput = Integer.parseInt(input);
            if(numberInput > 1 && numberInput < 5){ validInput = true; }
            else{ System.out.println("Invalid input! Try again!"); }
        }
        numPlayers = numberInput;
        validInput = false;
        while(!validInput){
            System.out.println("How many computers?");
            input = in.readLine();
            numberInput = Integer.parseInt(input);
            if(numberInput <= numPlayers){ validInput = true; }
            else{ System.out.println("Invalid input! Try again!"); }
        }
        initializePlayers(numberInput);
        System.out.println("Starting game with " + numPlayers + " players and "
                + numberInput + " computers!");
        for(int i = 0; i < numPlayers; i++){
            System.out.println("Player "+(i+1)+": "+players.get(i).getHand());
        }
    }
}
