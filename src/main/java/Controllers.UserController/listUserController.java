package Controllers.UserController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import entities.User;
import services.UserService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class listUserController {
    @FXML
    private ListView<User> userListListView; // ListView pour afficher les détails d'utilisateurs

    @FXML
    private Button btnAddUser;

    @FXML
    private ComboBox<String> statusInput; // ComboBox pour sélectionner le critère de tri

    private UserService userService;
    private List<User> users;

    public listUserController() {
        userService = new UserService();
    }

    @FXML
    private void initialize() {
        // Récupérez la liste des utilisateurs à partir de votre service
        users = userService.show();

        // Ajoutez les utilisateurs à la ListView
        userListListView.getItems().addAll(users);

        // Ajouter des éléments à la ComboBox de tri
        statusInput.getItems().addAll("Name", "Email","Role");

        // Définir la manière dont les utilisateurs sont affichés dans la ListView
        userListListView.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
            @Override
            public ListCell<User> call(ListView<User> listView) {
                return new ListCell<User>() {
                    @Override
                    protected void updateItem(User user, boolean empty) {
                        super.updateItem(user, empty);
                        if (empty || user == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            String role = "";
                            if (user.getRoles().contains("ROLE_CLIENT")) {
                                role = "Client";
                            } else if (user.getRoles().contains("ROLE_ADMIN")) {
                                role = "Admin";
                            } else if (user.getRoles().contains("ROLE_FOURNISSEUR")) {
                                role = "Fournisseur";
                            } else {
                                role = "Inconnu";
                            }

                            String birthDate = user.getDatenaissance() != null ? new SimpleDateFormat("dd/MM/yyyy").format(user.getDatenaissance()) : "Inconnue";

                            setText("Nom: " + user.getName() + "                                                     Prénom: " + user.getLastname() +
                                "\nTéléphone:"  + user.getNumber()+ "                                       Email: " + user.getEmail()  +
                                "\nRole: " + role + "                                                       Date de naissance: " + birthDate);

                            Button editButton = new Button("Editer");
                            Button deleteButton = new Button("Supprimer");

                            editButton.setOnAction(event -> {
                                openUpdateUserWindow(user);

                            });

                            deleteButton.setOnAction(event -> {
                                userService.delete(user);
                                userListListView.getItems().remove(user);
                            });

                            HBox buttonsBox = new HBox(editButton, deleteButton);
                            setGraphic(buttonsBox);
                        }
                    }
                };
            }
        });

        // Si vous souhaitez gérer les sélections d'utilisateurs
        userListListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Gérer la sélection de l'utilisateur ici
            if (newValue != null) {
                System.out.println("Utilisateur sélectionné : " + newValue.getName());
                System.out.println("Prénom : " + newValue.getLastname());
                System.out.println("Email : " + newValue.getEmail());
                System.out.println("Téléphone : " + newValue.getNumber());
                System.out.println("Role : " + newValue.getRoles());
                System.out.println("Date de naissance : " + newValue.getDatenaissance());
            }
        });
        userListListView.setOnMouseClicked(event -> {
            // Obtenez l'utilisateur sélectionné
            User selectedUser = userListListView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                // Affichez l'interface "ShowUser.fxml" avec les détails de l'utilisateur sélectionné
                userService.show();
            }
        });

        // Écouter les changements de sélection dans la ComboBox de tri
        // Écouter les changements de sélection dans la ComboBox de tri
        statusInput.setOnAction(event -> {
            String selectedSort = statusInput.getValue();
            if ("Nom".equals(selectedSort)) {
                // Trier la liste des utilisateurs par nom
                Collections.sort(users, Comparator.comparing(User::getName));
            } else if ("Email".equals(selectedSort)) {
                // Trier la liste des utilisateurs par email
                Collections.sort(users, Comparator.comparing(User::getEmail));
            } else if ("Role".equals(selectedSort)) {
                // Trier la liste des utilisateurs par rôle
                Collections.sort(users, Comparator.comparing(User::getRoles));
            }
            // Mettre à jour la ListView avec la liste triée
            userListListView.getItems().setAll(users);
        });

    }

    private void openUpdateUserWindow(User user) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/ModiferUser.fxml"));
        try {
            Parent root = loader.load();
            ModifierUser updateUserController = loader.getController();
            updateUserController.setUser(user);
            updateUserController.setParentController(this); // Passer une référence à cette instance de listUserController à la fenêtre de mise à jour
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void navigateToAddUser(MouseEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/AddUser.fxml"));
        Parent root;
        try {
            root = loader.load();
            Stage stage = (Stage) btnAddUser.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void statusChange(javafx.event.ActionEvent actionEvent) {
    }

    public void AddUserClicked(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/AjouterUser.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
