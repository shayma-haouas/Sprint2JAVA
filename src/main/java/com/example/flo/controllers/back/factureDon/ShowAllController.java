package com.example.flo.controllers.back.factureDon;

import com.example.flo.controllers.back.MainWindowController;
import com.example.flo.entities.FactureDon;
import com.example.flo.services.FactureDonService;
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

    public static FactureDon currentFactureDon;

    @FXML
    public Text topText;
    @FXML
    public VBox mainVBox;


    List<FactureDon> listFactureDon;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listFactureDon = FactureDonService.getInstance().getAll();

        displayData();
    }

    void displayData() {
        mainVBox.getChildren().clear();

        Collections.reverse(listFactureDon);

        if (!listFactureDon.isEmpty()) {
            for (FactureDon factureDon : listFactureDon) {

                mainVBox.getChildren().add(makeFactureDonModel(factureDon));

            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeFactureDonModel(
            FactureDon factureDon
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_BACK_MODEL_FACTURE_DON)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#nomDonateurText")).setText("NomDonateur : " + factureDon.getNomDonateur());
            ((Text) innerContainer.lookup("#prenomDonateurText")).setText("PrenomDonateur : " + factureDon.getPrenomDonateur());
            ((Text) innerContainer.lookup("#emailText")).setText("Email : " + factureDon.getEmail());
            ((Text) innerContainer.lookup("#adressesText")).setText("Adresses : " + factureDon.getAdresses());
            ((Text) innerContainer.lookup("#numeroTelephoneText")).setText("NumeroTelephone : " + factureDon.getNumeroTelephone());
            ((Text) innerContainer.lookup("#descriptionText")).setText("Description : " + factureDon.getDescription());

            ((Text) innerContainer.lookup("#donText")).setText("Don : " + factureDon.getDon().toString());


            ((Button) innerContainer.lookup("#editButton")).setOnAction((event) -> modifierFactureDon(factureDon));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((event) -> supprimerFactureDon(factureDon));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    private void modifierFactureDon(FactureDon factureDon) {
        currentFactureDon = factureDon;
        MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_FACTURE_DON);
    }

    private void supprimerFactureDon(FactureDon factureDon) {
        currentFactureDon = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer factureDon ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.isPresent()) {
            if (action.get() == ButtonType.OK) {
                if (FactureDonService.getInstance().delete(factureDon.getId())) {
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_FACTURE_DON);
                } else {
                    AlertUtils.makeError("Could not delete factureDon");
                }
            }
        }
    }


}
