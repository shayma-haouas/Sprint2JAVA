package com.example.flo.controllers.back.reclamation;

import com.example.flo.controllers.back.MainWindowController;
import com.example.flo.entities.Reclamation;
import com.example.flo.services.ReclamationService;
import com.example.flo.utils.AlertUtils;
import com.example.flo.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.input.KeyEvent;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;

public class ShowAllController implements Initializable {
    
    public static Reclamation currentReclamation;

    @FXML
    public Text topText;
    @FXML
    public Button addButton;
    @FXML
    public VBox mainVBox;
    

    List<Reclamation> listReclamation;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listReclamation = ReclamationService.getInstance().getAll();
        
        displayData();
    }

    void displayData() {
        mainVBox.getChildren().clear();
        
        Collections.reverse(listReclamation);

        if (!listReclamation.isEmpty()) {
            for (Reclamation reclamation : listReclamation) {
                
                mainVBox.getChildren().add(makeReclamationModel(reclamation));
                
            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeReclamationModel(
            Reclamation reclamation
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_BACK_MODEL_RECLAMATION)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#typeText")).setText("Type : " + reclamation.getType());
            ((Text) innerContainer.lookup("#descriptionText")).setText("Description : " + reclamation.getDescription());
            ((Text) innerContainer.lookup("#dateajoutText")).setText("Dateajout : " + reclamation.getDateajout());
            ((Text) innerContainer.lookup("#datemodifText")).setText("Datemodif : " + reclamation.getDatemodif());
            
            ((Text) innerContainer.lookup("#userText")).setText("User : " + reclamation.getUser().toString());
            
            
            ((Button) innerContainer.lookup("#editButton")).setOnAction((ignored) -> modifierReclamation(reclamation));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((ignored) -> supprimerReclamation(reclamation));
            

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }
    
    @FXML
    private void ajouterReclamation(ActionEvent ignored) {
        currentReclamation = null;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_RECLAMATION);
    }

    private void modifierReclamation(Reclamation reclamation) {
        currentReclamation = reclamation;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_RECLAMATION);
    }

    private void supprimerReclamation(Reclamation reclamation) {
        currentReclamation = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer reclamation ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.isPresent()) {
            if (action.get() == ButtonType.OK) {
                if (ReclamationService.getInstance().delete(reclamation.getId())) {
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_RECLAMATION);
                } else {
                    AlertUtils.makeError("Could not delete reclamation");
                }
            }
        }
    }
    
    
}
