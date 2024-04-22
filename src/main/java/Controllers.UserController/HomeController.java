package Controllers.UserController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.UserService;

import java.io.IOException;

public class HomeController {

    @FXML
    private Text emailfield;

    private String authenticatedEmail; // Store authenticated email

    public void setAuthenticatedEmail(String email) {
        this.authenticatedEmail = email;
    }

    @FXML
    public void Profileclicked(MouseEvent mouseEvent) {
        // L'utilisateur est connecté, afficher la page de profil
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/profile.fxml"));
            Parent root = loader.load();
            ProfileController profileController = loader.getController();
            profileController.setAuthenticatedEmail(authenticatedEmail); // Passing authenticated email to ProfileController
            Scene scene = emailfield.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Chargement du profil échoué", "Impossible de charger la page du profil.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) emailfield.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Déconnexion échouée", "Impossible d'ouvrir la fenêtre de connexion.");
        }
    }
}
