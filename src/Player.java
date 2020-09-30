import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public abstract class Player{
    public int name;
    public List<Domino> hand;
    private int score;
    public boolean consoleGame;
    private final boolean isHuman;
    private List<Domino> boneyard;
    public List<Domino> mexicanTrain;
    public List<List<Domino>> playerTrains;
    public List<Integer> trainMarked;

    public Player(int n, boolean console, boolean human){
        name = n;
        consoleGame = console;
        hand = new ArrayList<>();
        score = 0;
        isHuman = human;
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

    public void newHand(){ hand = new ArrayList<>(); }

    public int getScore(){ return score; }

    public void updateScore(){
        int update = 0;
        for(Domino d : hand){ update += d.getScoreTotal(); }
        score += update;
    }

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

    public boolean handEmpty(){ return hand.size() == 0; }

    public boolean validMove(int domInd, int trainInd){
        List<Domino> train;
        Domino domino = hand.get(domInd);
        Domino caboose;
        if(trainInd == 0){ train = mexicanTrain; }
        else{
            train = playerTrains.get(trainInd-1);
            if(train.size() == 0){
                if(trainInd == name){
                    train.add(hand.remove(domInd));
                    return true;
                }
                else{
                    if(isHuman){ startTrainError(); }
                    return false;
                }
            }
        }
        caboose = train.get(train.size()-1);
        if(!caboose.isDouble() && trainMarked.get(0) == 1){
            if(isHuman){ openDoubleError(); }
            return false;
        }
        if(domino.getLeft() == caboose.getRight()){
            if(trainInd == name){ trainMarked.set(trainInd, 0); }
            train.add(hand.remove(domInd));
            return true;
        }
        else{ return false; }
    }

    public void checkOpenDouble(){
        trainMarked.set(0, 0);
        if(mexicanTrain.get(mexicanTrain.size()-1).isDouble()){
            trainMarked.set(0, 1);
        }
        for(List<Domino> d : playerTrains){
            if(d.size() != 0){
                if(d.get(d.size()-1).isDouble()){ trainMarked.set(0, 1); }
            }
        }
    }

    private void openDoubleError(){
        if(consoleGame){
            System.out.println("You have to handle the open double before " +
                    "you can play here.");
        }
    }

    private void startTrainError(){
        if(consoleGame){
            System.out.println("You can't start another player's train!");
        }
    }
}
