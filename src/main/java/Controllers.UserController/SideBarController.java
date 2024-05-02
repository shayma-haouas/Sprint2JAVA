package Controllers.UserController;

import entities.User;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import services.UserService;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class SideBarController {

    @FXML
    private Pane content_area;
    private User userData; // Stocke les données de l'utilisateur

    private String authenticatedEmail;
    @FXML
    private Label nameLabel;
    public void setAuthenticatedEmail(String email) {
        this.authenticatedEmail = email;
        // Utilisez cet e-mail pour récupérer les données de l'utilisateur et initialiser la vue
        initData(); // Par exemple, pour le chargement des données de l'utilisateur
    }

    public void initData() {
        // Utilisez l'e-mail authentifié pour récupérer les données de l'utilisateur
        UserService userService = new UserService();
        userData = userService.getUserByEmail(authenticatedEmail);
        // Maintenant, utilisez les données de l'utilisateur pour initialiser votre vue
        // Par exemple:
        nameLabel.setText(userData.getName());
        // Autres initialisations de vue avec les données de l'utilisateur
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


        // Afficher une boîte de dialogue de confirmation avant de se déconnecter
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Déconnexion");
        alert.setHeaderText(null);
        alert.setContentText("Êtes-vous sûr de vouloir vous déconnecter ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // L'utilisateur a confirmé la déconnexion
            System.out.println("Logout Successful");
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
        profileController.initializeFields(); // Par exemple, initialisation des données du profil

        // Remplacer le contenu actuel par la vue du profil
        content_area.getChildren().clear();
        content_area.getChildren().add(profilParent);
    }

    @FXML
    public void StaticCliked(MouseEvent event) throws IOException {
        // Chargement de la vue FXML des statistiques
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/dash.fxml"));
        Parent statisticsParent = loader.load();

        // Remplacement du contenu de content_area avec la vue des statistiques
        content_area.getChildren().clear();
        content_area.getChildren().add(statisticsParent);
    }

    public void capturer(MouseEvent event) {
        // Récupération de la racine de la scène
        Node root = content_area.getScene().getRoot();

        // Création d'une image pour stocker la capture d'écran
        WritableImage image = new WritableImage((int) root.getBoundsInLocal().getWidth(), (int) root.getBoundsInLocal().getHeight());

        // Capture d'écran de la racine de la scène
        root.snapshot(null, image);

        // Définir le chemin du dossier de destination
        String dossierDestination = "C:\\Users\\siwar\\OneDrive\\Bureau\\JAVASPRINT\\Sprint2JAVA\\src\\main\\resources\\img\\capture\\";

        // Générer un nom de fichier unique
        String nomFichier = "capture_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".png";

        // Enregistrer la capture dans le dossier de destination
        File fichier = new File(dossierDestination + nomFichier);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", fichier);
            System.out.println("Capture enregistrée : " + fichier.getAbsolutePath());

            // Afficher une boîte de dialogue pour informer l'utilisateur que la capture a été enregistrée
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Capture d'écran");
            alert.setHeaderText(null);
            alert.setContentText("La capture d'écran a été enregistrée avec succès à l'emplacement : " + fichier.getAbsolutePath());
            alert.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
            // Afficher une boîte de dialogue d'erreur si une exception se produit lors de l'enregistrement de la capture
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Une erreur s'est produite lors de l'enregistrement de la capture d'écran.");
            alert.showAndWait();
        }
    }
    }




