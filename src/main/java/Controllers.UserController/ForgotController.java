package Controllers.UserController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class ForgotController {
    @FXML
    private ImageView retour;
    @FXML
    private TextField textField;

    public void retourClick(MouseEvent event) {


        try {
            Parent root = FXMLLoader.load(getClass().getResource("/UserInterface/Registration.fxml"));
            Scene scene = retour.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goForgot(ActionEvent actionEvent) {
    }


}

