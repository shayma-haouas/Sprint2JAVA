package com.example.flo.controllers.back.reponse;


import com.example.flo.entities.Reponse;
import com.example.flo.controllers.back.MainWindowController;
import com.example.flo.services.ReponseService;
import com.example.flo.utils.AlertUtils;
import com.example.flo.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.text.Text;

import com.example.flo.entities.Reclamation;

import java.net.URL;

import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ManageController implements Initializable {

    @FXML
    public TextField descriptionTF;
    @FXML
    public DatePicker dateajoutDP;
    @FXML
    public DatePicker datemodifDP;
    
    @FXML
    public ComboBox<Reclamation> reclamationCB;
    
    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    Reponse currentReponse;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        for (Reclamation reclamation : ReponseService.getInstance().getAllReclamations()) {
            reclamationCB.getItems().add(reclamation);
        }
        
        currentReponse = ShowAllController.currentReponse;

        if (currentReponse != null) {
            topText.setText("Modifier reponse");
            btnAjout.setText("Modifier");
            
            try {
                descriptionTF.setText(currentReponse.getDescription());
                dateajoutDP.setValue(currentReponse.getDateajout());
                datemodifDP.setValue(currentReponse.getDatemodif());
                
                reclamationCB.setValue(currentReponse.getReclamation());
                
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
            reponse.setDateajout(dateajoutDP.getValue());
            reponse.setDatemodif(datemodifDP.getValue());
            
            reponse.setReclamation(reclamationCB.getValue());

            if (currentReponse == null) {
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
        
        
        
        if (dateajoutDP.getValue() == null){
            AlertUtils.makeInformation("Choisir une date pour dateajout");
            return false;
        }
        
        
        if (datemodifDP.getValue() == null){
            AlertUtils.makeInformation("Choisir une date pour datemodif");
            return false;
        }
        
        
        if (reclamationCB.getValue() == null) {
            AlertUtils.makeInformation("Veuillez choisir un reclamation");
            return false;
        }
        return true;
    }
}