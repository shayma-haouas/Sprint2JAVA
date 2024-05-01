package edu.esprit.flo.controllers.don_back;

import edu.esprit.flo.controllers.MainWindowControllerBack;
import edu.esprit.flo.entities.Don;
import edu.esprit.flo.entities.User;
import edu.esprit.flo.services.DonService;
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

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ShowAllController implements Initializable {

    public static Don currentDon;

    @FXML
    public Text topText;
    @FXML
    public VBox mainVBox;
    @FXML
    private TextField search;
    @FXML
    private Button trie1;

    @FXML
    private Button trie2;


    List<Don> listDon;

    public void initialize(URL url, ResourceBundle rb) {
        listDon = DonService.getInstance().getAll();
        displayData();

        search.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.isEmpty()) {
                    List<Don> searchResults = DonService.getInstance().search(newValue);
                    if (!searchResults.isEmpty()) {
                        listDon.clear();
                        listDon.addAll(searchResults);
                        displayData();
                    } else {
                        AlertUtils.makeInformation("No matching results found.");
                    }
                } else {
                    // If the search field is empty, display all data
                    listDon.clear();
                    listDon.addAll(DonService.getInstance().getAll());
                    displayData();
                }
            }
        });
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

    public Parent makeDonModel(Don don) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_BACK_MODEL_DON)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#typeText")).setText("Type : " + don.getType());
            ((Text) innerContainer.lookup("#descriptionText")).setText("Description : " + don.getDescription());
            ((Text) innerContainer.lookup("#dateDonText")).setText("DateDon : " + don.getDateDon());

            // Check if User object is not null before accessing toString()
            User user = don.getUser();
            if (user != null) {
                ((Text) innerContainer.lookup("#userText")).setText("User : " + user.toString());
            } else {
                ((Text) innerContainer.lookup("#userText")).setText("User : [Unknown]");
            }

            ((Button) innerContainer.lookup("#editButton")).setOnAction((event) -> modifierDon(don));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((event) -> supprimerDon(don));

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    private void modifierDon(Don don) {
        currentDon = don;
        MainWindowControllerBack.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_DON);
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
                    MainWindowControllerBack.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_DON);
                } else {
                    AlertUtils.makeError("Could not delete don");
                }
            }
        }
    }
    @FXML
    private void triNomD(ActionEvent event) {
        // Sort the list in descending order by donation date
        listDon.sort(Comparator.comparing(Don::getDateDon).reversed());
        displayData();
    }
    @FXML
    private void triNomA(ActionEvent event) {
        // Sort the list in ascending order by donation date
        listDon.sort(Comparator.comparing(Don::getDateDon));
        displayData();
    }




}
