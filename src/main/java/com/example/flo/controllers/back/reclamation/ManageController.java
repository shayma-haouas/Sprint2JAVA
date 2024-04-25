package com.example.flo.controllers.back.reclamation;


import com.example.flo.entities.Reclamation;
import com.example.flo.controllers.back.MainWindowController;
import com.example.flo.services.ReclamationService;
import com.example.flo.utils.AlertUtils;
import com.example.flo.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.text.Text;

import com.example.flo.entities.User;

import java.net.URL;

import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ManageController implements Initializable {

    @FXML
    public TextField typeTF;
    @FXML
    public TextField descriptionTF;
    @FXML
    public DatePicker dateajoutDP;
    @FXML
    public DatePicker datemodifDP;
    
    @FXML
    public ComboBox<User> userCB;
    
    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    Reclamation currentReclamation;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        for (User user : ReclamationService.getInstance().getAllUsers()) {
            userCB.getItems().add(user);
        }
        
        currentReclamation = ShowAllController.currentReclamation;

        if (currentReclamation != null) {
            topText.setText("Modifier reclamation");
            btnAjout.setText("Modifier");
            
            try {
                typeTF.setText(currentReclamation.getType());
                descriptionTF.setText(currentReclamation.getDescription());
                dateajoutDP.setValue(currentReclamation.getDateajout());
                datemodifDP.setValue(currentReclamation.getDatemodif());
                
                userCB.setValue(currentReclamation.getUser());
                
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
            reclamation.setDateajout(dateajoutDP.getValue());
            reclamation.setDatemodif(datemodifDP.getValue());
            
            reclamation.setUser(userCB.getValue());

            if (currentReclamation == null) {
                if (ReclamationService.getInstance().add(reclamation)) {
                     AlertUtils.makeSuccessNotification("Reclamation ajouté avec succés");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_RECLAMATION);
                } else {
                    AlertUtils.makeError("Error");
                }
            } else {
                reclamation.setId(currentReclamation.getId());
                if (ReclamationService.getInstance().edit(reclamation)) {
                      AlertUtils.makeSuccessNotification("Reclamation modifié avec succés");
                    ShowAllController.currentReclamation = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_RECLAMATION);
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
        
        
        
        if (dateajoutDP.getValue() == null){
            AlertUtils.makeInformation("Choisir une date pour dateajout");
            return false;
        }
        
        
        if (datemodifDP.getValue() == null){
            AlertUtils.makeInformation("Choisir une date pour datemodif");
            return false;
        }
        
        
        if (userCB.getValue() == null) {
            AlertUtils.makeInformation("Veuillez choisir un user");
            return false;
        }
        return true;
    }
}