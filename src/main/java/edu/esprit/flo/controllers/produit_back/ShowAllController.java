package edu.esprit.flo.controllers.produit_back;

import edu.esprit.flo.controllers.MainWindowControllerBack;
import edu.esprit.flo.entities.Produit;
import edu.esprit.flo.services.ProduitService;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;

public class ShowAllController implements Initializable {

    public static Produit currentProduit;

    @FXML
    public Text topText;
    @FXML
    public Button addButton;
    @FXML
    public VBox mainVBox;


    List<Produit> listProduit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listProduit = ProduitService.getInstance().getAll();

        displayData();
    }

    void displayData() {
        mainVBox.getChildren().clear();

        Collections.reverse(listProduit);

        if (!listProduit.isEmpty()) {
            for (Produit produit : listProduit) {

                mainVBox.getChildren().add(makeProduitModel(produit));

            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeProduitModel(
            Produit produit
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_BACK_MODEL_PRODUIT)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#nompText")).setText("Nomp : " + produit.getNomp());
            ((Text) innerContainer.lookup("#descpText")).setText("Descp : " + produit.getDescp());
            ((Text) innerContainer.lookup("#catgText")).setText("Catg : " + produit.getCatg());
            ((Text) innerContainer.lookup("#prixText")).setText("Prix : " + produit.getPrix());


            Path selectedImagePath = FileSystems.getDefault().getPath(produit.getImage());
            if (selectedImagePath.toFile().exists()) {
                ((ImageView) innerContainer.lookup("#imageIV")).setImage(new Image(selectedImagePath.toUri().toString()));
            }

            ((Button) innerContainer.lookup("#editButton")).setOnAction((event) -> modifierProduit(produit));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((event) -> supprimerProduit(produit));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    @FXML
    private void ajouterProduit(ActionEvent ignored) {
        currentProduit = null;
        MainWindowControllerBack.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_PRODUIT);
    }

    private void modifierProduit(Produit produit) {
        currentProduit = produit;
        MainWindowControllerBack.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_PRODUIT);
    }

    private void supprimerProduit(Produit produit) {
        currentProduit = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer produit ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.isPresent()) {
            if (action.get() == ButtonType.OK) {
                if (ProduitService.getInstance().delete(produit.getId())) {
                    MainWindowControllerBack.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_PRODUIT);
                } else {
                    AlertUtils.makeError("Could not delete produit");
                }
            }
        }
    }


}
