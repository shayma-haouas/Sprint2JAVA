package controllers;

import entities.Sponsor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import services.ServiceSpon;

import java.sql.SQLException;

public class CRUDsponsor {
    private ServiceSpon rcrd = new ServiceSpon();



    public ObservableList<Sponsor> data = FXCollections.observableArrayList();
    @FXML
    private TextField tfnom;

    @FXML
    private TextField emailField;

    @FXML
    private TextField numfield;

    @FXML
    private Button btnenvoyer;
    @FXML
    void ajoutersponsor(ActionEvent event) {
        // Get the data from the input fields
        String nom = tfnom.getText().trim(); // Assuming tfnom is the TextField for sponsor name
        String email = emailField.getText().trim(); // Assuming emailField is the TextField for email
        String numeroText = numfield.getText().trim(); // Assuming numfield is the TextField for number

        // Validate input
        if (nom.isEmpty() || email.isEmpty() || numeroText.isEmpty()) {
            // Afficher un message d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Champs vides");
            alert.setHeaderText(null);
            alert.setContentText("Aucune de ces informations ne doit être vide. Veuillez remplir tous les champs.");
            alert.showAndWait();
            return;
        }
        // Validate name format
        if (!isValidName(nom)) {
            // Afficher un message d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Champs vides");
            alert.setHeaderText(null);
            alert.setContentText("Nom invalide, Le nom doit contenir uniquement des caractères et ne doit pas dépasser 10 caractères.");
            alert.showAndWait();
            return;
        }

        // Validate email format
        if (!isValidEmail(email)) {
            // Afficher un message d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Champs vides");
            alert.setHeaderText(null);
            alert.setContentText("Format d'email invalide, Veuillez entrer une adresse e-mail valide.");
            alert.showAndWait();
            return;
        }

        int numero;
        try {
            numero = Integer.parseInt(numeroText); // Parse the number
        } catch (NumberFormatException e) {

            // Afficher un message d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("integer");
            alert.setHeaderText(null);
            alert.setContentText("veuillez saisir un numero .");
            alert.showAndWait();
            return;
        }
        // Create a Sponsor object with the retrieved data
        Sponsor sponsor = new Sponsor(0, nom, email, numero); // 0 khtr id mtaana auto generated

        // Insert the Sponsor object into the database
        try {
            rcrd.ajouter(sponsor); // Assuming rcrd is the instance of ServiceSpon
            // Optionally, you can clear the input fields after adding the sponsor
            tfnom.clear();
            emailField.clear();
            numfield.clear();
            // Refresh the table view to display the newly added sponsor

        } catch (SQLException ex) {
            // Handle any SQL exceptions
            ex.printStackTrace();
            // You might want to show an error message to the user
        }
    }
    // Method to validate name format
    private boolean isValidName(String name) {
        return name.matches("[a-zA-Z]+")&& name.length() >= 3 && name.length() <= 10;
    }

    // Method to validate email format using regular expression
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }
}

