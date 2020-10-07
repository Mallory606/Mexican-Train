/******************************************************************************
 * Ashley Krattiger                                                           *
 *                                                                            *
 * Domino                                                                     *
 *                                                                            *
 * Holds information on the dots a Domino has and handles changes             *
 *****************************************************************************/
public class Domino{
    /**************************************************************************
     * Global Variables:                                                      *
     * left - number of dots on the left of the domino                        *
     * right - number of dots on the right of the domino                      *
     *************************************************************************/
    private int left;
    private int right;

    /**************************************************************************
     * Domino Constructor                                                     *
     *                                                                        *
     * Initializes left and right with arguments el and r                     *
     *************************************************************************/
    public Domino(int el, int r){
        left = el;
        right = r;
    }

    /**************************************************************************
     * getLeft                                                                *
     *                                                                        *
     * Getter. Returns left. Takes no arguments                               *
     *************************************************************************/
    public int getLeft(){ return left; }

    /**************************************************************************
     * getRight                                                               *
     *                                                                        *
     * Getter. Returns right. Takes no arguments                              *
     *************************************************************************/
    public int getRight(){ return right; }

    /**************************************************************************
     * flip                                                                   *
     *                                                                        *
     * Switches values of left and right. Takes no arguments, returns nothing *
     *                                                                        *
     * Variable:                                                              *
     * temp - holds value of left while it is being changed                   *
     *************************************************************************/
    public void flip(){
        int temp = left;
        left = right;
        right = temp;
    }

    /**************************************************************************
     * toString                                                               *
     *                                                                        *
     * Returns String representation of the domino. Takes no arguments        *
     *************************************************************************/
    public String toString(){ return "[ "+left+" | "+right+" ]"; }

    /**************************************************************************
     * isDouble                                                               *
     *                                                                        *
     * Returns true if left is the same number as right. Takes no arguments   *
     *************************************************************************/
    public boolean isDouble(){ return left == right; }

    /**************************************************************************
     * getScoreTotal                                                          *
     *                                                                        *
     * Returns total points (dots) the domino has. Takes no arguments         *
     *************************************************************************/
    public int getScoreTotal(){ return left + right; }
}
