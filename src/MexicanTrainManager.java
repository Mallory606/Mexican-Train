import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MexicanTrainManager{
    //Value 0 of trainMarked actually keeps track of whether there is an open double
    private List<Domino> boneyard;
    private List<Player> players;
    private List<Domino> mexicanTrain;
    private List<List<Domino>> playerTrains;
    private List<Integer> trainMarked;
    private boolean consoleGame;
    private boolean gameRunning;
    private int numPlayers;
    private int currPlayer;
    private int round;

    public MexicanTrainManager(boolean console){
        consoleGame = console;
        round = 9;
        initializeBoneyard(round);
        if(consoleGame){
            try{ consoleGame(); }
            catch(IOException e){ e.printStackTrace(); }
        }
    }

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

    private void initializePlayers(int numComputers){
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

    private void printGameState(){
        System.out.println("GameState:\n");
        for(int i = 0; i < numPlayers; i++){
            System.out.print(players.get(i).toString() + ": ");
            if(trainMarked.get(i+1) == 1){ System.out.println("* "); }
            printTrain(playerTrains.get(i), false);
            System.out.println("\n");
        }
        System.out.print("Mexican Train: ");
        printTrain(mexicanTrain, true);
        System.out.println("\n");
    }

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
        System.out.println(line1);
        System.out.print(line2);
    }

    private boolean roundOver(){
        String winner;
        for(Player p : players){
            if(p.handEmpty()){
                winner = tallyScores();
                if(consoleGame){
                    System.out.println("Round over!");
                    if(round == 0){ System.out.println(winner + " wins!"); }
                    else{ System.out.println(winner + " is in the lead!"); }
                }
                return true;
            }
        }
        return false;
    }

    private String tallyScores(){
        int max = 0;
        String winner = "";
        for(Player p : players){
            p.updateScore();
            if(p.getScore() > max){ winner = p.toString(); }
        }
        return winner;
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
        /*for(int i = 0; i < numPlayers; i++){
            System.out.println("Player "+(i+1)+": "+players.get(i).getHand());
        }*/
        currPlayer = 0;
        gameRunning = true;
        while(gameRunning){
            printGameState();
            System.out.println("It's " + players.get(currPlayer).toString()
                    + "'s turn!\n");
            players.get(currPlayer).makeMove();
            if(currPlayer == numPlayers-1){ currPlayer = 0; }
            else{ currPlayer++; }
            if(roundOver()){
                if(round == 0){ gameRunning = false; }
                else{
                    initializeBoneyard(--round);
                    for(Player p : players){
                        p.newHand();
                    }
                    initializeHands();
                }
            }
        }
        System.out.println("GAME OVER");
    }
}
