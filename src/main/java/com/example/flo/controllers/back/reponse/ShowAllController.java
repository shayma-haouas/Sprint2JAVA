package com.example.flo.controllers.back.reponse;

import com.example.flo.controllers.back.MainWindowController;
import com.example.flo.entities.Reponse;
import com.example.flo.services.ReponseService;
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
    
    public static Reponse currentReponse;

    @FXML
    public Text topText;
    @FXML
    public Button addButton;
    @FXML
    public VBox mainVBox;
    

    List<Reponse> listReponse;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listReponse = ReponseService.getInstance().getAll();
        
        displayData();
    }

    void displayData() {
        mainVBox.getChildren().clear();
        
        Collections.reverse(listReponse);

        if (!listReponse.isEmpty()) {
            for (Reponse reponse : listReponse) {
                
                mainVBox.getChildren().add(makeReponseModel(reponse));
                
            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeReponseModel(
            Reponse reponse
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_BACK_MODEL_REPONSE)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#descriptionText")).setText("Description : " + reponse.getDescription());
            ((Text) innerContainer.lookup("#dateajoutText")).setText("Dateajout : " + reponse.getDateajout());
            ((Text) innerContainer.lookup("#datemodifText")).setText("Datemodif : " + reponse.getDatemodif());
            
            ((Text) innerContainer.lookup("#reclamationText")).setText("Reclamation : " + reponse.getReclamation().toString());
            
            
            ((Button) innerContainer.lookup("#editButton")).setOnAction((ignored) -> modifierReponse(reponse));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((ignored) -> supprimerReponse(reponse));
            

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }
    
    @FXML
    private void ajouterReponse(ActionEvent ignored) {
        currentReponse = null;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_REPONSE);
    }

    private void modifierReponse(Reponse reponse) {
        currentReponse = reponse;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_REPONSE);
    }

    private void supprimerReponse(Reponse reponse) {
        currentReponse = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer reponse ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.isPresent()) {
            if (action.get() == ButtonType.OK) {
                if (ReponseService.getInstance().delete(reponse.getId())) {
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_REPONSE);
                } else {
                    AlertUtils.makeError("Could not delete reponse");
                }
            }
        }
    }
    
    
}
