package controllers;

import entities.Evenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.ServiceEvenement;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class REC {

    @FXML
    private VBox recommendedEventsVBox;
    private List<Evenement> evenements = new ArrayList<>();
    private Image image;
    private Mylistener Mylistener;

    private ServiceEvenement serviceEvenement = new ServiceEvenement();


    private List<Evenement> getDataFromDatabase() {
        return serviceEvenement.afficherEVWithSponsors();
    }






    // Method to populate recommended events
    public void populateRecommendedEvents() {
        // Get recommended events based on proximity to current date
        try {
            // Get recommended events based on proximity to current date
            List<Evenement> recommendedEvents = serviceEvenement.recommendEventsByProximity(5); // Change 5 to the desired number of recommendations

            // Clear existing children of the VBox
            recommendedEventsVBox.getChildren().clear();

            // Iterate through recommended events and create custom cards
            for (Evenement recommendedEvent : recommendedEvents) {
                // Create a VBox to hold the attributes of the event
                VBox eventCard = new VBox();
              eventCard.getStyleClass().add("event-card"); // Add CSS class for styling
                ImageView imageView = new ImageView();
                String imagePath = recommendedEvent.getImage();

// Load the image from the file path
                File file = new File(imagePath);
                Image image = new Image(file.toURI().toString());

// Set the image to the image view
                imageView.setImage(image);
// Set the width and height of the image view
                imageView.setFitWidth(100); // Set your desired width
                imageView.setFitHeight(100); // Set your desired height

// Add the image view to the event card
                eventCard.getChildren().add(imageView);


               // Set the height of the image
                // Create labels for each attribute of the event
                Label nameLabel = new Label("Name: " + recommendedEvent.getNameevent());
                Label typeLabel = new Label("Type: " + recommendedEvent.getType());
                Label startDateLabel = new Label("Start Date: " + recommendedEvent.getDatedebut());
                Label endDateLabel = new Label("End Date: " + recommendedEvent.getDatefin());
                Label descriptionLabel = new Label("Description: " + recommendedEvent.getDescription());
                Label participantsLabel = new Label("Participants: " + recommendedEvent.getNbparticipant());
                Label locationLabel = new Label("Location: " + recommendedEvent.getLieu());

                // Add labels to the event card
                eventCard.getChildren().addAll(nameLabel, typeLabel, startDateLabel, endDateLabel, descriptionLabel, participantsLabel, locationLabel);

                // Add the event card to the VBox
                recommendedEventsVBox.getChildren().add(eventCard);
            }
        } catch (Exception e) {
            // Log any exceptions
            e.printStackTrace();
        }
    }}
