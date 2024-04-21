package Controllers.UserController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import services.UserService;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField Emailfield;

    @FXML
    private TextField Passwordfield;

    @FXML
    void loginclicked(ActionEvent event) {

        String email = Emailfield.getText();
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
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/profile.fxml"));
                Parent root = loader.load();
                ProfileController profileController = loader.getController();
                profileController.setAuthenticatedEmail(email);
                Stage stage = (Stage) Emailfield.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Login Failed", "Invalid email or password. Please try again.");
        }
    }

    private boolean isValidEmail(String email) {
        // Utiliser une expression régulière pour vérifier le format de l'email
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
            Scene scene = Emailfield.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
