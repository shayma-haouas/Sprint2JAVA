package Controllers.UserController;

import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import services.UserService;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

public class AjouterUser {

    @FXML
    private ImageView imageField;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private TextField firstnameField;

    @FXML
    private TextField lastnameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField numberField;

    @FXML
    private PasswordField PasswordField;

    @FXML
    private PasswordField ResetField;

    @FXML
    private DatePicker datePicker;

    private UserService userService = new UserService();

    @FXML
    private void initialize() {
        // Ajoutez des éléments à la ChoiceBox
        choiceBox.getItems().addAll("ROLE_CLIENT", "ROLE_FOURNISSEUR");
    }

    @FXML
    private void onImageClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            imageField.setImage(image);
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune image sélectionnée", null, "Aucune image sélectionnée.");
        }
    }

    @FXML
    void UploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files", "*.png", "*.jpg", "*.jpeg", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            String imagePath = selectedFile.getAbsolutePath();
            imageField.setImage(new Image(selectedFile.toURI().toString()));
        }
    }

    @FXML
    void AjoutUser(ActionEvent actionEvent) {
        String role = choiceBox.getValue();
        if (role != null && !role.isEmpty()) {
            try {
                String name = firstnameField.getText().trim();
                String lastname = lastnameField.getText().trim();
                String email = emailField.getText().trim();
                String password = PasswordField.getText().trim();
                String resetPassword = ResetField.getText().trim();
                int number = Integer.parseInt(numberField.getText().trim());

                if (name.isEmpty() || lastname.isEmpty() || email.isEmpty() || password.isEmpty() || resetPassword.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Erreur lors de l'ajout", null, "Veuillez remplir tous les champs.");
                    return;
                }

                if (!password.equals(resetPassword)) {
                    showAlert(Alert.AlertType.ERROR, "Erreur lors de l'ajout", null, "Les champs de mot de passe ne correspondent pas.");
                    return;
                }

                Date dateNaissance = null;
                if (datePicker.getValue() != null) {
                    dateNaissance = Date.valueOf(datePicker.getValue());
                }



                User user = new User(name, lastname,  password, email,role, "", number, false, dateNaissance);
                userService.add(user);

                showAlert(Alert.AlertType.INFORMATION, "Ajout réussi", "Utilisateur ajouté avec succès", null);
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur lors de l'ajout", "Une erreur s'est produite lors de l'ajout de l'utilisateur", e.getMessage());
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur lors de l'ajout", "Veuillez entrer un numéro valide", null);
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucun rôle sélectionné", "Veuillez sélectionner un rôle pour l'utilisateur", null);
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void UpdateUser(javafx.scene.input.MouseEvent mouseEvent) {
    }

    public void DeleteUser(javafx.scene.input.MouseEvent mouseEvent) {
    }
}
