import java.io.BufferedReader;
import java.util.Comparator;
import java.util.PriorityQueue;

public class ComputerPlayer extends Player{
    public ComputerPlayer(int n, boolean console){ super(n, console, 0); }

    @Override
    public void makeMove(){
        boolean validMove = false;
        boolean pulled = false;
        boolean pass = false;
        Domino dom;
        PriorityQueue<Domino> moveQueue = new PriorityQueue<>((o1, o2) -> {
            if(o1.isDouble() && !o2.isDouble()){ return 1; }
            else if(o2.isDouble() && !o1.isDouble()){ return -1; }
            else{ return o1.getScoreTotal() - o2.getScoreTotal(); }
        });

        moveQueue.addAll(hand);
        while(!validMove && !pass){
            dom = moveQueue.poll();
            if(dom == null){
                if(!pulled){
                    pullFromBoneyard();
                    pulled = true;
                }
                else{
                    trainMarked.set(name, 1);
                    pass = true;
                }
            }
            for(int i = 0; i <= playerTrains.size(); i++){
                //validMove = validMove();
            }
        }
    }
}
