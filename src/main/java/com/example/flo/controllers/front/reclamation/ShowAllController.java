package com.example.flo.controllers.front.reclamation;

import com.example.flo.controllers.front.MainWindowController;
import com.example.flo.entities.Reclamation;
import com.example.flo.services.ReclamationService;
import com.example.flo.utils.AlertUtils;
import com.example.flo.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
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
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_FRONT_MODEL_RECLAMATION)));

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
        MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_MANAGE_RECLAMATION);
    }

    private void modifierReclamation(Reclamation reclamation) {
        currentReclamation = reclamation;
        MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_MANAGE_RECLAMATION);
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
                    MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_RECLAMATION);
                } else {
                    AlertUtils.errorNotification("Could not delete reclamation");
                }
            }
        }
    }


}
