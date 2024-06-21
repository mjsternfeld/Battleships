package gui;

import game.Player;
import game.assets.Map;
import game.assets.ships.ShipType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import network.Client;
import network.client.Join;
import network.host.Host;

import java.io.IOException;

/*
 * This class implements a Game Board GUI for Battleships
 *
 * The GameBoard has a two player chat. It is possible to click on Tiles sp that an action, like placing ships
 * shooting on enemy tiles can happen. The board should show a hidden enemy board and your own board with the
 * ships
 *
 * @version ich nutz git
 * @author Christian Jesinghaus
 * */
public class GameBoardGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    //Setting the BoardSize
    private final int BOARD_SIZE = 10;
    //create a primary Stage
    //Creating Chat object which will be used in the corresponding constructor
    private ClientChat enteredClientChat;
    private HostChat enteredHostChat;
    //Creating a Join and Host object which will be used in the corresponding constructor
    private Join givenJoin;
    private Host givenHost;
    //Creating a player object for game interaction
    private final Player givenPlayer;

    //Creating a boolean telling if you are the Host or not
    private final boolean isServer;
    //Creating a boolean telling if the to be placed ships orientation is vertical or not
    private boolean ifVertical = false;
    //Creating a String for information interchange between the  drag and drop elements
    private  String transferString = "";

    //Creating for image objects for the battleships in the gameboard Gui
    Image img = new Image("https://upload.wikimedia.org/wikipedia/commons/d/db/Russian_cruiser_Moskva.jpg");
    Image imgCruiser = new Image("data:image/jpeg;base64,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");
    Image imgCarrier = new Image("data:image/jpeg;base64,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");
    Image imgSub = new Image("https://upload.wikimedia.org/wikipedia/commons/b/bb/US_Navy_040730-N-1234E-002_PCU_Virginia_%28SSN_774%29_returns_to_the_General_Dynamics_Electric_Boat_shipyard.jpg");
    //A constructor for the GUI taking a Player and a Clientchat for creating a gameboard for clients
   StartGUI mainMenu;
    public GameBoardGUI(Join givenJoin, ClientChat enteredClientChat, Player givenPlayer, StartGUI mainMenu) throws Exception {
        this.givenPlayer = givenPlayer;
        this.enteredClientChat = enteredClientChat;
        this.isServer = false;
        this.givenJoin = givenJoin;
        this.mainMenu = mainMenu;
    }
    //Creating a GUI taking a plaer and a hostchat for creating a gameboard for hosts
    public GameBoardGUI(Host givenHost, HostChat enteredHostChat, Player givenPlayer, StartGUI mainMenu) throws Exception {
        this.givenPlayer = givenPlayer;
        this.enteredHostChat = enteredHostChat;
        this.isServer = true;
        this.givenHost = givenHost;
        this.mainMenu = mainMenu;
        this.givenPlayer.setMyTurn(true);
    }

    /*
     * A getter for the Client in Host or Join object
     * @return givenHost.getClient() or givenJoin.getClient() iss logisch ne
     * @author Christian Jesinghaus
     * */
    public Client getClient(){
        if (isServer){
            return givenHost.getClient();
        }
        else{
            return givenJoin.getClient();
        }
    }


    /*
     * Helper function which changes the isVertical Boolean to true/false if false/true
     *
     * */
    public void changeisvertical(){
        ifVertical = !ifVertical;
    }

    //Helper for the text that is shown
    public void drawMove(Text text) {
        text.setText("X");
        text.setFill(Color.RED);
    }

    public void drawMoveWaterBombed(Text text){
        text.setText("O");
        text.setFill(Color.BLACK);
    }
    /*
     * Helper functions to change the tiles look to an image of the wanted battleship
     *@param tile the Rectangle tile in  the GUI whichs image to change
     * @author Christian Jesinghaus
     *
     * */
    public void changeTileToBattleship(Rectangle tile){
        tile.setFill(new ImagePattern(img));
    }
    public void changeTileToCarrier(Rectangle tile){
        tile.setFill(new ImagePattern(imgCarrier));
    }
    public void changeTileToCruiser(Rectangle tile){
        tile.setFill(new ImagePattern(imgCruiser));
    }
    public void changeTileToSubmarine(Rectangle tile){
        tile.setFill(new ImagePattern(imgSub));
    }

    /*
     * A method to update the Gameboard according to the given player map
     * @param playermap the Map which shall be used as information for the Gameboard
     * @param tile the tile which will be updated
     * @param text the text which can be placed over the tile for click information
     * @param i,j two ints as coordinates of the tile for synchronizing GUI and map
     * @author Christian Jesinghaus
     * */
    public void synchronizeGUITileandMapTiles(Map playerMap, Rectangle tile, Text text, int j, int i) {

        if (playerMap.getTileFromMap(j + 1, i + 1).getVisible()) {
            if (playerMap.getTileFromMap(j + 1, i + 1).getIsWater()) {
                tile.setFill(Color.CORNFLOWERBLUE);
                if (playerMap.getTileFromMap(j+1,i+1).getBombed()){
                    drawMoveWaterBombed(text);
                }
            }
            if (!playerMap.getTileFromMap(j + 1, i + 1).getIsWater()) {

                try {
                    if (ShipType.DESTROYER == playerMap.getShipAtCoordinates(j+1, i+1).getShipType()) {
                        changeTileToBattleship(tile);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
                try {
                    if (ShipType.CARRIER == playerMap.getShipAtCoordinates(j+1, i+1).getShipType()) {
                        changeTileToCarrier(tile);
                    }
                } catch (Exception e) {
                    System.out.println(e);                            }
                try {
                    if (ShipType.CRUISER == playerMap.getShipAtCoordinates(j+1, i+1).getShipType()) {
                        changeTileToCruiser(tile);
                    }
                } catch (Exception e) {
                    System.out.println(e +" cruiser exception in update tiles");
                }
                try {
                    if (ShipType.SUBMARINE == playerMap.getShipAtCoordinates(j+1, i+1).getShipType()) {
                        changeTileToSubmarine(tile);
                    }
                    /*else{
                        System.out.println("Not a Submarine");
                    }*/
                } catch (Exception e) {
                    System.out.println(e+ "Submarine exception in update tiles");
                }
                if (playerMap.getTileFromMap(j + 1, i + 1).getBombed()) {
                    drawMove(text);

                }
            }
        } if(!playerMap.getTileFromMap(j + 1, i + 1).getVisible() && !playerMap.getTileFromMap(j + 1, i + 1).getBombed()) {
            tile.setFill(Color.GREY);

        }

    }
    /*
     * A method to create the Board which is centered in the Stage. Setting a MousClick listener
     * on each tile for a possible action like printing something on it.
     *
     * @return gameBoard a parent showing the Board in the Stage
     * @author Christian Jesinghaus
     * */

    public Rectangle[][] opponentTiles = new Rectangle[10][10];
    public Rectangle[][] playerTiles = new Rectangle[10][10];
    public Text[][] opponentTexts = new Text[10][10];
    public Text[][] playerTexts = new Text[10][10];

    public void refreshPlayerBoard(){
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                int x = j;
                int y = i;
                //Creating tiles filled with cornflowerblue
                Rectangle tile = new Rectangle(50, 50);
                tile.setFill(Color.CORNFLOWERBLUE);
                tile.setStroke(Color.BLACK);
                playerTiles[j][i] = tile;

                Text text = new Text();
                text.setFont(Font.font(40));
                synchronizeGUITileandMapTiles(givenPlayer.getPlayerMap(), playerTiles[x][y], text, x, y);
            }
        }
    }
    public void refreshOpponentBoard(){
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                int x = j;
                int y = i;
                //Creating tiles filled with cornflowerblue
                Rectangle tile = new Rectangle(50, 50);
                tile.setFill(Color.CORNFLOWERBLUE);
                tile.setStroke(Color.BLACK);
                opponentTiles[j][i] = tile;

                Text text = new Text();
                text.setFont(Font.font(40));
                synchronizeGUITileandMapTiles(givenPlayer.getOpponentMap(), opponentTiles[x][y], text, x, y);
            }
        }
            }
    Parent givenPlayerMap;
    Parent givenOpponentMap;

    public void refreshBoards(){


                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            givenPlayerMap = createBoard(givenPlayer.getPlayerMap());
                            givenOpponentMap = createBoard(givenPlayer.getOpponentMap());
                            grid.add(givenPlayerMap, 0, 1);
                            grid.add(givenOpponentMap, 2, 1);
                        }
                    });

    }

    public  Parent createBoard(Map playerMap) {

        //Setting up a gridpane for the gameboard
        GridPane gameBoard = new GridPane();
        gameBoard.setPrefSize(500, 500);
        gameBoard.setAlignment(Pos.CENTER);
        Rectangle[][] tileList = new Rectangle[10][10];
        Text[][] textList = new Text[10][10];
       /* if (playerMap == givenPlayer.getPlayerMap()){
            tileList = playerTiles;
            textList = playerTexts;
        }else if (playerMap == givenPlayer.getOpponentMap()){
            tileList = opponentTiles;
            textList = opponentTexts;
        }*/
        //Placing a Rectangle with features on each node of the grid pane
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                int x = j;
                int y = i;
                //Creating tiles filled with cornflowerblue
                Rectangle tile = new Rectangle(50, 50);
                tile.setFill(Color.CORNFLOWERBLUE);
                tile.setStroke(Color.BLACK);
                tileList[j][i] = tile;

                //Putting text and tiles in one StackPane so that the text is displayed when clicked
                //This can be changed, so that a different tile is displayed
                Text text = new Text();
                text.setFont(Font.font(40));

                //gameBoard.add(new StackPane(tile, text), j, i);
                //Setting a MouseClick listener so that on a click the text is displayed
                /*Task<Void> task = new Task<>() {
                    @Override
                    protected Void call() throws Exception {


                            while (true) {
                                try {
                                if (givenPlayer.checkPlacementStatus()) {
                                    synchronizeGUITileandMapTiles(playerMap, tileList[x][y], text, x, y);
                                }
                                Thread.sleep(1200);
                                } catch (Exception e) {
                                    System.out.println(e + "  Tile Thread Exception");
                                    return null;
                                }
                            }
                    }
                };
                Thread t = new Thread(task);
                t.setDaemon(true); // thread will not prevent application shutdown
                t.start();
                    */
                synchronizeGUITileandMapTiles(playerMap, tileList[x][y], text, x, y);


                tileList[x][y].setOnMouseClicked(event -> {
                    if (!playerMap.getIsPlayerMap() && givenPlayer.checkPlacementStatus() && givenPlayer.getMyTurn()) {
                        //givenPlayer.attackTile(x + 1, y + 1);
                        getClient().sendCoordinatesToServer(x, y);
                    }
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

                //Assigning all tiles their to the map corresponding values
                synchronizeGUITileandMapTiles(playerMap, tileList[x][y], text,j,i);

                tileList[x][y].setOnDragOver(event -> {
                    /* data is dragged over the target */
                    /* accept it only if it is not dragged from the same node
                     * and if it has a string data */
                    if (event.getGestureSource() != tileList[x][y] &&
                            event.getDragboard().hasString()) {
                        /* allow for both copying and moving, whatever user chooses */
                        event.acceptTransferModes(TransferMode.ANY);
                    }
                    event.consume();
                });

                //Create a preview of the ship if you enter a tile where you can place it
                tileList[x][y].setOnDragEntered(event -> {
                    Dragboard db = event.getDragboard();
                    String dbString = db.getString();

                    // the drag-and-drop gesture entered the target

                    if (event.getGestureSource() != tileList[x][y] &&
                            event.getDragboard().hasString()) {
                        try {
                            switch (dbString) {
                                case "Destroyer":
                                    if (givenPlayer.checkPlacementLocation(ShipType.DESTROYER, ifVertical, x + 1, y + 1)) {
                                        //drawMove(text);
                                        givenPlayer.placeShip(ShipType.DESTROYER, ifVertical, x + 1, y + 1);
                                        for (int i13 = 0; i13 < BOARD_SIZE; i13++) {
                                            for (int j13 = 0; j13 < BOARD_SIZE; j13++) {
                                                synchronizeGUITileandMapTiles(playerMap, tileList[j13][i13], text, j13, i13);
                                            }
                                        }
                                        givenPlayer.undoLastShipPlacement();
                                    }
                                    break;
                                case "Cruiser":
                                    if (givenPlayer.checkPlacementLocation(ShipType.CRUISER, ifVertical, x + 1, y + 1)) {
                                        //drawMove(text);
                                        givenPlayer.placeShip(ShipType.CRUISER, ifVertical, x + 1, y + 1);
                                        for (int i13 = 0; i13 < BOARD_SIZE; i13++) {
                                            for (int j13 = 0; j13 < BOARD_SIZE; j13++) {
                                                synchronizeGUITileandMapTiles(playerMap, tileList[j13][i13], text, j13, i13);
                                            }
                                        }
                                        givenPlayer.undoLastShipPlacement();
                                    }
                                    break;
                                case "Carrier":
                                    if (givenPlayer.checkPlacementLocation(ShipType.CARRIER, ifVertical, x + 1, y + 1)) {
                                        //drawMove(text);
                                        givenPlayer.placeShip(ShipType.CARRIER, ifVertical, x + 1, y + 1);
                                        for (int i13 = 0; i13 < BOARD_SIZE; i13++) {
                                            for (int j13 = 0; j13 < BOARD_SIZE; j13++) {
                                                synchronizeGUITileandMapTiles(playerMap, tileList[j13][i13], text, j13, i13);
                                            }
                                        }
                                        givenPlayer.undoLastShipPlacement();
                                    }
                                    break;
                                case "Submarine":
                                    if (givenPlayer.checkPlacementLocation(ShipType.SUBMARINE, ifVertical, x + 1, y + 1)) {
                                        //drawMove(text);
                                        givenPlayer.placeShip(ShipType.SUBMARINE, ifVertical, x + 1, y + 1);

                                        for (int i13 = 0; i13 < BOARD_SIZE; i13++) {
                                            for (int j13 = 0; j13 < BOARD_SIZE; j13++) {
                                                synchronizeGUITileandMapTiles(playerMap, tileList[j13][i13], text, j13, i13);
                                            }
                                        }
                                        givenPlayer.undoLastShipPlacement();

                                    }
                                    break;
                            }
                        } catch (Exception ex) {
                            System.out.println(ex + "  in placement");
                        }
                    }
                    event.consume();
                });

                tileList[x][y].setOnDragExited(event -> {
                    //mouse moved away, remove the graphical cues
                    if (event.getGestureSource() != tileList[x][y] &&
                            event.getDragboard().hasString()) {
                        if (!event.getDragboard().getString().equals("Done")) {
                            try {
                                for (int i12 = 0; i12 < BOARD_SIZE; i12++) {
                                    for (int j12 = 0; j12 < BOARD_SIZE; j12++) {
                                        synchronizeGUITileandMapTiles(playerMap, tileList[j12][i12], text, j12, i12);
                                    }
                                }
                            } catch (Exception e) {
                                System.out.println("Inside synchronuze Exception");
                            }
                        }
                    }
                    event.consume();
                });

                tileList[x][y].setOnDragDropped(event -> {
                    /* data dropped */
                    /* if there is a string data on dragboard, read it and use it */
                    Dragboard db = event.getDragboard();
                    boolean success = false;
                    if (db.hasString()) {
                        String dbString = db.getString();
                        System.out.println(dbString + "  <-DBString");
                        try {
                            switch (dbString) {
                                case "Destroyer":
                                    if (givenPlayer.checkPlacementLocation(ShipType.DESTROYER, ifVertical, x + 1, y + 1) && givenPlayer.checkShipCount(ShipType.DESTROYER)) {
                                        //drawMove(text);
                                        givenPlayer.placeShip(ShipType.DESTROYER, ifVertical, x + 1, y + 1);
                                    }
                                    break;
                                case "Cruiser":
                                    if (givenPlayer.checkPlacementLocation(ShipType.CRUISER, ifVertical, x + 1, y + 1) && givenPlayer.checkShipCount(ShipType.CRUISER)) {
                                        //drawMove(text);
                                        givenPlayer.placeShip(ShipType.CRUISER, ifVertical, x + 1, y + 1);
                                    }
                                    break;
                                case "Carrier":
                                    if (givenPlayer.checkPlacementLocation(ShipType.CARRIER, ifVertical, x + 1, y + 1) && givenPlayer.checkShipCount(ShipType.CARRIER)) {
                                        //drawMove(text);
                                        givenPlayer.placeShip(ShipType.CARRIER, ifVertical, x + 1, y + 1);
                                    }
                                    break;
                                case "Submarine":
                                    if (givenPlayer.checkPlacementLocation(ShipType.SUBMARINE, ifVertical, x + 1, y + 1) && givenPlayer.checkShipCount(ShipType.SUBMARINE)) {
                                        //drawMove(text);
                                        givenPlayer.placeShip(ShipType.SUBMARINE, ifVertical, x + 1, y + 1);
                                    }
                                    break;
                                default:
                                    try {
                                        for (int i1 = 0; i1 < BOARD_SIZE; i1++) {
                                            for (int j1 = 0; j1 < BOARD_SIZE; j1++) {
                                                synchronizeGUITileandMapTiles(playerMap, tileList[j1][i1], text, j1, i1);
                                            }
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Inside synchronuze Exception");
                                        throw new RuntimeException(e);
                                    }
                                    break;
                            }
                        } catch (Exception ex) {
                            System.out.println(ex + "  in placement");
                        }
                        try {
                            for (int i1 = 0; i1 < BOARD_SIZE; i1++) {
                                for (int j1 = 0; j1 < BOARD_SIZE; j1++) {
                                    synchronizeGUITileandMapTiles(playerMap, tileList[j1][i1], text, j1, i1);
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Inside synchronuze Exception");
                            throw new RuntimeException(e);
                        }
                        success = true;
                    }
                    /* let the source know whether the string was successfully
                     * transferred and used */
                    event.setDropCompleted(success);
                    event.consume();
                });
                gameBoard.add(new StackPane(tileList[j][i], text), j, i);
            }
        }
        return gameBoard;
    }



    /*
    * Creates the Menu for the ship drag event and interaction and preview
    * @return previewGrid a GridPane in which these elements are arranged
    * @author Christian Jesinghaus
    * */
    public Parent createShipPreview(){
        //Creating the Grid in which the elements of the preview menu are arranged
        GridPane previewGrid = new GridPane();
        previewGrid.setHgap(3);
        previewGrid.setVgap(3);
        //Create a grid for the drag element
        GridPane shipPane =new GridPane();
        shipPane.setHgap(3);
        shipPane.setVgap(3);
        //Create the menu buttons for Placements, change Orientation and Readyness
        Button Placement = new Button();
        Button Orientation = new Button();
        Button Ready = new Button();
        Button Shoot = new Button();
        Button Back = new Button();
        Label placementLabel = new Label();
        placementLabel.setLabelFor(Placement);
        placementLabel.setText("Start Placement");
        Label OrientationLabel = new Label();
        OrientationLabel.setLabelFor(Orientation);
        OrientationLabel.setText("Toggle Orientation");

        Back.setText("Undo");
        Shoot.setText("Load");
        Ready.setText("Ready");
        Ready.setDisable(true);
        Orientation.setText("Rotate");
        Placement.setText("Set Ship");
        //Create the tile for the ship preview
        Rectangle tile = new Rectangle(50, 50);
        tile.setFill(Color.WHITE);
        Label tileLabel = new Label();
        tileLabel.setLabelFor(tile);
        tileLabel.setText("Drag a ship");
        //Add this tile to the drag event grid
        shipPane.add(tile, 0, 0);


        /*
        * Creating the action for the ready button. Sending the update string to the server
        * and receiving one to create the opponent map
        * */
        Ready.setOnAction(e -> {
            Ready.setDisable(true);
            getClient().sendShipArrayToServer(givenPlayer.createUpdateString());
            getClient().sendMessageToServer("I am ready!");
            /*givenPlayer.updateOpponentMap(HIER MUSS DER VOM SERVER KOMMENDE STRING REIN);*/
        });

        Shoot.setOnAction(e->{
            Shoot.setText("You can shoot now");
        });

        Back.setOnAction(e->{
            //givenPlayer.undoLastShipPlacement();
            /*try {
                this.mainMenu.start(primaryStage);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }*/
        });
        /*
        * Creating the action for the Placement button
        * depending on the ship type to currently place the drag tile will be defined
        * and the transferstring as well. If the end of placement phase is reached the Placement
        * Button and Tile are deactivated and the Ready Button gets activated
        * */
        Placement.setOnAction(e -> {
            if (givenPlayer.checkShipCount(ShipType.CARRIER)) {
                Placement.setText("Carrier(" + givenPlayer.getCarrierCount() + ")");
                transferString = "Carrier";
                changeTileToCarrier(tile);
            } else if (givenPlayer.checkShipCount(ShipType.CRUISER)) {
                Placement.setText("Cruiser(" + givenPlayer.getCruiserCount() + ")");
                transferString = "Cruiser";
                changeTileToCruiser(tile);
            } else if (givenPlayer.checkShipCount(ShipType.DESTROYER)) {
                Placement.setText("Destroyer(" + givenPlayer.getDestroyerCount() + ")");
                transferString = "Destroyer";
                changeTileToBattleship(tile);
            } else if (givenPlayer.checkShipCount(ShipType.SUBMARINE)) {
                Placement.setText("Submarine(" + givenPlayer.getSubmarineCount() + ")");
                transferString = "Submarine";
                changeTileToSubmarine(tile);

            } else if (givenPlayer.checkPlacementStatus()) {
                Ready.setDisable(false);
                Placement.setDisable(true);
                shipPane.setDisable(true);
                tile.setFill(Color.BLACK);
            }
        });

        /*
        * Creates a Button to change the placed ships orientation
        * Default: Horizontal orientation, if pressed changes to vertical by changing the
        * ifVertical boolean
        *
        * */
        Orientation.setOnAction(e -> changeisvertical());
        /*
        * Creates a Drag Event from the Source, transferring a String to the target
        *
        * */
        shipPane.setOnDragDetected(event -> {
            /* drag was detected, start a drag-and-drop gesture*/
            /* allow any transfer mode */
            Dragboard db = shipPane.startDragAndDrop(TransferMode.ANY);

            /* Put a string on a dragboard */
            ClipboardContent content = new ClipboardContent();
            content.putString(transferString);
            db.setContent(content);

            event.consume();
        });
        /*
        * Creates the corresponding drag done event telling the target if the drag is done
        *
        * */
        shipPane.setOnDragDone(event -> {
            /* the drag and drop gesture ended */
            /* if the data was successfully moved, clear it */
            if (event.getTransferMode() == TransferMode.MOVE) {
                ClipboardContent content = new ClipboardContent();
                content.putString("Done");
                event.getDragboard().setContent(content);
            }
            event.consume();
        });
        Button Exit = new Button();
        Exit.setText("Exit");
        Exit.setOnAction(e -> {

            System.exit(1);
        });
        //Setting all PlacementMenu Elements up in a GriPane
        previewGrid.add(shipPane,0,2);
        previewGrid.add(Placement,1,0);
        previewGrid.add(Orientation,2,0);
        previewGrid.add(Ready,3,0);
        //previewGrid.add(Shoot,4,0);
        previewGrid.add(Exit,2,2);
       // previewGrid.add(Back,3,2);
        return previewGrid;
    }





    /*
    * This method sets everything up in a beautiful GUI
    *
    * it puts the player map to the left, the opponent map to the right and the chat
    * in the middle. Below the chat you can find the placement and general interaction menu.
    * Depending on if you are the host or not you will get that corresponding chat
    * */
    GridPane grid = new GridPane();
    public Parent arrangeBoardsandChat() throws Exception {
        grid.setHgap(10);
        grid.setVgap(12);
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(5));
        Label playerMapLabel = new Label();
        Label opponentMapLabel = new Label();
        Label chatLabel = new Label();
        Label previewLabel = new Label();
        previewLabel.setText("Placement Menu");
        chatLabel.setText("Chat");
        playerMapLabel.setText("Your Map");
        opponentMapLabel.setText("Opponent Map");

        givenPlayerMap = createBoard(givenPlayer.getPlayerMap());
        givenOpponentMap = createBoard(givenPlayer.getOpponentMap());

        //playerMapLabel.setLabelFor(givenPlayerMap);
        //opponentMapLabel.setLabelFor(givenOpponentMap);
        previewLabel.setTextFill(Color.WHITE);
        chatLabel.setTextFill(Color.WHITE);
        playerMapLabel.setTextFill(Color.WHITE);
        opponentMapLabel.setTextFill(Color.WHITE);
        previewLabel.setFont(new Font("Arial", 15));
        chatLabel.setFont(new Font("Arial", 30));
        playerMapLabel.setFont(new Font("Arial", 30));
        opponentMapLabel.setFont(new Font("Arial", 30));
        previewLabel.setAlignment(Pos.CENTER);
        grid.add(playerMapLabel, 0, 0);
        grid.add(opponentMapLabel,2, 0);
        grid.add(givenPlayerMap, 0, 1);
        grid.add(givenOpponentMap, 2, 1);
        grid.add(previewLabel,1,2);
        grid.add(createShipPreview(),1,3);
        if (isServer){
            try{
                grid.add(chatLabel, 1, 0);
                grid.add(enteredHostChat.createContent(), 1, 1);
            }
            catch(Exception e){
                System.out.println(e);
                System.exit(2);
            }
        }else{
            try{
                grid.add(chatLabel, 1, 0);
                grid.add(enteredClientChat.createContent(), 1, 1);
            }
            catch(Exception e){
                System.out.println(e);
                System.exit(2);
            }

        }
        grid.setStyle(" -fx-background-color: black;");
        return grid;
    }

    /*
     * The start function setting up the GUI including the gameBoard
     *
     * @param primaryStage the stage in which the game is displayed
     * @author Christian Jesinghaus
     * */
   double width = 1500;
   double height = 780;
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Creating the scene
        Scene scene = new Scene(arrangeBoardsandChat(),this.width,this.height);
        //Set ClientChat if Client else ServerChat
        primaryStage.setTitle("Battleships");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
