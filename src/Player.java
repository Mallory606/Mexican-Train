import java.util.ArrayList;
import java.util.List;

public abstract class Player{
    private List<Domino> hand;
    private int score;

    public Player(){
        hand = new ArrayList<>();
        score = 0;
    }

    public abstract void makeMove();

    public void dealDomino(Domino d){ hand.add(d); }

    public Domino playDomino(int i){ return hand.get(i); }

    public int getScore(){ return score; }

    public void updateScore(int update){ score += update; }
}
