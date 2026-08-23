package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//import database.DatabaseConnection;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        //DatabaseConnection.connect();
        try {

            Parent root = FXMLLoader.load(
                getClass().getResource("/views/login.fxml")
            );

            // SMALLER LOGIN WINDOW
            Scene scene = new Scene(root, 1000, 600);
            // X stage.setMaximized(true);
            stage.setTitle("Healthcare Management System");

            stage.setScene(scene);

            stage.show();

        }
        catch(Exception e) {

            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        launch(args);
    }
}