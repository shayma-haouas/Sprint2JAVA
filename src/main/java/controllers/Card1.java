package controllers;
import entities.Evenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import controllers.Mylistener;
import javafx.stage.Stage;
import services.ServiceEvenement;

import java.io.IOException;

public class Card1 {
    @FXML
    private Label nameLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private ImageView img;
    @FXML
    private Label datedebutLabel;


    @FXML
    private Button participate; // Button for user participation
/*@FXML
    private void participate(MouseEvent mouseEvent) {
        if (Mylistener != null && evenement != null) {
            // Call the listener's method to handle user participation
            Mylistener.onClickListener(evenement);
        }
    }*/
    private ServiceEvenement serviceEvenement = new ServiceEvenement();
    // Method to handle user participation when the participate button is clicked
    /*@FXML
    private void handleParticipation() {
        // Replace these with actual userId and eventId values
        int userId = 1;
        int eventId = evenement.getId();

        // Call the addUserToEvent method to add the user to the event
        serviceEvenement.addUserToEvent(userId, eventId);
    }*/
    @FXML
    private Label datefinLabel;
    @FXML
    private Label placeLabel;

    @FXML
    private Label ID;
    private Evenement evenement;
    private Mylistener Mylistener;

    public void setData(Evenement evenement, Mylistener Mylistener) {
        this.evenement = evenement;
        this.Mylistener = Mylistener;

        nameLabel.setText(evenement.getNameevent());
        typeLabel.setText("Type: " + evenement.getType());

        placeLabel.setText("Location: " + evenement.getLieu());
        Image  image = new Image("file:" + evenement.getImage());
        img.setImage(image);
    }



    @FXML
    private void click(MouseEvent mouseEvent) {
        Mylistener.onClickListener(evenement);
    }







}