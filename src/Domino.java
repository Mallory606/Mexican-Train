public class Domino{
    private final int left;
    private final int right;

    public Domino(int l, int r){
        left = l;
        right = r;
    }

    public int getLeft(){ return left; }

    public int getRight(){ return right; }

    public String toString(){ return "[ "+left+" | "+right+" ]"; }

    public boolean isDouble(){ return left == right; }
}
