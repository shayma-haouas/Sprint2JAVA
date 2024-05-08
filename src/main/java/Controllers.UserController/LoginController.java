package Controllers.UserController;

import com.mysql.cj.Session;
import entities.User;
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
import java.sql.SQLException;

public class LoginController {
    static String emailc;

    @FXML
    private TextField emailField;

    @FXML
    private TextField Passwordfield;
    private static Session session;
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    private int loginAttempts = 0;

    @FXML
    void loginclicked(ActionEvent event) throws SQLException {
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
        boolean loginSuccessful = userService.login(email, password);

        if (loginSuccessful) {
            System.out.println("Login successful!");
            String role = userService.getRole(email);
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

                } else if ("ROLE_FOURNISSEUR".equals(role)) {
                    System.out.println(role);
                    FXMLLoader loader2= new FXMLLoader(getClass().getResource("/UserInterface/Home2.fxml"));
                    Parent root = loader2.load(); // Load the FXML file

                    HomeController homeController = loader2.getController(); // Get the controller instance

                    homeController.setAuthenticatedEmail(email);
                    this.emailc = email;
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Unknown Role", "Your role is not recognized.");
                    return;
                }
                // Other processing
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            loginAttempts++;

            if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
                // Block the user after three unsuccessful login attempts
                User user = userService.getUserByEmail(email);
                userService.banUser(user);
                showAlert(Alert.AlertType.ERROR, "Error", "Account Blocked", "Your account has been blocked due to too many failed login attempts.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Login Failed", "Incorrect email or password.");
            }
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
            Node source = (Node) event.getSource();
            Scene scene = source.getScene();

            Parent root = FXMLLoader.load(getClass().getResource("/UserInterface/ResetPwd.fxml"));
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Session getSession() {
        return session;
    }

    private boolean passwordVisible = false;

    public void toggleShowPassword(ActionEvent actionEvent) {
        passwordVisible = !passwordVisible;

        if (passwordVisible) {
            Passwordfield.setPromptText(Passwordfield.getText());
            Passwordfield.setText("");
        } else {
            StringBuilder maskedPassword = new StringBuilder();
            for (int i = 0; i < Passwordfield.getPromptText().length(); i++) {
                maskedPassword.append("*");
            }
            Passwordfield.setText(Passwordfield.getPromptText());
            Passwordfield.setPromptText(null);
        }
    }
}
