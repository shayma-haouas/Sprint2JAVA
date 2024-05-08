package edu.esprit.flo.controllers.commande_back;

import edu.esprit.flo.controllers.MainWindowControllerBack;
import edu.esprit.flo.entities.Commande;
import edu.esprit.flo.services.CommandeService;
import edu.esprit.flo.utils.AlertUtils;
import edu.esprit.flo.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ShowAllController implements Initializable {

    public static Commande currentCommande;

    @FXML
    public Text topText;
    @FXML
    public Button addButton;
    @FXML
    public VBox mainVBox;
    @FXML
    public ComboBox<String> sortCB;


    List<Commande> listCommande;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listCommande = CommandeService.getInstance().getAll();
        sortCB.getItems().addAll("Tri par montant", "Tri par date", "Tri par lieu", "Tri par quantite", "Tri par produit");

        displayData();
    }

    void displayData() {
        mainVBox.getChildren().clear();

        Collections.reverse(listCommande);

        if (!listCommande.isEmpty()) {
            for (Commande commande : listCommande) {

                mainVBox.getChildren().add(makeCommandeModel(commande));

            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeCommandeModel(
            Commande commande
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_BACK_MODEL_COMMANDE)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#montantText")).setText("Montant : " + commande.getMontant());
            ((Text) innerContainer.lookup("#datecmdText")).setText("Datecmd : " + commande.getDatecmd());
            ((Text) innerContainer.lookup("#lieucmdText")).setText("Lieucmd : " + commande.getLieucmd());
            ((Text) innerContainer.lookup("#quantiteText")).setText("Quantite : " + commande.getQuantite());
            ((Text) innerContainer.lookup("#userText")).setText("User : " + commande.getUser().toString());
            ((Text) innerContainer.lookup("#produitText")).setText("Produit : " + commande.getProduit().toString());

            ((Button) innerContainer.lookup("#editButton")).setOnAction((event) -> modifierCommande(commande));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((event) -> supprimerCommande(commande));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }


    private void modifierCommande(Commande commande) {
        currentCommande = commande;
        MainWindowControllerBack.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_COMMANDE);
    }

    private void supprimerCommande(Commande commande) {
        currentCommande = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer commande ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.isPresent()) {
            if (action.get() == ButtonType.OK) {
                if (CommandeService.getInstance().delete(commande.getId())) {
                    MainWindowControllerBack.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_COMMANDE);
                } else {
                    AlertUtils.makeError("Could not delete commande");
                }
            }
        }
    }

    public void sort(ActionEvent actionEvent) {
        Commande.compareVar = sortCB.getValue();
        listCommande.stream().sorted(Commande::compareTo);
        displayData();
    }
}
