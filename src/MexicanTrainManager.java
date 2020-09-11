import java.util.ArrayList;
import java.util.List;

public class MexicanTrainManager{
    private List<Domino> boneyard;
    private List<Player> players;

    public MexicanTrainManager(){
        initializeBoneyard();
    }

    private void initializeBoneyard(){
        boneyard = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                boneyard.add(new Domino(i, j));
            }
        }
    }

    private Domino pullFromBoneyard(){
        int rand = (int)(Math.random()*boneyard.size());
        return boneyard.remove(rand);
    }
}
