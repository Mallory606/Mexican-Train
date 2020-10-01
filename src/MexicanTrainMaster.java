public class MexicanTrainMaster{
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
