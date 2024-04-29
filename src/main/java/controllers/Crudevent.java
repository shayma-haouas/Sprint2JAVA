package controllers;

import entities.Evenement;
import entities.Sponsor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceEvenement;
import services.ServiceSpon;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Crudevent implements Initializable {
    private ServiceEvenement ecrd = new ServiceEvenement();
    public ObservableList<Evenement> data = FXCollections.observableArrayList();
    public ObservableList<Sponsor> data1 = FXCollections.observableArrayList();

    ObservableList<Evenement> list;

    String path="";


    @FXML
    private TextField tfnameEV;

    @FXML
    private TextField tflieu;

    @FXML
    private TextField tftype;

    @FXML
    private TextField tfdescription;

    @FXML
    private TextField tfnbParticipant;

    @FXML
    private DatePicker tfdatedebut;

    @FXML
    private DatePicker tfdatefin;





    @FXML
    private Button btnsupprimer;

    @FXML
    private Button browseimg;

    @FXML
    private ComboBox<String> cbsponsor;

    @FXML
    private Button btnrf;

    @FXML
    private TextField tfsp;

    @FXML
    private Button btnajouterev;

    @FXML
    private ImageView image2;

    @FXML
    private Button btnsoponsor;

    @FXML
    private Button btnev;

    @FXML
    private ImageView image1;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Récupérer les sponsors de la base de données et les ajouter à la liste de noms de sponsors
        ServiceSpon cd = new ServiceSpon();
        List<Sponsor> sponsors = cd.affichersponsor();

        // Créer une liste de noms de sponsors
        List<String> sponsorNames = new ArrayList<>();
        for (Sponsor sponsor : sponsors) {
            sponsorNames.add(sponsor.getName());
        }

        // Définir la liste des noms de sponsors comme éléments du ComboBox
        cbsponsor.setItems(FXCollections.observableArrayList(sponsorNames));

        // Ajouter un gestionnaire d'événements pour le ComboBox
        cbsponsor.setOnAction(event -> selectsponsor(event));
    }
    @FXML
    void ajouterev(ActionEvent event) {
        try {
            // Vérifier que tous les champs sont remplis
            if (cbsponsor.getValue() == null || tfnameEV.getText().isEmpty() || tflieu.getText().isEmpty()
                    || tftype.getText().isEmpty() || tfdescription.getText().isEmpty()
                    || tfdatedebut.getValue() == null || tfdatefin.getValue() == null
                    || tfnbParticipant.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Champs vides");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs !");
                alert.showAndWait();
                return;
            }

            // Récupérer sponsor
            String sponsor = cbsponsor.getValue();

            // Récupérer les valeurs des champs
            String nom = tfnameEV.getText();
            String lieu = tflieu.getText();
            String type = tftype.getText();
            String description = tfdescription.getText();
            ////////Description ne depasse pas 200 caractere ////
            if (tfdescription.getText().length() > 200) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Description trop longue");
                alert.setHeaderText(null);
                alert.setContentText("La description ne doit pas dépasser 200 caractères !");
                alert.showAndWait();
                return;
            }

            // Récupérer la valeur sélectionnée dans le composant DatePicker
            LocalDate datedebut1 = tfdatedebut.getValue();
            LocalDate datefin1 = tfdatefin.getValue();

            // Vérifier que la date de début est avant la date de fin
            if (datefin1.isBefore(datedebut1)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Date invalide");
                alert.setHeaderText(null);
                alert.setContentText("La date de fin doit être après la date de début !");
                alert.showAndWait();
                return;
            }

            // Convertir les dates en chaînes
            String datedebut = datedebut1.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            String datefin = datefin1.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            // Convertir les chaînes de date en valeurs de date MySQL
            String mysqlDateString = LocalDate.parse(datedebut, DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString();
            String mysqlDateString11 = LocalDate.parse(datefin, DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString();

            // Récupérer les autres champs
            String image = path;
            image2.setImage(new Image("file:" + image));


            // Vérifier que les champs pour le nombre de participants est un nombre entier
            int nb_participant = 0;

            try {
                nb_participant = Integer.parseInt(tfnbParticipant.getText());
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Champs invalides");
                alert.setHeaderText(null);
                alert.setContentText("Le champ pour le nombre de participants doit être un nombre entier !");
                alert.showAndWait();
                return;
            }

            // Vérifier que le nombre de participants est supérieur à zéro
            if (nb_participant <= 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Champs invalides");
                alert.setHeaderText(null);
                alert.setContentText("Le nombre de participants doit être supérieur à zéro !");
                alert.showAndWait();
                return;
            }

            // Get sponsor ID
            int sponsorId = ecrd.getIdSponsor(sponsor);

            // Add the event to the database
            ecrd.ajouterEvenement(new Evenement(sponsorId, nom, type,datedebut, datefin,description,nb_participant,lieu, image ));

            // Afficher un message de confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("L evenement a été ajoutée avec succès");
            alert.showAndWait();

            // Effacer les zones de texte
            cbsponsor.setValue("");
        } catch (NumberFormatException ex) {
            // Afficher une alerte si la zone de texte idConducteur ne contient pas un entier

        }
    }

    @FXML
    void ajouterimage(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            // load the selected image into the image view
            path=selectedFile.getAbsolutePath();
            Image image = new Image(selectedFile.toURI().toString());
            image2.setImage(image);
        }


    }


    @FXML
    void gererevenement(ActionEvent event) {

    }

    @FXML
    void gerersponsor(ActionEvent event) {
        try {
            Parent SponsorParent = FXMLLoader.load(getClass().getResource("/eventback/sponsor.fxml"));
            Scene SponsorScene = new Scene(SponsorParent);
            Stage window = (Stage)(((Button)event.getSource()).getScene().getWindow());
            window.setScene(SponsorScene);
            window.show();
        } catch (IOException e) {
        }
    }



    @FXML
    void modifierev(ActionEvent event) {

    }

    @FXML
    void rafraichir(ActionEvent event) {

    }

    @FXML
    void selectedEvent(MouseEvent event) {

    }

    @FXML
    void selectsponsor(ActionEvent event) {
        String selectedSponsor = cbsponsor.getValue();
        // do something with the selected conducteur
        System.out.println("Selected sponsor: " + selectedSponsor);
    }

    @FXML
    void supprimerev(ActionEvent event) {

    }

}