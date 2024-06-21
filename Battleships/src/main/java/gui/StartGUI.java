package gui;

import game.Player;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.stage.Popup;
import network.client.Join;
import network.host.Host;
import java.io.IOException;

/*
* A StartGUI for the game initializing a window with START,JOIN, HOST, EXIT
* Buttons with corresponding actions.
*
* @version wat weiss ich ich nutz Git
* @author Christian Jesinghaus
* */
public class StartGUI extends Application {

    //Create Host and Client Player
    private static Player HostPlayer;
    private static Player ClientPlayer;

    public static network.host.Host getNewHost() {
        return newHost;
    }

    public static network.client.Join getNewJoin() {
        return newJoin;
    }

    //Create Host and Join object
    private static Host newHost;
    private static HostChat newHostChat;
    private static HostChat newHostChatGame;

    private static ClientChat newClientChat;
    private static ClientChat newClientChatGame;
    private static Join newJoin;
    // Definition of the buttons
    Button Start;
    Button Exit;
    Button Join;
    Button Host;
    //To save the entered IP adress
    String EnteredIP;
    //To save the entered Port
    String EnteredName;
    boolean isServer;

    GridPane chatGrid = new GridPane();

    //Create a stage
    private final Stage primaryStage = new Stage();
    private final Stage HostChatStage = new Stage();
    private final Stage ClientChatStage = new Stage();

    private Stage GameBoardStage = new Stage();

    //Constructor for the StartGUI

   public StartGUI(Stage primaryStage) throws IOException{

        start(primaryStage);
    }

    static GameBoardGUI newHostGame;
    static GameBoardGUI newClientGame;

    /*
    * A helper for initializing a button and set a corresponding text
    * @param buttonText the text which is set for the buttonText
    * @author Christian Jesinghaus
    *
    * */
    public Button ButtonInitSetText(String buttonText){
        Button buttonName = new Button();
        buttonName.setText(buttonText);
        return buttonName;
    }
    public static GameBoardGUI getClientGameboardGUI(){
        return newClientGame;
    }
    public static GameBoardGUI getHostGameboardGUI(){
        return newHostGame;
    }
    /*
    * This function starts the GUI with all its implementations
    *
    * The StartGUI has buttons like Start, Exit, Join, Host. Start starts the game, Exit ends the program
    * Join opens two text input windows to input the hosts IP and Port and Host shows you your IP/Port
    * In the finished version Join/Host will start a Server/Client connection for a multiplayer and a chat
    * Start then will start a new Battleship game between two players.
    *
    * @param primaryStage the stage on which the GUI is placed
    * @return void
    * @author Christian Jesinghaus
    * */
    @Override
    public void start(Stage primaryStage) throws IOException{
        try {
            //Setting titel of window
            primaryStage.setTitle("Battleships");
            //Creating a stack pane
            StackPane root = new StackPane();

           //Creating the scene
            Scene scene = new Scene(root,600,600);

            // create a label for Port/Label
            Label label = new Label("Your IP: " + network.host.Host.getIPAddress() + " \n" + "Your Port: 9999");
            // create a popup for showing IP/Port
            Popup popup = new Popup();
            //Aligning the Popup
            popup.setAnchorLocation(PopupWindow.AnchorLocation.CONTENT_TOP_LEFT);
            // set background
            label.setStyle(" -fx-background-color: white;");
            // add the label
            popup.getContent().add(label);
            // set size of label
            label.setMinWidth(60);
            label.setMinHeight(40);

            // create a text input dialog for IP
            TextInputDialog td = new TextInputDialog("Enter here");

            //create a text input for the Port
            TextInputDialog td2 = new TextInputDialog("Enter here");

            // setHeaderText for the textinput windows
            td.setHeaderText("Enter Host-IP");
            td2.setHeaderText("Enter your Name");

            //initialising buttons and setting text
            Start = ButtonInitSetText("Start");
            Exit = ButtonInitSetText("Exit");
            Join = ButtonInitSetText("Join");
            Host = ButtonInitSetText("Host");

            //Adding actions to the buttons
            Exit.setOnAction(e -> {
                //When Exit is pressed, end the program
                System.exit(1);
            });
            //Set the Start button action
            Start.setOnAction(e -> {
                //When pressed Start, start a new GameBoard
                if (isServer) {
                    try {
                        newHostGame = new GameBoardGUI(newHost, newHostChatGame, HostPlayer, this);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        newHostGame.start(primaryStage);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    try {
                        newClientGame = new GameBoardGUI(newJoin, newClientChatGame, ClientPlayer, this);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        newClientGame.start(primaryStage);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
                //Start.setText("Eig. sollte starten");
            });
            //Set the Join Button action
            Join.setOnAction(e -> {

                isServer = false;
                //Show the textinput window
                td.showAndWait();
                //Save IP
                EnteredIP = td.getEditor().getText();
                System.out.println(EnteredIP + "The entered IP");
                //Show the window for the port
                td2.showAndWait();
                //Save Port
                EnteredName = td2.getEditor().getText();
                System.out.println(EnteredName + "The entered Name");
                try {
                    ClientPlayer = new Player(false);
                    newJoin = new Join(EnteredIP, EnteredName);
                    try {
                        newClientChat = new ClientChat(newJoin);
                        newClientChatGame = new ClientChat(newJoin);
                        chatGrid.add(newClientChat.createContent(), 0, 0);

                    } catch (Exception anotherE) {
                        System.out.println("Exception im Join button");
                        System.exit(2);
                    }
                } catch (IOException IOe) {
                    System.out.println("Test (catch block in IOException)");
                }
            });
            //Set the Host button action
            Host.setOnAction(e -> {

                isServer = true;
                //If Host is clicked show your IP and Port in a PopUp

                    try {
                        HostPlayer = new Player(true);
                        newHost = new Host("Host");
                        String IP = newHost.getIPAddress();
                        String Port = "9999";
                        try {
                            newHostChat = new HostChat(newHost);
                            newHostChatGame = new HostChat(newHost);

                            chatGrid.add(newHostChat.createContent(), 0, 0);
                            newHostChat.addMessagetoChat("Your IP is: " + IP + "\n");
                            Host.setDisable(true);

                        } catch (Exception newE) {
                            System.out.println(newE);
                            System.exit(2);
                        }
                    } catch (IOException IOe) {
                        System.out.println("Test (catch block in StartGUI)");
                    }
            });


            //Adding the buttons to a VBox
            VBox vbox = new VBox(5); // 5 is the spacing between elements in the VBox
            GridPane grid = new GridPane();
            vbox.setPadding(new Insets(10));
            StackPane cell = new StackPane(chatGrid);
            cell.setPrefSize(150,150);
            grid.setHgap(10);
            grid.setVgap(10);
            vbox.getChildren().addAll( Host, Join, Start , Exit, cell);
            //grid.add(Host, 1,0);
            //grid.add(Join, 1,1);
            //grid.add(Start, 1,2);
            //grid.add(Exit, 1,3);
            //grid.add(cell,0,12 );
            vbox.setAlignment(Pos.CENTER);
            // Adding the VBox to the StackPane and centering it
            root.getChildren().add(vbox);
            root.setStyle("-fx-background-image: url(https://as1.ftcdn.net/v2/jpg/00/27/96/54/1000_F_27965429_T9KSr7OM9LIJUOpKq2Ky0OQcSvoX1bkK.jpg); ");


            //root.setStyle(" -fx-background-color: blue;");
            root.setPadding(new Insets(16));

            //Putting scene to stage
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    //getters and setters for the player objects
    public static Player getClientPlayer() {
        return ClientPlayer;
    }

    public static void setClientPlayer(Player clientPlayer) {
        ClientPlayer = clientPlayer;
    }

    public static Player getHostPlayer() {
        return HostPlayer;
    }

    public static void setHostPlayer(Player hostPlayer) {
        HostPlayer = hostPlayer;
    }


    public static void main(String[] args) throws IOException {
        launch(args);
    }
}
