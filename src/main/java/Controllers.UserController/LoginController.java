package Controllers.UserController;
import com.mysql.cj.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import services.UserService;

import java.io.IOException;

public class LoginController {
    static  String emailc;

    @FXML
    private TextField emailField;

    @FXML
    private TextField Passwordfield;
    private static Session session;

    @FXML
    void loginclicked(ActionEvent event) {
        String email = emailField.getText();
        String password = Passwordfield.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Fields Empty", "Please fill in both email and password fields.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Email", "Please enter a valid email address.");
            return;
        }

        UserService userService = new UserService();
        String role = userService.getRole(email);
        boolean loginSuccessful = userService.login(email, password);

        if (loginSuccessful) {
            System.out.println("Login successful!");
            try {
                FXMLLoader loader;
                if ("ROLE_CLIENT".equals(role)) {
                    FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/UserInterface/home.fxml"));
                    Parent root = loader1.load(); // Load the FXML file

                    HomeController homeController = loader1.getController(); // Get the controller instance

                    homeController.setAuthenticatedEmail(email);
                    this.emailc = email;
                } else if ("ROLE_ADMIN".equals(role)) {
                    loader = new FXMLLoader(getClass().getResource("/UserInterface/sidebar.fxml"));
                    Parent root = loader.load();
                    SideBarController sidebarController = loader.getController();
                    this.emailc = email;

                    sidebarController.setAuthenticatedEmail(email);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Unknown Role", "Your role is not recognized.");
                    return;
                }
                // Autres traitements
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Login Failed", "Incorrect email or password.");
        }

}


    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void ClickedSign(MouseEvent mouseEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/UserInterface/Registration.fxml"));
            Scene scene = emailField.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void forgot(MouseEvent event) {
        try {
            // Récupérer le nœud source de l'événement
            Node source = (Node) event.getSource();

            // Récupérer la scène à partir du nœud source
            Scene scene = source.getScene();

            // Charger la page Forgot.fxml et changer la racine de la scène
            Parent root = FXMLLoader.load(getClass().getResource("/UserInterface/ResetPwd.fxml"));
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Session getSession() {
        return session;
    }

}
