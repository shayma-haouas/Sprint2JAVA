package com.example.flo.controllers.back.reponse;

import com.example.flo.controllers.back.MainWindowController;
import com.example.flo.entities.Reponse;
import com.example.flo.services.ReponseService;
import com.example.flo.utils.AlertUtils;
import com.example.flo.utils.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ShowAllController implements Initializable {

    public static Reponse currentReponse;

    @FXML
    public Text topText;
    @FXML
    public VBox mainVBox;
    @FXML
    public TextField searchTF;


    List<Reponse> listReponse;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listReponse = ReponseService.getInstance().getAll();

        displayData("");
    }

    void displayData(String searchText) {
        mainVBox.getChildren().clear();

        Collections.reverse(listReponse);

        listReponse.stream()
                .filter(reponse -> searchText.isEmpty()
                        || reponse.getDescription().toLowerCase().contains(searchText.toLowerCase()))
                .map(this::makeReponseModel)
                .forEach(mainVBox.getChildren()::add);

        if (listReponse.isEmpty()) {
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

    private void modifierReponse(Reponse reponse) {
        currentReponse = reponse;
        ManageController.selectedReclamation = null;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_REPONSE);
    }

    private void supprimerReponse(Reponse reponse) {
        ManageController.selectedReclamation = null;

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
                    AlertUtils.errorNotification("Could not delete reponse");
                }
            }
        }
    }

    @FXML
    public void search(KeyEvent keyEvent) {
        displayData(searchTF.getText());
    }
}
