package controllers;

import services.ServiceSpon;
import entities.Sponsor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.MyDatabase;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Sponsorb {
    private ServiceSpon rcrd = new ServiceSpon();



    public ObservableList<Sponsor> data = FXCollections.observableArrayList();
    @FXML
    private Button btnenvoyer;

    @FXML
    private TableView<Sponsor> tableSponsors;

    @FXML
    private TableColumn<Sponsor, String> colnom;

    @FXML
    private TableColumn<Sponsor, String> colemail;

    @FXML
    private TableColumn<Sponsor, Integer> colnumber;

    @FXML
    private TableColumn<Sponsor, Integer> colid;

    @FXML
    private Button btnmodifier;

    @FXML
    private Button btnsupprimer;

    @FXML
    private Button trie;

    @FXML
    private TextField emailField;

    @FXML
    private TextField numfield;

    @FXML
    private TextField tfnom;

    @FXML
    private Button statistique;

    @FXML
    private Button trie1;

    @FXML
    private TextField searchInput;

    @FXML
    private Button btnretour;

    @FXML
    private ImageView image1;

    @FXML
    private Button btnEvenement;

    @FXML
    private Button btnParticipant;

    @FXML
    private Button btnSponsor;

    //DISPLAAYYY DATAASPON
    @FXML
    public void show() {
        // Clear the existing data bsh metdupliquish l old data khtr k naaml show f testing o show houny ijouny mertyn
        data.clear();
        try {
            String requete = "SELECT * FROM sponsor";
            Statement st = MyDatabase.getInstance().getConnection().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                Sponsor r = new Sponsor(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getInt("number"));
                data.add(r);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }


        colnom.setCellValueFactory(new PropertyValueFactory<>("name"));
        colemail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colnumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        colid.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableSponsors.setItems(data);




    }



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
            show();
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

    @FXML
    void gererEvenement(ActionEvent event) {

            try {
                Parent EvenementsParent = FXMLLoader.load(getClass().getResource("/eventback/Evenement.fxml"));
                Scene EvenementsScene = new Scene(EvenementsParent);
                Stage window = (Stage)(((Button)event.getSource()).getScene().getWindow());
                window.setScene(EvenementsScene);
                window.show();
            } catch (IOException e) {
            }
        }


    @FXML
    void gererParticipant(ActionEvent event) {

    }

    @FXML
    void gererSponsor(ActionEvent event) {

    }
    @FXML
    void Selected(MouseEvent event) {
        Sponsor sponsor = tableSponsors.getSelectionModel().getSelectedItem();
        if (sponsor != null) {
            tfnom.setText(sponsor.getName());
            emailField.setText(sponsor.getEmail());
            numfield.setText(String.valueOf(sponsor.getNumber()));
        } else {
            tfnom.setText("");
            emailField.setText("");
            numfield.setText("");
        }
    }
    @FXML
    void modifiersponsor(ActionEvent event) {

        Sponsor Sponsor = tableSponsors.getSelectionModel().getSelectedItem();


        if (Sponsor == null) {
            // Aucune evénement sélectionnée, afficher un message d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de modification");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une Sponsor à modifier.");
            alert.showAndWait();
        }



        else {
            // Vérifier que tous les champs sont remplis
            if (numfield.getText() == null  || tfnom.getText().isEmpty()|| emailField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de modification");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs avant de modifier une sponsor.");
                alert.showAndWait();
                return;
            }
            // Check if numfield contains only numeric characters
            String numvald = numfield.getText();
            if (!numvald .matches("\\d+")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Number");
                alert.setHeaderText(null);
                alert.setContentText("veuillez saisir un numero valid");
                alert.showAndWait();
                return;
            }
            // Vérifier que la longueur de la description est supérieure à 5 caractères
            String nomvald = tfnom.getText().trim();
             if (!isValidName(nomvald)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("le nom doit contenir que des caractetes ( au moins 3 caractères at au maximum 10 caracteres.)");
                alert.showAndWait();
                return;
            }

            // Récupérer les nouvelles valeurs



           // Vérifier que l'email est valide amlt var ekhr l validation
            String emailval = emailField.getText();
            if (!emailval .matches("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Email invalide");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez saisir une adresse email valide.");
                alert.showAndWait();
                return;
            }
            String numeroText = numfield.getText();
            String nom = tfnom.getText();
            String email = emailField.getText();
            int numero = Integer.parseInt(numeroText);

            // Mettre à jour la evénement dans la base de données
            rcrd.modifierSpon(new Sponsor(Sponsor.getId(), nom, email, numero));

            // Afficher un message de confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Modification réussie");
            alert.setHeaderText(null);
            alert.setContentText("La sponsor a été modifiée avec succès.");
            alert.showAndWait();
            // Clear the text fields after modification
            tfnom.clear();
            emailField.clear();
            numfield.clear();
            // Rafraîchir la table view pour afficher les nouvelles données
            data.clear();
            show();
        }
    }




    @FXML
    void retour(ActionEvent event) {

    }

    @FXML
    void searchSponsors(ActionEvent event) {

    }

    @FXML
    void statistique(ActionEvent event) {

    }

    @FXML
    void supprimersponsor(ActionEvent event) {
        // Récupérer la ligne sélectionnée dans la table view
        Sponsor selectedSponsors = tableSponsors.getSelectionModel().getSelectedItem();
        if (selectedSponsors == null) {
            // Afficher un message de confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Suppression deSponsors");
            alert.setHeaderText(null);
            alert.setContentText("you need to selct.");
            alert.showAndWait();
            return;
        }

        // Récupérer les valeurs des colonnes "coltype", "coldate" et "coldescription" de la ligne sélectionnée
        String name = selectedSponsors.getName();
        String email = selectedSponsors.getEmail();
        int number = selectedSponsors.getNumber();

        // Créer la requête SQL pour supprimer la evénement de la base de données en fonction des valeurs des colonnes sélectionnées
        String sql = "DELETE FROM sponsor WHERE name = '" + name + "' AND email = '" + email + "' AND number = '" + number+ "'";

        try {
            // Exécuter la requête SQL
            Statement st = DatabaseCon.getInstance().getConnection().createStatement();
            st.executeUpdate(sql);

            // Supprimer la ligne de la table view
            tableSponsors.getItems().remove(selectedSponsors);

            // Afficher un message de confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Suppression deSponsors");
            alert.setHeaderText(null);
            alert.setContentText("La Sponsors a été supprimée avec succès.");
            alert.showAndWait();
            clearFields();
            // Fermer la connexion à la base de données
            st.close();
        } catch (SQLException e) {
            // Afficher un message d'erreur en cas d'échec de la suppression
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur lors de la suppression de Sponsors");
            alert.setHeaderText(null);
            alert.setContentText("Impossible de supprimer la Sponsors sélectionnée.");
            alert.showAndWait();
            e.printStackTrace();

        }
        clearFields();
    }
// for the supprission after it
    private void clearFields() {
        tfnom.setText("");
        emailField.setText("");
        numfield.setText("");
    }

    @FXML
    void triNomA(ActionEvent event) {

    }

    @FXML
    void triNomD(ActionEvent event) {

    }

}

