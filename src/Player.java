import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public abstract class Player{
    public int name;
    public List<Domino> hand;
    private int score;
    private List<Domino> boneyard;
    public List<Domino> mexicanTrain;
    public List<List<Domino>> playerTrains;
    public List<Integer> trainMarked;

    public Player(int n){
        name = n;
        hand = new ArrayList<>();
        score = 0;
    }

    public abstract void makeMove();

    public void giveTrains(List<Domino> boneYd, List<Domino> mexTrain,
                           List<List<Domino>> plTrain,List<Integer> trainMrkd){
        boneyard = boneYd;
        mexicanTrain = mexTrain;
        playerTrains = plTrain;
        trainMarked = trainMrkd;
    }

    public void dealDomino(Domino d){ hand.add(d); }

    //public Domino playDomino(int i){ return hand.remove(i); }

    public int getScore(){ return score; }

    public void updateScore(int update){ score += update; }

    public void pullFromBoneyard(){
        int rand = (int)(Math.random()*boneyard.size());
        hand.add(boneyard.remove(rand));
    }

    public String toString(){ return "Player " + name; }

    public String getHand(){
        String handRep = "";
        for(Domino d : hand){
            handRep += d.toString()+" ";
        }
        return handRep;
    }
}
