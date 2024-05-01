package edu.esprit.flo.controllers.dechets_back;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import edu.esprit.flo.controllers.MainWindowControllerBack;
import edu.esprit.flo.entities.Dechets;
import edu.esprit.flo.services.DechetsService;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;

public class ShowAllController implements Initializable {

    public static Dechets currentDechets;

    @FXML
    public Text topText;
    @FXML
    public Button addButton;
    @FXML
    public VBox mainVBox;
    @FXML
    private TextField searchField;



    @FXML
    private Button trie;

    @FXML
    private Button trie1;

    List<Dechets> listDechets;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listDechets = DechetsService.getInstance().getAll();
        displayData();


        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.isEmpty()) {
                    List<Dechets> searchResults = DechetsService.getInstance().search(newValue);
                    if (!searchResults.isEmpty()) {
                        listDechets.clear();
                        listDechets.addAll(searchResults);
                        displayData();
                    } else {
                        AlertUtils.makeInformation("No matching results found.");
                    }
                } else {
                    // If the search field is empty, display all data
                    listDechets.clear();
                    listDechets.addAll(DechetsService.getInstance().getAll());
                    displayData();
                }
            }
        });




}

    void displayData() {
        mainVBox.getChildren().clear();

        Collections.reverse(listDechets);

        if (!listDechets.isEmpty()) {
            for (Dechets dechets : listDechets) {

                mainVBox.getChildren().add(makeDechetsModel(dechets));

            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune Donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeDechetsModel(
            Dechets dechets
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_BACK_MODEL_DECHETS)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#typeText")).setText("Type : " + dechets.getType());
            ((Text) innerContainer.lookup("#dateEntreText")).setText("DateEntre : " + dechets.getDateEntre());
            ((Text) innerContainer.lookup("#descriptionText")).setText("Description : " + dechets.getDescription());
            ((Text) innerContainer.lookup("#quantiteText")).setText("Quantite : " + dechets.getQuantite());


            Path selectedImagePath = FileSystems.getDefault().getPath(dechets.getImage());
            if (selectedImagePath.toFile().exists()) {
                ((ImageView) innerContainer.lookup("#imageIV")).setImage(new Image(selectedImagePath.toUri().toString()));
            }

            ((Button) innerContainer.lookup("#editButton")).setOnAction((event) -> modifierDechets(dechets));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((event) -> supprimerDechets(dechets));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    @FXML
    private void ajouterDechets(ActionEvent ignored) {
        currentDechets = null;
        MainWindowControllerBack.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_DECHETS);
    }

    private void modifierDechets(Dechets dechets) {
        currentDechets = dechets;
        MainWindowControllerBack.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_DECHETS);
    }

    private void supprimerDechets(Dechets dechets) {
        currentDechets = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer dechets ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.isPresent()) {
            if (action.get() == ButtonType.OK) {
                if (DechetsService.getInstance().delete(dechets.getId())) {
                    MainWindowControllerBack.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_DECHETS);
                } else {
                    AlertUtils.makeError("Could not delete dechets");
                }
            }
        }
    }
    @FXML
    private void triNomD(ActionEvent event) {
        // Sort the list in descending order by Sponsor name
        listDechets.sort(Comparator.comparing(Dechets::getQuantite).reversed());
        displayData();
    }

    // Method to perform sorting in ascending order
    @FXML
    private void triNomA(ActionEvent event) {
        // Sort the list in ascending order by Sponsor name
        listDechets.sort(Comparator.comparing(Dechets::getQuantite));
        displayData();
    }

}
