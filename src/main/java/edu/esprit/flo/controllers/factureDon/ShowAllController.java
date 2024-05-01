package edu.esprit.flo.controllers.factureDon;

import edu.esprit.flo.controllers.MainWindowController;
import edu.esprit.flo.entities.FactureDon;
import edu.esprit.flo.services.FactureDonService;
import edu.esprit.flo.utils.AlertUtils;
import edu.esprit.flo.utils.Constants;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ShowAllController implements Initializable {

    public static FactureDon currentFactureDon;

    @FXML
    public Text topText;
    @FXML
    public Button addButton;
    @FXML
    public VBox mainVBox;
    @FXML


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
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_FRONT_MODEL_FACTURE_DON)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#nomDonateurText")).setText("NomDonateur : " + factureDon.getNomDonateur());
            ((Text) innerContainer.lookup("#prenomDonateurText")).setText("PrenomDonateur : " + factureDon.getPrenomDonateur());
            ((Text) innerContainer.lookup("#emailText")).setText("Email : " + factureDon.getEmail());
            ((Text) innerContainer.lookup("#adressesText")).setText("Adresses : " + factureDon.getAdresses());
            ((Text) innerContainer.lookup("#numeroTelephoneText")).setText("NumeroTelephone : " + factureDon.getNumeroTelephone());
            ((Text) innerContainer.lookup("#descriptionText")).setText("Description : " + factureDon.getDescription());

            ((Text) innerContainer.lookup("#donText")).setText("Don : " + factureDon.getDon().toString());

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    @FXML
    private void ajouterFactureDon(ActionEvent ignored) {
        currentFactureDon = null;
        MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_MANAGE_FACTURE_DON);
    }

}
