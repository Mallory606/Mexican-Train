import java.util.PriorityQueue;

public class ComputerPlayer extends Player{
    public ComputerPlayer(int n, boolean console){ super(n, console, false); }

    @Override
    public void makeMove(){
        boolean validMove = false;
        boolean pulled = false;
        boolean pass = false;
        Integer domInd;
        PriorityQueue<Integer> moveQueue = new PriorityQueue<>((o1, o2) -> {
            Domino dom1 = hand.get(o1);
            Domino dom2 = hand.get(o2);
            if(dom1.isDouble() && !dom2.isDouble()){ return -1; }
            else if(dom2.isDouble() && !dom1.isDouble()){ return 1; }
            else{ return dom2.getScoreTotal() - dom1.getScoreTotal(); }
        });

        System.out.println(getHand());
        for(int i = 0; i < hand.size(); i++){ moveQueue.add(i); }
        while(!validMove && !pass){
            domInd = moveQueue.poll();
            if(domInd == null){
                if(!pulled){
                    pullFromBoneyard();
                    moveQueue.add(hand.size()-1);
                    pulled = true;
                }
                else{
                    trainMarked.set(name, 1);
                    pass = true;
                }
            }
            else{
                for(int j = 0; j <= playerTrains.size(); j++){
                    validMove = validMove(domInd, j);
                    if(validMove){ break; }
                    hand.get(domInd).flip();
                    validMove = validMove(domInd, j);
                    if(validMove){ break; }
                }
            }
        }
    }
}
