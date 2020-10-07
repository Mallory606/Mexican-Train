import java.util.PriorityQueue;

/******************************************************************************
 * Ashley Krattiger                                                           *
 *                                                                            *
 * ComputerPlayer                                                             *
 *                                                                            *
 * Extends Player. Handles move making for computers                          *
 *****************************************************************************/
public class ComputerPlayer extends Player{
    /**************************************************************************
     * Player Constructor                                                     *
     *                                                                        *
     * Calls super(). Initializes global variables                            *
     *                                                                        *
     * Argument: n - the name of the Player                                   *
     *           console - tells us if the game is a console game             *
     *************************************************************************/
    public ComputerPlayer(int n, boolean console){ super(n, console, false); }

    /**************************************************************************
     * makeMove                                                               *
     *                                                                        *
     * Overridden method. Completes a turn for the Player                     *
     *                                                                        *
     * Takes no arguments, returns nothing                                    *
     * Catches IOException                                                    *
     *                                                                        *
     * Variable:                                                              *
     * validMove - keeps track of whether proposed move is valid              *
     * pulled - keeps track of whether they pulled from the boneyard          *
     * pass - keeps track of whether the computer has passed for this turn    *
     * domInd - holds the index in hand of the Domino they want to play       *
     * dom - Domino which the user wants to play. Held so method can check if *
     *       a double Domino was successfully played                          *
     * moveQueue - organizes the Dominos in hand in order of what would be the*
     *             most advantageous move for the computer to make            *
     *************************************************************************/
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
