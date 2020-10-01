import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;


public class Display extends javafx.application.Application{
    private Stage setPlayers;
    private ChoiceBox<Integer> numCompsBox;
    private MexicanTrainManager manager;
    private Integer numPlayers;
    private Integer numComps;
    private List<Domino> boneyard;
    private List<Player> players;
    private List<Domino> mexicanTrain;
    private List<List<Domino>> playerTrains;
    private List<Integer> trainMarked;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Mexican Train");

        setPlayers = new Stage();
        setPlayers.initModality(Modality.APPLICATION_MODAL);
        setPlayers.initOwner(primaryStage);
        setPlayers.setAlwaysOnTop(true);
        setPlayers.setTitle("Select Number of Players");
        VBox setPlayersLists = new VBox(10);
        setPlayersLists.setAlignment(Pos.CENTER);
        HBox numPlayersHBox = new HBox(5);
        Label numPlayersLabel = new Label("Number of Players:");
        Integer[] numPlayersList = {2, 3, 4};
        ChoiceBox<Integer> numPlayersBox =
            new ChoiceBox<>(FXCollections.observableArrayList(numPlayersList));
        numPlayersHBox.getChildren().addAll(numPlayersLabel, numPlayersBox);
        HBox numCompsHBox = new HBox(5);
        Label numCompsLabel = new Label("Number of Computers:");
        numCompsBox = new ChoiceBox<>();
        numCompsHBox.getChildren().addAll(numCompsLabel, numCompsBox);
        numPlayersBox.getSelectionModel().selectedIndexProperty()
                .addListener((observable, oldValue, newValue) -> {
            Integer[] numCompsList;
            if((Integer)newValue == 0){ numCompsList = new Integer[]{0, 1}; }
            else if((Integer)newValue == 1){ numCompsList=new Integer[]{0,1,2};}
            else{ numCompsList = new Integer[]{0, 1, 2, 3}; }
            numCompsBox.setItems(FXCollections.observableArrayList(numCompsList));
        });
        Button start = new Button("Start");
        start.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            numPlayers = numPlayersBox.getValue();
            numComps = numCompsBox.getValue();
            if(numPlayers != null && numComps != null){
                manager = new MexicanTrainManager(false);
                setPlayers.close();
            }
        });
        setPlayersLists.getChildren().addAll(numPlayersHBox,numCompsHBox,start);
        Scene setPlayersScene = new Scene(setPlayersLists, 200, 115);
        setPlayers.setScene(setPlayersScene);
        setPlayers.show();


        initializeGame();


        BorderPane border = new BorderPane();

        Scene scene = new Scene(border, 1000, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer a = new AnimationTimer(){
            @Override
            public void handle(long now){

            }
        };
        a.start();
    }

    /**************************************************************************
     * startWindow()                                                          *
     *                                                                        *
     * Wrapper class that allows MexicanTrainMaster to launch the window.     *
     *                                                                        *
     * Argument: args - args passed from main                                 *
     * Returns nothing                                                        *
     *************************************************************************/
    public void startWindow(String[] args){ launch(args); }

    private void initializeGame(){
        manager.setNumPlayers(numPlayers);
        manager.initializePlayers(numComps);
        manager.setCurrPlayer(0);
        boneyard = manager.getBoneyard();
        players = manager.getPlayers();
        mexicanTrain = manager.getMexicanTrain();
        playerTrains = manager.getPlayerTrains();
        trainMarked = manager.getTrainMarked();
    }
}
