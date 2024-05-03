package Controllers.UserController;

import com.sun.javafx.charts.Legend;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import entities.User;
import services.UserService;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class listUserController {
    @FXML
    private ListView<User> userListListView; // ListView pour afficher les détails d'utilisateurs
    @FXML
    private TableView<User> userTableView;
    @FXML
    private Button btnAddUser;
    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> statusInput; // ComboBox pour sélectionner le critère de tri

    private UserService userService;
    private List<User> users;

    public listUserController() {
        userService = new UserService();
    }

    @FXML
    private void initialize() {
        users = userService.show();
        userListListView.getItems().addAll(users);
        statusInput.getItems().addAll("Name", "Email", "Role");

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

                            setText("Nom: " + user.getName() + " Prénom: " + user.getLastname() +
                                "\nTéléphone:" + user.getNumber() + " Email: " + user.getEmail() +
                                "\nRole: " + role + " Date de naissance: " + birthDate);

                            // Créer un ImageView pour afficher l'image
                            ImageView imageView = new ImageView();
                            // Charger l'image depuis le chemin stocké dans l'objet User
                            Image image = new Image(new File(user.getImage()).toURI().toString());
                            // Définir la taille de l'image
                            imageView.setFitWidth(50);
                            imageView.setFitHeight(50);
                            // Définir l'image dans l'ImageView
                            imageView.setImage(image);

                            // Créer des boutons pour éditer et supprimer l'utilisateur
                            Button editButton = new Button("Editer");
                            Button deleteButton = new Button("Supprimer");

                            editButton.setOnAction(event -> {
                                openUpdateUserWindow(user);
                            });

                            deleteButton.setOnAction(event -> {
                                userService.delete(user);
                                userListListView.getItems().remove(user);
                                refreshUserList();
                            });

                            // Créer un HBox pour afficher les boutons et l'image
                            HBox hbox = new HBox(imageView, editButton, deleteButton);
                            // Définir le graphique de la cellule comme le HBox
                            setGraphic(hbox);
                        }
                    }
                };
            }
        });

        userListListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
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
            User selectedUser = userListListView.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                userService.show();
            }
        });


        statusInput.setOnAction(event -> {
            String selectedSort = statusInput.getValue();
            if ("Nom".equals(selectedSort)) {
                Collections.sort(users, Comparator.comparing(User::getName));
            } else if ("Email".equals(selectedSort)) {
                Collections.sort(users, Comparator.comparing(User::getEmail));
            } else if ("Role".equals(selectedSort)) {
                Collections.sort(users, Comparator.comparing(User::getRoles));
            }
            userListListView.getItems().setAll(users);
        });
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {

            List<User> filteredUsers = users.stream()
                .filter(user ->
                    user.getName().toLowerCase().contains(newValue.toLowerCase()) ||
                        user.getLastname().toLowerCase().contains(newValue.toLowerCase()) ||
                        user.getEmail().toLowerCase().contains(newValue.toLowerCase()) ||
                        user.getRoles().toLowerCase().contains(newValue.toLowerCase())
                )
                .toList();
            userListListView.getItems().setAll(filteredUsers);
        });


    }

    private void openUpdateUserWindow(User user) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/ModiferUser.fxml"));
        try {
            Parent root = loader.load();
            ModifierUser updateUserController = loader.getController();
            updateUserController.setUser(user);
            updateUserController.setParentController(this);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            refreshUserList();
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
            refreshUserList();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void refreshUserList() {
        userListListView.getItems().clear();
        users = userService.show();
        userListListView.getItems().addAll(users);
    }

}
