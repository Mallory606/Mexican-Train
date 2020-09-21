import java.util.ArrayList;
import java.util.List;

public abstract class Player{
    private int name;
    private List<Domino> hand;
    private int score;
    private List<Domino> mexicanTrain;
    private List<List<Domino>> playerTrains;
    private List<Integer> trainMarked;

    public Player(int n){
        name = n;
        hand = new ArrayList<>();
        score = 0;
    }

    public abstract void makeMove();

    public void giveTrains(List<Domino> mexTrain, List<List<Domino>> plTrain,
                           List<Integer> trainMrkd){
        mexicanTrain = mexTrain;
        playerTrains = plTrain;
        trainMarked = trainMrkd;
    }

    public void dealDomino(Domino d){ hand.add(d); }

    public Domino playDomino(int i){ return hand.remove(i); }

    public int getScore(){ return score; }

    public void updateScore(int update){ score += update; }

    public String toString(){ return "Player " + name; }

    public String getHand(){
        String handRep = "";
        for(Domino d : hand){
            handRep += d.toString()+" ";
        }
        return handRep;
    }
}
