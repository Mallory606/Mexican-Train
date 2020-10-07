/******************************************************************************
 * Ashley Krattiger                                                           *
 *                                                                            *
 * MexicanTrainMaster                                                         *
 *                                                                            *
 * Initializes game based on the argument given upon starting the program.    *
 * Contains main                                                              *
 *****************************************************************************/
public class MexicanTrainMaster{
    /**************************************************************************
     * Main                                                                   *
     *                                                                        *
     * Runs the program. Takes one argument in the form of a String reading   *
     * either "console" or "GUI". This argument starts the game on the console*
     * if "console", and on GUI if "GUI". Returns nothing.                    *
     *                                                                        *
     * Variables:                                                             *
     * manager - the MexicanTrainManager for the console game                 *
     * window - the Display for the GUI game                                  *
     *************************************************************************/
    public static void main(String[] args){
        MexicanTrainManager manager;
        Display window;
        if(args[0].equals("console")){
            manager = new MexicanTrainManager(true);
        }
        else{
            window = new Display();
            window.startWindow(args);
        }
    }
}
