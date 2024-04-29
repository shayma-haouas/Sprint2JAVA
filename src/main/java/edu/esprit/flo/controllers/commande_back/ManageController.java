package edu.esprit.flo.controllers.commande_back;


import edu.esprit.flo.controllers.MainWindowControllerBack;
import edu.esprit.flo.entities.Commande;
import edu.esprit.flo.entities.Produit;
import edu.esprit.flo.entities.User;
import edu.esprit.flo.services.CommandeService;
import edu.esprit.flo.utils.AlertUtils;
import edu.esprit.flo.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class ManageController implements Initializable {

    @FXML
    public TextField montantTF;
    @FXML
    public DatePicker datecmdDP;
    @FXML
    public TextField lieucmdTF;
    @FXML
    public TextField quantiteTF;

    @FXML
    public ComboBox<User> userCB;
    @FXML
    public ComboBox<Produit> produitCB;

    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    Commande currentCommande;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        for (User user : CommandeService.getInstance().getAllUsers()) {
            userCB.getItems().add(user);
        }

        for (Produit produit : CommandeService.getInstance().getAllProduits()) {
            produitCB.getItems().add(produit);
        }

        currentCommande = ShowAllController.currentCommande;

        if (currentCommande != null) {
            topText.setText("Modifier commande");
            btnAjout.setText("Modifier");

            try {
                montantTF.setText(String.valueOf(currentCommande.getMontant()));
                datecmdDP.setValue(currentCommande.getDatecmd());
                lieucmdTF.setText(currentCommande.getLieucmd());
                quantiteTF.setText(String.valueOf(currentCommande.getQuantite()));

                userCB.setValue(currentCommande.getUser());
                produitCB.setValue(currentCommande.getProduit());

            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter commande");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent ignored) {

        if (controleDeSaisie()) {

            Commande commande = new Commande();
            commande.setMontant(Float.parseFloat(montantTF.getText()));
            commande.setDatecmd(datecmdDP.getValue());
            commande.setLieucmd(lieucmdTF.getText());
            commande.setQuantite(Integer.parseInt(quantiteTF.getText()));

            commande.setUser(userCB.getValue());
            commande.setProduit(produitCB.getValue());

            if (currentCommande == null) {
                if (CommandeService.getInstance().add(commande)) {
                    AlertUtils.makeSuccessNotification("Commande ajouté avec succés");
                    MainWindowControllerBack.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_COMMANDE);
                } else {
                    AlertUtils.makeError("Error");
                }
            } else {
                commande.setId(currentCommande.getId());
                if (CommandeService.getInstance().edit(commande)) {
                    AlertUtils.makeSuccessNotification("Commande modifié avec succés");
                    ShowAllController.currentCommande = null;
                    MainWindowControllerBack.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_COMMANDE);
                } else {
                    AlertUtils.makeError("Error");
                }
            }

        }
    }


    private boolean controleDeSaisie() {


        if (montantTF.getText().isEmpty()) {
            AlertUtils.makeInformation("montant ne doit pas etre vide");
            return false;
        }


        try {
            Float.parseFloat(montantTF.getText());
        } catch (NumberFormatException ignored) {
            AlertUtils.makeInformation("montant doit etre un réel");
            return false;
        }
        if (datecmdDP.getValue() == null) {
            AlertUtils.makeInformation("Choisir une date pour datecmd");
            return false;
        }


        if (lieucmdTF.getText().isEmpty()) {
            AlertUtils.makeInformation("lieucmd ne doit pas etre vide");
            return false;
        }


        if (quantiteTF.getText().isEmpty()) {
            AlertUtils.makeInformation("quantite ne doit pas etre vide");
            return false;
        }


        try {
            Integer.parseInt(quantiteTF.getText());
        } catch (NumberFormatException ignored) {
            AlertUtils.makeInformation("quantite doit etre un nombre");
            return false;
        }

        if (userCB.getValue() == null) {
            AlertUtils.makeInformation("Veuillez choisir un user");
            return false;
        }
        if (produitCB.getValue() == null) {
            AlertUtils.makeInformation("Veuillez choisir un produit");
            return false;
        }
        return true;
    }
}