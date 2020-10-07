import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.List;


public class Display extends javafx.application.Application{
    private Stage setPlayers;
    private ChoiceBox<Integer> numCompsBox;
    private MexicanTrainManager manager;
    private Label turnLabel;
    private Label scoreLabel;
    private Label boneyardLabel;
    private ChoiceBox<Integer> dominoChoice;
    private ChoiceBox<String> trainChoice;
    private Button drawBone;
    private Integer numPlayers;
    private Integer numComps;
    private List<Domino> boneyard;
    private List<Player> players;
    private List<Domino> mexicanTrain;
    private List<List<Domino>> playerTrains;
    private List<Integer> trainMarked;
    private Canvas gameBoard;
    private boolean gameRunning;
    private boolean newTurn;
    private boolean turnPassed;
    private boolean moveMade;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Mexican Train");

        gameRunning = false;
        newTurn = true;
        turnPassed = false;
        moveMade = false;

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
                initializeGame();
                gameRunning = true;
                setPlayers.close();
            }
        });
        setPlayersLists.getChildren().addAll(numPlayersHBox,numCompsHBox,start);
        Scene setPlayersScene = new Scene(setPlayersLists, 200, 115);
        setPlayers.setScene(setPlayersScene);
        setPlayers.show();


        gameBoard = new Canvas(2500, 500);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(gameBoard);
        scrollPane.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox userInterface = new VBox(10);
        turnLabel = new Label("   Player 1's Turn!   ");
        turnLabel.setFont(new Font(20));
        scoreLabel = new Label("Score: ");
        boneyardLabel = new Label(" left in Boneyard");
        Label playLabel = new Label("Play a Domino:");
        HBox dominoBox = new HBox(10);
        Label dominoLabel = new Label("Pick Domino:");
        dominoChoice = new ChoiceBox<>();
        dominoBox.getChildren().addAll(dominoLabel, dominoChoice);
        dominoBox.setAlignment(Pos.CENTER);
        HBox trainBox = new HBox(10);
        Label trainLabel = new Label("Pick Train:");
        trainChoice = new ChoiceBox<>();
        trainBox.getChildren().addAll(trainLabel, trainChoice);
        trainBox.setAlignment(Pos.CENTER);
        Button flip = new Button("Flip Domino");
        drawBone = new Button("Draw from Boneyard");
        Button playDom = new Button("Play Domino");
        userInterface.getChildren().addAll(turnLabel, scoreLabel,boneyardLabel,
                playLabel, dominoBox, trainBox, flip, drawBone, playDom);
        userInterface.setAlignment(Pos.TOP_CENTER);

        flip.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Integer currDom = dominoChoice.getValue();
            if(currDom != null){
                currDom -= 1;
                players.get(manager.getCurrPlayer()).getDomino(currDom).flip();
            }
        });
        drawBone.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Player currPlayer = players.get(manager.getCurrPlayer());
            if(turnPassed){
                trainMarked.set(manager.getCurrPlayer()+1, 1);
                turnPassed = false;
                moveMade = true;
            }
            else{
                if(boneyard.size() > 0){ currPlayer.pullFromBoneyard(); }
                newTurn = true;
                turnPassed = true;
            }
        });
        playDom.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            Integer currDom = dominoChoice.getValue();
            String train = trainChoice.getValue();
            HumanPlayer currPlayer = (HumanPlayer)players
                    .get(manager.getCurrPlayer());
            boolean playDouble;
            if(currDom != null && train != null){
                currPlayer.setDominoInd(currDom-1);
                switch(train){
                    case "Mexican":
                        currPlayer.setTrainInd(0);
                        break;
                    case "Player 1":
                        currPlayer.setTrainInd(1);
                        break;
                    case "Player 2":
                        currPlayer.setTrainInd(2);
                        break;
                    case "Player 3":
                        currPlayer.setTrainInd(3);
                        break;
                    default:
                        currPlayer.setTrainInd(4);
                        break;
                }
                playDouble = currPlayer.getDomino(currDom-1).isDouble();
                currPlayer.makeMove();
                if(!currPlayer.getMoveError().equals("")){
                    Stage errorMessage = new Stage();
                    errorMessage.initModality(Modality.APPLICATION_MODAL);
                    errorMessage.initOwner(primaryStage);
                    errorMessage.setAlwaysOnTop(true);
                    errorMessage.setTitle("Move Error");
                    VBox errorBox = new VBox(10);
                    Label error = new Label(currPlayer.getMoveError());
                    Button okay = new Button("Okay");
                    okay.addEventHandler(MouseEvent.MOUSE_CLICKED,
                            event1 -> errorMessage.close());
                    errorBox.getChildren().addAll(error, okay);
                    errorBox.setAlignment(Pos.CENTER);
                    Scene errorScene = new Scene(errorBox, 250, 100);
                    errorMessage.setScene(errorScene);
                    errorMessage.show();
                }
                else if(!playDouble){
                    turnPassed = false;
                    newTurn = true;
                    moveMade = true;
                }
                else{
                    newTurn = true;
                    turnPassed = false;
                }
            }
        });

        BorderPane border = new BorderPane();
        border.setCenter(scrollPane);
        border.setRight(userInterface);

        Scene scene = new Scene(border, 1000, 520);
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer a = new AnimationTimer(){
            @Override
            public void handle(long now){
                if(gameRunning){
                    updateGameState();
                    if(moveMade){
                        gameRunning = manager.guiGame();
                        updateGameState();
                        if(manager.isNewRound()){
                            initializeRound();
                            Stage newRound = new Stage();
                            newRound.initModality(Modality.APPLICATION_MODAL);
                            newRound.initOwner(primaryStage);
                            newRound.setAlwaysOnTop(true);
                            newRound.setTitle("New Round");
                            VBox newRoundBox = new VBox(10);
                            Label roundLabel = new Label("New Round! "
                                    +manager.getCurrWinner() + " is winning!");
                            Button okay = new Button("Okay");
                            okay.addEventHandler(MouseEvent.MOUSE_CLICKED,
                                    event1 -> newRound.close());
                            newRoundBox.getChildren().addAll(roundLabel, okay);
                            newRoundBox.setAlignment(Pos.CENTER);
                            Scene roundScene = new Scene(newRoundBox, 200, 100);
                            newRound.setScene(roundScene);
                            newRound.show();
                        }
                        newTurn = true;
                        moveMade = false;
                    }
                }
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
        initializeRound();

        String[] possTrains = new String[numPlayers+1];
        possTrains[0] = "Mexican";
        for(int i = 1; i <= numPlayers; i++){ possTrains[i] = "Player "+i; }
        trainChoice.setItems(FXCollections.observableArrayList(possTrains));
    }

    private void initializeRound(){
        manager.setCurrPlayer(0);
        boneyard = manager.getBoneyard();
        players = manager.getPlayers();
        mexicanTrain = manager.getMexicanTrain();
        playerTrains = manager.getPlayerTrains();
        trainMarked = manager.getTrainMarked();
        drawGameBoard();
        manager.resetNewRound();
    }

    private void updateGameState(){
        Player currPlayer = players.get(manager.getCurrPlayer());
        drawGameBoard();
        turnLabel.setText("   Player "+currPlayer.getName()+"'s Turn!   ");
        scoreLabel.setText("Score: " + currPlayer.getScore());
        boneyardLabel.setText(boneyard.size() + " left in Boneyard");

        if(newTurn){
            List<Domino> currHand = currPlayer.getHandGUI();
            Integer[] possDoms = new Integer[currHand.size()];
            for(int i = 0; i < currHand.size(); i++){ possDoms[i] = i + 1; }
            dominoChoice.setItems(FXCollections.observableArrayList(possDoms));
            newTurn = false;
        }
        if(turnPassed){ drawBone.setText("Pass"); }
        else{ drawBone.setText("Draw from Boneyard"); }
    }

    private void drawGameBoard(){
        GraphicsContext gc = gameBoard.getGraphicsContext2D();
        List<Domino> tempTrain;
        int tempX;
        int tempY;
        int addWidth;
        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, 2500, 500);   //each row = 80 tall, 20 for spacing (5 between)
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.GOLD);
        gc.setFont(new Font(20));
        gc.fillRoundRect(5, 5, 80, 80, 5, 5);
        gc.strokeRoundRect(5, 5, 80, 80, 5, 5);
        gc.strokeText("Mexican", 10, 40, 70);
        gc.strokeText("Train", 23, 65, 70);
        tempX = 95;
        for(Domino d : mexicanTrain){
            addWidth = drawDomino(d, gc, tempX, 5);
            tempX += addWidth + 5;
        }
        tempY = 0;
        for(int i = 0; i < numPlayers; i++){
            tempY += 83;
            gc.setFill(Color.GOLD);
            gc.fillRoundRect(5, tempY+5, 80, 80, 5, 5);
            gc.strokeRoundRect(5, tempY+5, 80, 80, 5, 5);
            gc.strokeText("Player", 18, tempY+40, 70);
            gc.strokeText(""+(i+1), 38, tempY+65, 70);
            if(trainMarked.get(i+1) == 1){
                gc.setFill(Color.FIREBRICK);
                gc.fillOval(60, tempY+60, 10, 10);
                gc.strokeOval(60, tempY+60, 10, 10);
            }
            tempTrain = playerTrains.get(i);
            tempX = 95;
            for(Domino d : tempTrain){
                addWidth = drawDomino(d, gc, tempX, tempY+5);
                tempX += addWidth + 5;
            }
        }
        gc.setFill(Color.GOLD);
        gc.fillRoundRect(5, 420, 80, 80, 5, 5);
        gc.strokeRoundRect(5, 420, 80, 80, 5, 5);
        gc.strokeText("Player", 18, 455, 70);
        gc.strokeText("Hand", 20, 475, 75);
        tempTrain = players.get(manager.getCurrPlayer()).getHandGUI();
        tempX = 95;
        for(Domino d : tempTrain){
            addWidth = drawDomino(d, gc, tempX, 420);
            tempX += addWidth + 5;
        }

        if(!gameRunning){
            gc.setFill(Color.WHITE);
            gc.fillRect(200, 200, 200, 200);
            gc.strokeRect(200, 200, 200, 200);
            gc.strokeText("GAME OVER\nPlayer "+
                    manager.getCurrWinner()+" wins!", 200, 200);
        }
    }

    //domino is 80x35, returns width of domino drawing
    private int drawDomino(Domino dom, GraphicsContext gc, int x, int y){
        gc.setFill(Color.IVORY);
        if(dom.isDouble()){
            gc.fillRoundRect(x, y, 35, 80, 15, 15);
            gc.setFill(Color.BLACK);
            gc.fillRoundRect(x+2, y+38, 32, 5, 15, 5);
            drawDots(dom.getLeft(), gc, x, y+2);
            drawDots(dom.getRight(), gc, x, y+45);
            return 35;
        }
        else{
            gc.fillRoundRect(x, y+20, 80, 35, 15, 15);
            gc.setFill(Color.BLACK);
            gc.fillRoundRect(x+38, y+22, 5, 32, 5, 15);
            drawDots(dom.getLeft(), gc, x+2, y+20);
            drawDots(dom.getRight(), gc, x+45, y+20);
            return 80;
        }
    }

    private void drawDots(int numDots, GraphicsContext gc, int x, int y){
        if(numDots == 4 || numDots == 5 || numDots == 6 || numDots == 7
                || numDots == 8 || numDots == 9){
            gc.fillOval(x+5, y+5, 5, 5);
            gc.fillOval(x+25, y+25, 5, 5);
        }
        if(numDots == 8 || numDots == 9){ gc.fillOval(x+15, y+5, 5, 5); }
        if(numDots == 2 || numDots == 3 || numDots == 4 || numDots == 5 ||
                numDots == 6 || numDots == 7 || numDots == 8 || numDots == 9){
            gc.fillOval(x+25, y+5, 5, 5);
            gc.fillOval(x+5, y+25, 5, 5);
        }
        if(numDots == 6 || numDots == 7 || numDots == 8 || numDots == 9){
            gc.fillOval(x+5, y+15, 5, 5);
        }
        if(numDots==1 || numDots==3 || numDots==5 || numDots==7 || numDots==9){
            gc.fillOval(x+15, y+15, 5, 5);
        }
        if(numDots == 6 || numDots == 7 || numDots == 8 || numDots == 9){
            gc.fillOval(x+25, y+15, 5, 5);
        }
        if(numDots == 8 || numDots == 9){ gc.fillOval(x+15, y+25, 5, 5); }
    }
}
