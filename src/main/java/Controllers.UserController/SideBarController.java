package Controllers.UserController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class SideBarController {

    @FXML
    private Pane content_area;

    private String authenticatedEmail;


    public void setAuthenticatedEmail(String email) {
        this.authenticatedEmail = email;
    }

    @FXML
    private void openArticleList(MouseEvent event) throws IOException {
        // Chargement de la vue FXML de la page d'ajout d'article
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/ajouterUser.fxml"));
        Parent addArticleParent = loader.load();

        // Récupération du contrôleur de la vue d'ajout d'article
        AjouterUser addArticleController = loader.getController();

        // Remplacer le contenu actuel par la vue d'ajout d'article
        content_area.getChildren().clear();
        content_area.getChildren().add(addArticleParent);
    }


    @FXML
    public void openUserList(MouseEvent mouseEvent) throws IOException {
        // Chargement de la vue FXML de la liste des utilisateurs
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/listUser.fxml"));
        Parent userListParent = loader.load();

        content_area.getChildren().clear();
        content_area.getChildren().add(userListParent);
    }

    @FXML
    public void UpdateUser(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/ModiferUser.fxml"));
        Parent updateUserParent = loader.load();
        content_area.getChildren().clear();
        content_area.getChildren().add(updateUserParent);
    }

    @FXML
    void logoutclicked(MouseEvent mouseEvent) {
        // Vérifier si l'utilisateur est connecté
        if (authenticatedEmail == null || authenticatedEmail.isEmpty()) {
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
                Stage currentStage = (Stage) content_area.getScene().getWindow();
                currentStage.close();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Déconnexion échouée", "Impossible d'ouvrir la fenêtre de connexion.");
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String erreur, String déconnexionÉchouée, String s) {
        Alert alert = new Alert(alertType);
        alert.setTitle(erreur);
        alert.setHeaderText(déconnexionÉchouée);
        alert.setContentText(s);
        alert.showAndWait();
    }

    @FXML
    public void Profilclicked(MouseEvent mouseEvent) throws IOException {
        // Chargement de la vue FXML du profil
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/Profile.fxml"));
        Parent profilParent = loader.load();

        // Récupération du contrôleur du profil
        ProfileController profileController = loader.getController();

        // Appel d'une méthode pour effectuer des actions supplémentaires dans le contrôleur du profil si nécessaire
        profileController.initData(); // Par exemple, initialisation des données du profil

        // Remplacer le contenu actuel par la vue du profil
        content_area.getChildren().clear();
        content_area.getChildren().add(profilParent);
    }

}
