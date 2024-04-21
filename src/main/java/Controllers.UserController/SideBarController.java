package Controllers.UserController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class SideBarController {
    @FXML
    private Pane content_area;

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
}



