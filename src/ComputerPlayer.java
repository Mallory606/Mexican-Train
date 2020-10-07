import java.util.PriorityQueue;

public class ComputerPlayer extends Player{
    public ComputerPlayer(int n, boolean console){ super(n, console, false); }

    @Override
    public void makeMove(){
        boolean validMove = false;
        boolean pulled = false;
        boolean pass = false;
        Integer domInd;
        Domino dom;
        PriorityQueue<Integer> moveQueue = new PriorityQueue<>((o1, o2) -> {
            Domino dom1 = fromHand(o1);
            Domino dom2 = fromHand(o2);
            if(dom1.isDouble() && !dom2.isDouble()){ return -1; }
            else if(dom2.isDouble() && !dom1.isDouble()){ return 1; }
            else{ return dom2.getScoreTotal() - dom1.getScoreTotal(); }
        });

        //System.out.println(getHand());
        for(int i = 0; i < handSize(); i++){ moveQueue.add(i); }
        while(!validMove && !pass){
            domInd = moveQueue.poll();
            if(domInd == null){
                if(!pulled && getBoneyardSize() > 0){
                    pullFromBoneyard();
                    moveQueue.add(handSize()-1);
                    pulled = true;
                }
                else{
                    setTrainMarked();
                    pass = true;
                }
            }
            else{
                for(int j = 0; j <= getPlayerTrainsSize(); j++){
                    dom = fromHand(domInd);
                    validMove = validMove(domInd, j);
                    checkOpenDouble();
                    if(validMove){
                        if(dom.isDouble()){ makeMove(); }
                        break;
                    }
                    fromHand(domInd).flip();
                    validMove = validMove(domInd, j);
                    checkOpenDouble();
                    if(validMove){
                        if(dom.isDouble()){ makeMove(); }
                        break;
                    }
                }
            }
        }
    }
}
