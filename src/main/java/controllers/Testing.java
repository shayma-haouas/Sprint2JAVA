package controllers;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Testing extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eventfront/FrontEV.fxml"));
            Parent root = loader.load();
           CardEvController c =loader.getController();
            // Get the controller instance


            // Call the showw() method to fetch data and populate the ListView

            //controller.showw();
            // Set up the primary stage
            primaryStage.setTitle("Testing FXML Interface with showw() Method");
            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
