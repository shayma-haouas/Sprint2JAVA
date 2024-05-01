package edu.esprit.flo.controllers.dechets_back;

import edu.esprit.flo.MainApp;
import edu.esprit.flo.controllers.MainWindowControllerBack;
import edu.esprit.flo.entities.Dechets;
import edu.esprit.flo.services.DechetsService;
import edu.esprit.flo.utils.AlertUtils;
import edu.esprit.flo.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.ResourceBundle;

public class ManageController implements Initializable {

    @FXML
    public TextField typeTF;
    @FXML
    public DatePicker dateEntreDP;
    @FXML
    public TextField descriptionTF;
    @FXML
    public TextField quantiteTF;
    @FXML
    public ImageView imageIV;

    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    Dechets currentDechets;
    Path selectedImagePath;
    boolean imageEdited;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        currentDechets = ShowAllController.currentDechets;

        if (currentDechets != null) {
            topText.setText("Modifier dechets");
            btnAjout.setText("Modifier");

            try {
                typeTF.setText(currentDechets.getType());
                dateEntreDP.setValue(currentDechets.getDateEntre());
                descriptionTF.setText(currentDechets.getDescription());
                quantiteTF.setText(String.valueOf(currentDechets.getQuantite()));
                selectedImagePath = FileSystems.getDefault().getPath(currentDechets.getImage());
                if (selectedImagePath.toFile().exists()) {
                    imageIV.setImage(new Image(selectedImagePath.toUri().toString()));
                }

            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter dechets");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent ignored) {

        if (controleDeSaisie()) {
            createImageFile();
            String imagePath = selectedImagePath.toString();

            Dechets dechets = new Dechets();
            dechets.setType(typeTF.getText());
            dechets.setDateEntre(dateEntreDP.getValue());
            dechets.setDescription(descriptionTF.getText());
            dechets.setQuantite(Integer.parseInt(quantiteTF.getText()));
            dechets.setImage(imagePath);

            if (currentDechets == null) {
                if (DechetsService.getInstance().add(dechets)) {
                    AlertUtils.makeSuccessNotification("Dechets ajouté avec succés");
                    MainWindowControllerBack.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_DECHETS);
                } else {
                    AlertUtils.makeError("Error");
                }
            } else {
                dechets.setId(currentDechets.getId());
                if (DechetsService.getInstance().edit(dechets)) {
                    AlertUtils.makeSuccessNotification("Dechets modifié avec succés");
                    ShowAllController.currentDechets = null;
                    MainWindowControllerBack.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_DECHETS);
                } else {
                    AlertUtils.makeError("Error");
                }
            }

            if (selectedImagePath != null) {
                createImageFile();
            }
        }
    }

    @FXML
    public void chooseImage(ActionEvent ignored) {

        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(MainApp.mainStage);
        if (file != null) {
            selectedImagePath = Paths.get(file.getPath());
            imageIV.setImage(new Image(file.toURI().toString()));
        }
    }

    public void createImageFile() {
        try {
            Path newPath = FileSystems.getDefault().getPath("src/main/resources/com/example/flo/images/uploads/" + selectedImagePath.getFileName());
            Files.copy(selectedImagePath, newPath, StandardCopyOption.REPLACE_EXISTING);
            selectedImagePath = newPath;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean controleDeSaisie() {


        if (typeTF.getText().isEmpty()) {
            AlertUtils.makeInformation("type ne doit pas etre vide");
            return false;
        }


        if (dateEntreDP.getValue() == null) {
            AlertUtils.makeInformation("Choisir une date pour dateEntre");
            return false;
        }


        if (descriptionTF.getText().isEmpty()) {
            AlertUtils.makeInformation("description ne doit pas etre vide");
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

        if (selectedImagePath == null) {
            AlertUtils.makeInformation("Veuillez choisir une image");
            return false;
        }

        return true;
    }
}