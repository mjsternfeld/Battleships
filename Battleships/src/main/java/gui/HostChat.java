package gui;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import network.host.Host;
import java.io.IOException;
/*
* A class implementing the HostChat
*
*
* @version nutze Git
* @author Christian Jesinghaus
* */
public class HostChat extends Application {
    /*
    public HostChat() throws Exception{
        Stage primaryStage = new Stage();
        init();
        start(primaryStage);
    }
    */

    //private ServerConnection connection = new ServerConnection(server);
    //Creating the text window
    public static TextArea messages = new TextArea();
    //Strings for IP and name
    private String enteredName;
    //Create the Join object
    private Host enteredHost;
    //Creating the constructor for the HostChat
    public HostChat(Host enteredHost) throws Exception{

        this.enteredHost = enteredHost;
        init();
    }
    /*
    * This method creates the Content showed in the text field and saved.
    *
    * @return root the Parent Node
    * @author Christian Jesinghaus
    * */
    public Parent createContent() throws IOException {
        //Setting up the Text Field where the messages are shown
        messages.setPrefHeight(350);
        //Creating the input window
        TextField input = new TextField();
        //Creating the input possibillity in the input window
        input.setOnAction(event->{
            String message = "";
            message += input.getText();
            input.clear();
            //Here the message is send to the server so that the client gets it
            try {
                this.enteredHost.sendMessageFromClientToServer(message);
                try{Thread.sleep(100);}catch(Exception e){};
            }
            catch(Exception e){
                messages.appendText("An Error occured" + "\n");
            }
        });
        //Setting everything up in a VBox
        VBox root = new VBox(5, messages, input);
        root.setPrefSize(150, 150);
        return root;
    }


    public void addMessagetoChat(String message){
        messages.appendText(message);
    }
    /*
    * A method overriding the init() method to initialize the chat window
    *
    * @author Christian Jesinghaus
    * */
    @Override
    public void init() throws Exception{
            try{
                createContent();
            }
            catch(Exception e){
                System.out.println(e);
                System.exit(2);
            }
    }

    /*
    * Overriding the start() method
    *Overriding the start() method so that it starts the Chat Window
    *
    * @param primaryStage the Stage where it is displayed
    * @author Christian Jesinghaus
    * */
    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.setTitle("Chat");
        primaryStage.show();
    }
/*
    @Override
    public void stop() throws Exception{
        server.closeConnections();
    }*/


    public static void main(String[] args) throws Exception{
       /* HostChat newHostChat = new HostChat("Host");
        Stage primaryStage = new Stage();
        newHostChat.start(primaryStage); */
        launch(args);
    }
}
