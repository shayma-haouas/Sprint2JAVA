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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eventback/sponsor.fxml"));
            Parent root = loader.load();

            // Get the controller instance
            Sponsorb controller = loader.getController();

            // Call the show() method to fetch data and populate the TableView
            controller.show();
            controller.ajoutersponsor(new ActionEvent());
            controller.modifiersponsor(new ActionEvent());
            // Set up the primary stage
            primaryStage.setTitle("Testing FXML Interface");
            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }}