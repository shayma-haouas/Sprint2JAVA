package controllers;

import entities.Evenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class eventback {

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
    private TableView<Evenement> tableEvenement;

    @FXML
    private TableColumn<Evenement, Integer> callidev;

    @FXML
    private TableColumn<Evenement, String> callnomev;

    @FXML
    private TableColumn<Evenement, String> calltype;

    @FXML
    private TableColumn<Evenement, String> calldescription;

    @FXML
    private TableColumn<Evenement, String> calldatedebut;

    @FXML
    private TableColumn<Evenement, String> calldatefin;

    @FXML
    private TableColumn<Evenement, Integer> callnbparticipant;

    @FXML
    private TableColumn<Evenement, String> calllieu;

    @FXML
    private TableColumn<Evenement, String> callimage;

    @FXML
    private TableColumn<Evenement, Integer> callids;

    @FXML
    private Button btnmodifier;

    @FXML
    private Button btnsupprimer;

    @FXML
    private Button browseimg;

    @FXML
    private ComboBox<?> cbsponsor;

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

    @FXML
    void ajouterev(ActionEvent event) {

    }

    @FXML
    void ajouterimage(ActionEvent event) {

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

    }

    @FXML
    void supprimerev(ActionEvent event) {

    }

}