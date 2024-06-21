package gui;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Test2GUIs extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Stage secondaryStage = new Stage();
       StartGUI Player1 = new StartGUI(primaryStage);
        StartGUI Player2 = new StartGUI(secondaryStage);
    }
}
