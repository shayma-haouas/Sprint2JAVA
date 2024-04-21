package Controllers.UserController;

import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import services.UserService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class listUserController implements Initializable {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    @FXML
    private ListView<String> listViewNom;
    @FXML
    private Button refresh;
    @FXML
    private Button modifier;

    @FXML
    private ListView<String> list;
    @FXML
    private Button delete;
    private UserService userService = new UserService(); // Utilisez votre service utilisateur

    @FXML
    void delete(ActionEvent event) throws SQLException {
        User user = getSelectedUser();
        if (user != null) {
            userService.delete(user); // Utilisez la méthode de suppression de votre service utilisateur
            RefreshA(); // Rafraîchissez la liste après la suppression
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Information manquante");
            alert.setHeaderText(null);
            alert.setContentText("Vous devez sélectionner un utilisateur.");
            Optional<ButtonType> option = alert.showAndWait();
        }
    }

    @FXML
    void refreshList(ActionEvent event) {
        RefreshA();
    }

    public void RefreshA() {
        List<User> users = userService.show(); // Obtenez tous les utilisateurs depuis votre service
        list.getItems().clear();
        StringBuilder affichage = new StringBuilder();
        affichage.append("Nom                  ");
        affichage.append("Prenom           ");
        affichage.append("Email                                     ");
        affichage.append("Role                        ");
        affichage.append("Date Naissance           ");
        // Ajoutez d'autres champs si nécessaire

        list.getItems().add(affichage.toString());
        for (User user : users) {
            StringBuilder userDetails = new StringBuilder();
            userDetails.append(user.getName()).append("       ");
            userDetails.append(user.getLastname()).append("       ");
            userDetails.append(user.getEmail()).append("       ");
            userDetails.append(user.getRoles()).append("        ");
            userDetails.append(user.getDatenaissance() != null ? dateFormat.format(user.getDatenaissance()) : "").append("       ");

            // Ajoutez d'autres champs si nécessaire
            list.getItems().add(userDetails.toString());
        }
    }

    @FXML
    void edit(ActionEvent event) throws SQLException {
        User user = getSelectedUser();
        if (user != null) {
            openEditWindow(user);
        } else {
            System.out.println("Aucun utilisateur sélectionné.");
        }
    }

    private User getSelectedUser() throws SQLException {
        int selectedIndex = listViewNom.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            // Récupérer l'utilisateur correspondant à l'index sélectionné
            List<User> users = userService.show(); // Obtenez tous les utilisateurs depuis votre service
            return users.get(selectedIndex);
        }
        return null;
    }

    private void openEditWindow(User user) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/User/EditUser.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Get the controller after loading the FXML file
        EditUserController editUserController = loader.getController();

        // Initialize the data in the editUserController
        editUserController.initData(user);

        // Set the scene
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void statusChange(ActionEvent actionEvent) {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        RefreshA();
    }
}
