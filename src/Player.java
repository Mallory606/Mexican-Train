import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public abstract class Player{
    public int name;
    public List<Domino> hand;
    private int score;
    private boolean consoleGame;
    private List<Domino> boneyard;
    public List<Domino> mexicanTrain;
    public List<List<Domino>> playerTrains;
    public List<Integer> trainMarked;

    public Player(int n, boolean console){
        name = n;
        consoleGame = console;
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

    public boolean validMove(int domInd, int trainInd){
        List<Domino> train = playerTrains.get(trainInd);
        Domino domino = hand.get(domInd);
        Domino caboose = train.get(train.size()-1);
        if(!caboose.isDouble() && trainMarked.get(0) == 1){
            openDoubleError();
            return false;
        }
        if(domino.getLeft() == caboose.getRight()){
            if(caboose.isDouble()){ trainMarked.set(0, 0); }
            if(domino.isDouble()){ trainMarked.set(0, 1); }
            train.add(hand.remove(domInd));
            return true;
        }
        else{ return false; }
    }

    private void openDoubleError(){
        if(consoleGame){
            System.out.println("You have to handle the open double before " +
                    "you can play here.");
        }
    }
}
