package Controllers.UserController;

import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.UserService;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProfileController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView imagefield;

    @FXML
    private Text nameField;

    @FXML
    private Text lastnamefield;

    @FXML
    private Text roleField;

    @FXML
    private Text datenaissancefield;

    @FXML
    private Text numberfield;

    @FXML
    private Text emailfield;

    private String authenticatedEmail;

    public void setAuthenticatedEmail(String email) {
        this.authenticatedEmail = email;
        initializeFields();
    }

    @FXML
    void initializeFields() {
        assert imagefield != null : "fx:id=\"imagefield\" was not injected: check your FXML file 'profile.fxml'.";
        assert nameField != null : "fx:id=\"nameField\" was not injected: check your FXML file 'profile.fxml'.";
        assert lastnamefield != null : "fx:id=\"lastnamefield\" was not injected: check your FXML file 'profile.fxml'.";
        assert roleField != null : "fx:id=\"roleField\" was not injected: check your FXML file 'profile.fxml'.";
        assert datenaissancefield != null : "fx:id=\"datenaissancefield\" was not injected: check your FXML file 'profile.fxml'.";
        assert numberfield != null : "fx:id=\"numberfield\" was not injected: check your FXML file 'profile.fxml'.";
        assert emailfield != null : "fx:id=\"emailfield\" was not injected: check your FXML file 'profile.fxml'.";

        // Remplir les champs avec les informations de l'utilisateur authentifié
        User authenticatedUser = getAuthenticatedUser(authenticatedEmail);
        if (authenticatedUser != null) {
            nameField.setText(authenticatedUser.getName());
            lastnamefield.setText(authenticatedUser.getLastname());
            roleField.setText(authenticatedUser.getRoles());
            // datenaissancefield.setText(authenticatedUser.getDatenaissance());
            // numberfield.setText(authenticatedUser.getNumber());
            emailfield.setText(authenticatedUser.getEmail());
        } else {
            System.out.println("Erreur lors de la récupération de l'utilisateur authentifié.");
        }
    }

    // Méthode de récupération de l'utilisateur authentifié (à adapter selon votre logique d'authentification)
    private User getAuthenticatedUser(String email) {
        UserService userService = new UserService();
        return userService.getUserByEmail(email);
    }

    @FXML
    void logoutclicked(MouseEvent mouseEvent) {
        // Vérifier si l'utilisateur est connecté
        if (authenticatedEmail == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Déconnexion échouée", "Vous n'êtes pas connecté.");
            return; // Arrêter l'exécution de la méthode si l'utilisateur n'est pas connecté
        }

        // Afficher une boîte de dialogue de confirmation avant de se déconnecter
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Déconnexion");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir vous déconnecter ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // L'utilisateur a confirmé la déconnexion
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/login.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Login");
                stage.show();

                // Fermer la fenêtre actuelle
                Stage currentStage = (Stage) emailfield.getScene().getWindow();
                currentStage.close();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Déconnexion échouée", "Impossible d'ouvrir la fenêtre de connexion.");
            }
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
