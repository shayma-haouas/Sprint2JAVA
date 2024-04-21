package Controllers.UserController;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.UserService;

import java.io.File;

public class AjouterUser {

    @FXML
    private ImageView imageField;
    private UserService userService = new UserService(); // Référence au service utilisateur

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
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune image sélectionnée");
            alert.setHeaderText(null);
            alert.setContentText("Aucune image sélectionnée.");
            alert.showAndWait();
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
        } }

    @FXML
    void AjoutUser(ActionEvent actionEvent) {
       /* try {
            // Exemple:
            User user = new User(imageField.getAccessibleRoleDescription(), l, roles, email, password, image, number, is_verified, datenaissance);
            userService.add(user);

            // Afficher une alerte pour informer l'utilisateur que l'ajout a été effectué avec succès
            showAlert(Alert.AlertType.INFORMATION, "Ajout réussi", "Utilisateur ajouté avec succès", null);
        } catch (SQLException e) {
            // Afficher une alerte pour informer l'utilisateur s'il y a eu une erreur lors de l'ajout
            showAlert(Alert.AlertType.ERROR, "Erreur lors de l'ajout", "Une erreur s'est produite lors de l'ajout de l'utilisateur", e.getMessage());
        }*/
    }

    // Méthode pour afficher une alerte
    private void showAlert(Alert.AlertType alertType, String title, String header, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void UpdateUser(MouseEvent mouseEvent) {
    }

    public void DeleteUser(MouseEvent mouseEvent) {
    }
}
