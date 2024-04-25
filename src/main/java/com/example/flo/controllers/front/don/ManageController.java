package com.example.flo.controllers.front.don;


import com.example.flo.controllers.front.MainWindowController;
import com.example.flo.entities.Don;
import com.example.flo.entities.User;
import com.example.flo.services.DonService;
import com.example.flo.utils.AlertUtils;
import com.example.flo.utils.Constants;
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
    public TextField typeTF;
    @FXML
    public TextField descriptionTF;
    @FXML
    public DatePicker dateDonDP;

    @FXML
    public ComboBox<User> userCB;

    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    Don currentDon;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        for (User user : DonService.getInstance().getAllUsers()) {
            userCB.getItems().add(user);
        }

        currentDon = ShowAllController.currentDon;

        if (currentDon != null) {
            topText.setText("Modifier don");
            btnAjout.setText("Modifier");

            try {
                typeTF.setText(currentDon.getType());
                descriptionTF.setText(currentDon.getDescription());
                dateDonDP.setValue(currentDon.getDateDon());

                userCB.setValue(currentDon.getUser());

            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter don");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent ignored) {

        if (controleDeSaisie()) {

            Don don = new Don();
            don.setType(typeTF.getText());
            don.setDescription(descriptionTF.getText());
            don.setDateDon(dateDonDP.getValue());

            don.set(userCB.getValue());

            if (currentDon == null) {
                if (DonService.getInstance().add(don)) {
                    AlertUtils.makeSuccessNotification("Don ajouté avec succés");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_DON);
                } else {
                    AlertUtils.makeError("Error");
                }
            } else {
                don.setId(currentDon.getId());
                if (DonService.getInstance().edit(don)) {
                    AlertUtils.makeSuccessNotification("Don modifié avec succés");
                    ShowAllController.currentDon = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_DON);
                } else {
                    AlertUtils.makeError("Error");
                }
            }

        }
    }


    private boolean controleDeSaisie() {


        if (typeTF.getText().isEmpty()) {
            AlertUtils.makeInformation("type ne doit pas etre vide");
            return false;
        }


        if (descriptionTF.getText().isEmpty()) {
            AlertUtils.makeInformation("description ne doit pas etre vide");
            return false;
        }


        if (dateDonDP.getValue() == null) {
            AlertUtils.makeInformation("Choisir une date pour dateDon");
            return false;
        }


        if (userCB.getValue() == null) {
            AlertUtils.makeInformation("Veuillez choisir un user");
            return false;
        }
        return true;
    }
}