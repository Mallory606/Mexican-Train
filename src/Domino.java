public class Domino{
    private int left;
    private int right;

    public Domino(int l, int r){
        left = l;
        right = r;
    }

    public int getLeft(){ return left; }

    public int getRight(){ return right; }

    public void flip(){
        int temp = left;
        left = right;
        right = temp;
    }

    public String toString(){ return "[ "+left+" | "+right+" ]"; }

    public boolean isDouble(){ return left == right; }

    public int getScoreTotal(){ return left + right; }
}
