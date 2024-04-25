package Controllers.UserController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.UserService;

import java.io.IOException;

public class HomeController {


    @FXML
    private Button EmailField;

    // Méthode pour définir l'e-mail authentifié
    public void setAuthenticatedEmail(String email) {
        if (LoginController.emailc != null) {
            EmailField.setText(email);
        }
    }

    public static  String authenticatedEmail; // Store authenticated email



    @FXML
    public void Profileclicked(MouseEvent mouseEvent) {
        // L'utilisateur est connecté, afficher la page de profil
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/profile.fxml"));
            Parent root = loader.load();
            ProfileController profileController = loader.getController();
            Scene scene = EmailField.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Chargement du profil échoué", "Impossible de charger la page du profil.");
        }
    }

    @FXML
    public void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) EmailField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Déconnexion échouée", "Impossible d'ouvrir la fenêtre de connexion.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
