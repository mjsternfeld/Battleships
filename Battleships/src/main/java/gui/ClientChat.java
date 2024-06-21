package gui;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import network.client.Join;

import java.io.IOException;

/*
* A class implementing the ClientChat
*
* @version siehe Git
* @author Christian Jesinghaus
* */
public class ClientChat extends Application  {
    //Creating the text field showing the messages
    public static TextArea messages = new TextArea();
    //Strings for IP and name
    private String enteredName;
    private String enteredIP;
    //Create the Join object
    private Join enteredJoin;

    //Constructor for the ClientChat
    public ClientChat(Join enteredJoin) throws Exception{
        this.enteredJoin = enteredJoin;
        init();
    }
    /*
     * This method creates the Content showed in the text field and saved.
     *
     * @return root the Parent Node
     * @author Christian Jesinghaus
     * */
    public Parent createContent() throws IOException {
        //Setting up the text window
        messages.setPrefHeight(350);
        //Creating the input text window
        TextField input = new TextField();
        //Setting up the input event on the input window
        input.setOnAction(event->{
            String message = "";
            message += input.getText();
            input.clear();
            //Sending the message to the server
            try {
                this.enteredJoin.sendMessage(message);
                try{Thread.sleep(100);}catch(Exception e){};
            }
            catch(Exception e){
                messages.appendText("An Error occured" + "\n");
            }
        });
        //Setting all up in a VBox
        VBox root = new VBox(5, messages, input);
        root.setPrefSize(150, 150);
        return root;
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

    public static void main(String[] args) {
        launch(args);
    }
}
