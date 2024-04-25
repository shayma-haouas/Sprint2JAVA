package com.example.flo.controllers.back.don;

import com.example.flo.controllers.back.MainWindowController;
import com.example.flo.entities.Don;
import com.example.flo.services.DonService;
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

    public static Don currentDon;

    @FXML
    public Text topText;
    @FXML
    public VBox mainVBox;


    List<Don> listDon;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listDon = DonService.getInstance().getAll();

        displayData();
    }

    void displayData() {
        mainVBox.getChildren().clear();

        Collections.reverse(listDon);

        if (!listDon.isEmpty()) {
            for (Don don : listDon) {

                mainVBox.getChildren().add(makeDonModel(don));

            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeDonModel(
            Don don
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_BACK_MODEL_DON)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#typeText")).setText("Type : " + don.getType());
            ((Text) innerContainer.lookup("#descriptionText")).setText("Description : " + don.getDescription());
            ((Text) innerContainer.lookup("#dateDonText")).setText("DateDon : " + don.getDateDon());

            ((Text) innerContainer.lookup("#userText")).setText("User : " + don.getUser().toString());


            ((Button) innerContainer.lookup("#editButton")).setOnAction((event) -> modifierDon(don));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((event) -> supprimerDon(don));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    private void modifierDon(Don don) {
        currentDon = don;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_DON);
    }

    private void supprimerDon(Don don) {
        currentDon = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer don ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.isPresent()) {
            if (action.get() == ButtonType.OK) {
                if (DonService.getInstance().delete(don.getId())) {
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_DON);
                } else {
                    AlertUtils.makeError("Could not delete don");
                }
            }
        }
    }


}
