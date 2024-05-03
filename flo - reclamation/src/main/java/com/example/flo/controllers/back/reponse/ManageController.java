package com.example.flo.controllers.back.reponse;


import com.example.flo.controllers.back.MainWindowController;
import com.example.flo.entities.Reclamation;
import com.example.flo.entities.Reponse;
import com.example.flo.services.ReponseService;
import com.example.flo.utils.AlertUtils;
import com.example.flo.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ManageController implements Initializable {

    @FXML
    public TextField descriptionTF;
    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    Reponse currentReponse;

    public static Reclamation selectedReclamation;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        currentReponse = ShowAllController.currentReponse;

        if (currentReponse != null) {
            topText.setText("Modifier reponse");
            btnAjout.setText("Modifier");

            try {
                descriptionTF.setText(currentReponse.getDescription());
            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter reponse");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent ignored) {

        if (controleDeSaisie()) {

            Reponse reponse = new Reponse();
            reponse.setDescription(descriptionTF.getText());
            reponse.setDatemodif(LocalDate.now());

            reponse.setReclamation(selectedReclamation);

            if (currentReponse == null) {
                reponse.setDateajout(LocalDate.now());

                if (ReponseService.getInstance().add(reponse)) {
                    AlertUtils.makeSuccessNotification("Reponse ajouté avec succés");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_REPONSE);
                } else {
                    AlertUtils.makeError("Error");
                }
            } else {
                reponse.setId(currentReponse.getId());
                if (ReponseService.getInstance().edit(reponse)) {
                    AlertUtils.makeSuccessNotification("Reponse modifié avec succés");
                    ShowAllController.currentReponse = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_REPONSE);
                } else {
                    AlertUtils.makeError("Error");
                }
            }

        }
    }


    private boolean controleDeSaisie() {
        if (descriptionTF.getText().isEmpty()) {
            AlertUtils.makeInformation("description ne doit pas etre vide");
            return false;
        }
        return true;
    }
}