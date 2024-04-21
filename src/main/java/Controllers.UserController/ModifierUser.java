package Controllers.UserController;

import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.UserService;

import java.io.File;
import java.util.Optional;

public class ModifierUser {

    @FXML
    private ImageView imageField;
    private String email;

    public void UploadImage(ActionEvent actionEvent) {
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

    private UserService userService = new UserService(); // Initialisation du service utilisateur



    public void setUserData(String userEmail) {
        this.email = userEmail; // Méthode pour définir l'email de l'utilisateur à modifier
    }

    public void ModifierUser(ActionEvent actionEvent) {
        // Afficher une boîte de dialogue de confirmation avant de modifier l'utilisateur
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de modification");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir modifier cet utilisateur ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // L'utilisateur a confirmé la modification, procéder à la modification
            // Rechercher l'utilisateur par son email
            User userToUpdate = userService.getUserByEmail(email);
            if (userToUpdate != null) {
                // Mettre à jour les informations de l'utilisateur en fonction des nouvelles valeurs
                // Utilisez les autres champs FXML pour récupérer les nouvelles informations de l'utilisateur
                // Par exemple, vous devez avoir des champs FXML pour le nom, le prénom, etc.
                // Mettez à jour ces champs de l'utilisateur à modifier

                // Appeler la méthode update de votre service UserService pour mettre à jour l'utilisateur
                userService.update(userToUpdate);
                showAlert(Alert.AlertType.INFORMATION, "Modification réussie", "Les informations de l'utilisateur ont été mises à jour avec succès");
            } else {
                showAlert(Alert.AlertType.ERROR, "Utilisateur non trouvé", "L'utilisateur avec cet email n'existe pas");
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.showAndWait();
    }
}
