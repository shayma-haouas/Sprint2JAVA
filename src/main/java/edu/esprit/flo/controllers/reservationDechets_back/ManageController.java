package edu.esprit.flo.controllers.reservationDechets_back;


import edu.esprit.flo.controllers.MainWindowControllerBack;
import edu.esprit.flo.entities.Dechets;
import edu.esprit.flo.entities.ReservationDechets;
import edu.esprit.flo.entities.User;
import edu.esprit.flo.services.DechetsService;
import edu.esprit.flo.services.ReservationDechetsService;
import edu.esprit.flo.utils.AlertUtils;
import edu.esprit.flo.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ManageController implements Initializable {

    @FXML
    public DatePicker dateDP;
    @FXML
    public DatePicker dateRamassageDP;
    @FXML
    public TextField nomFournisseurTF;
    @FXML
    public TextField numeroTellTF;
    @FXML
    public TextField quantiteTF;

    @FXML
    public ComboBox<User> userCB;
    @FXML
    public ComboBox<Dechets> dechetsCB;

    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    ReservationDechets currentReservationDechets;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        for (User user : ReservationDechetsService.getInstance().getAllUsers()) {
            userCB.getItems().add(user);
        }

        for (Dechets dechets : DechetsService.getInstance().getAll()) {
            dechetsCB.getItems().add(dechets);
        }

        currentReservationDechets = ShowAllController.currentReservationDechets;

        if (currentReservationDechets != null) {
            topText.setText("Modifier reservationDechets");
            btnAjout.setText("Modifier");

            try {
                dateDP.setValue(currentReservationDechets.getDate());
                dateRamassageDP.setValue(currentReservationDechets.getDateRamassage());
                nomFournisseurTF.setText(currentReservationDechets.getNomFournisseur());
                numeroTellTF.setText(currentReservationDechets.getNumeroTell());
                quantiteTF.setText(String.valueOf(currentReservationDechets.getQuantite()));

                userCB.setValue(currentReservationDechets.getUser());
                dechetsCB.setValue(currentReservationDechets.getDechets());
            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter reservationDechets");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent ignored) {

        if (controleDeSaisie()) {

            ReservationDechets reservationDechets = new ReservationDechets();
            reservationDechets.setDate(dateDP.getValue());
            reservationDechets.setDateRamassage(dateRamassageDP.getValue());
            reservationDechets.setNomFournisseur(nomFournisseurTF.getText());
            reservationDechets.setNumeroTell(numeroTellTF.getText());
            reservationDechets.setQuantite(Integer.parseInt(quantiteTF.getText()));

            reservationDechets.setUser(userCB.getValue());
            reservationDechets.setDechets(dechetsCB.getValue());

            if (currentReservationDechets == null) {
                if (ReservationDechetsService.getInstance().add(reservationDechets)) {
                    AlertUtils.makeSuccessNotification("ReservationDechets ajouté avec succés");
                    MainWindowControllerBack.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_RESERVATION_DECHETS);
                } else {
                    AlertUtils.makeError("Error");
                }
            } else {
                reservationDechets.setId(currentReservationDechets.getId());
                if (ReservationDechetsService.getInstance().edit(reservationDechets)) {
                    AlertUtils.makeSuccessNotification("ReservationDechets modifié avec succés");
                    ShowAllController.currentReservationDechets = null;
                    MainWindowControllerBack.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_RESERVATION_DECHETS);
                } else {
                    AlertUtils.makeError("Error");
                }
            }

        }
    }


    private boolean controleDeSaisie() {


        if (dateDP.getValue() == null) {
            AlertUtils.makeInformation("Choisir une date pour date");
            return false;
        }


        if (dateRamassageDP.getValue() == null) {
            AlertUtils.makeInformation("Choisir une date pour dateRamassage");
            return false;
        }


        if (nomFournisseurTF.getText().isEmpty()) {
            AlertUtils.makeInformation("nomFournisseur ne doit pas etre vide");
            return false;
        }


        if (numeroTellTF.getText().isEmpty()) {
            AlertUtils.makeInformation("numeroTell ne doit pas etre vide");
            return false;
        }


        if (quantiteTF.getText().isEmpty()) {
            AlertUtils.makeInformation("quantite ne doit pas etre vide");
            return false;
        }


        try {
            Integer.parseInt(quantiteTF.getText());
        } catch (NumberFormatException ignored) {
            AlertUtils.makeInformation("quantite doit etre un nombre");
            return false;
        }

        if (userCB.getValue() == null) {
            AlertUtils.makeInformation("Veuillez choisir un user");
            return false;
        }

        if (dechetsCB.getValue() == null) {
            AlertUtils.makeInformation("Veuillez choisir un dechet");
            return false;
        }

        return true;
    }
}