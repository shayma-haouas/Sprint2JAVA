package com.example.flo.controllers.back.reclamation;


import com.example.flo.controllers.back.MainWindowController;
import com.example.flo.entities.Reclamation;
import com.example.flo.entities.User;
import com.example.flo.services.ReclamationService;
import com.example.flo.utils.AlertUtils;
import com.example.flo.utils.BadWords;
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
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ManageController implements Initializable {

    @FXML
    public TextField typeTF;
    @FXML
    public TextField descriptionTF;
    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    Reclamation currentReclamation;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        currentReclamation = ShowAllController.currentReclamation;

        if (currentReclamation != null) {
            topText.setText("Modifier reclamation");
            btnAjout.setText("Modifier");

            try {
                typeTF.setText(currentReclamation.getType());
                descriptionTF.setText(currentReclamation.getDescription());
            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter reclamation");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent ignored) {

        if (controleDeSaisie()) {

            Reclamation reclamation = new Reclamation();
            reclamation.setType(typeTF.getText());
            reclamation.setDescription(descriptionTF.getText());
            reclamation.setDatemodif(LocalDate.now());

            if (currentReclamation == null) {
                reclamation.setDateajout(LocalDate.now());

                if (ReclamationService.getInstance().add(reclamation)) {
                    AlertUtils.successNotification("Reclamation ajouté avec succés");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_RECLAMATION);
                } else {
                    AlertUtils.errorNotification("Error");
                }
            } else {
                reclamation.setId(currentReclamation.getId());
                if (ReclamationService.getInstance().edit(reclamation)) {
                    AlertUtils.successNotification("Reclamation modifié avec succés");
                    ShowAllController.currentReclamation = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_RECLAMATION);
                } else {
                    AlertUtils.errorNotification("Error");
                }
            }

        }
    }


    private boolean controleDeSaisie() {


        if (typeTF.getText().isEmpty()) {
            AlertUtils.informationNotification("type ne doit pas etre vide");
            return false;
        }

        if (BadWords.filterText(typeTF.getText())) {
            AlertUtils.informationNotification("type ne doit pas contenir des mots interdits");
            return false;
        }

        if (descriptionTF.getText().isEmpty()) {
            AlertUtils.informationNotification("description ne doit pas etre vide");
            return false;
        }

        if (BadWords.filterText(descriptionTF.getText())) {
            AlertUtils.informationNotification("description ne doit pas contenir des mots interdits");
            return false;
        }

        return true;
    }
}