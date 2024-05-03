package Controllers.UserController;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import services.UserService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

public class RegistrationController {



    @FXML
    private DatePicker Datefield;

    @FXML
    private TextField Emailfield;

    @FXML
    private TextField Firstnamefield;

    @FXML
    private TextField Lastnamefield;

    @FXML
    private TextField Numberfield;

    @FXML
    private PasswordField Passwordfield;
    @FXML
    private ImageView imageView;
    @FXML
    private WebView captchaWebView;

    private String imagePath;
    @FXML
    private TextField captchaField;
    private String captchaText;
    @FXML
    private Button cam;
    String pic ;


    public void initialize(URL url, ResourceBundle rb) {

        // Chargez le captcha dans le WebView
        WebEngine engine = captchaWebView.getEngine();
        engine.load("https://www.google.com/recaptcha/api2/anchor?ar=1&k=6LeKGs0pAAAAAJkTYIf_U-YiIlCCu8Sqlief-0a3");
    }



    @FXML
    void Registratinclicked(ActionEvent event) {
        String name = Firstnamefield.getText();
        String lastname = Lastnamefield.getText();
        String email = Emailfield.getText();
        String numberText = Numberfield.getText();
        String password = Passwordfield.getText();
        String image = imagePath;
        DatePicker datenaissance = Datefield;
        String roles = "ROLE_CLIENT";

        String enteredCaptcha = captchaField.getText();
        boolean isValidCaptcha = validateCaptcha(enteredCaptcha);



        if (email.isEmpty() || !email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            showAlert(Alert.AlertType.ERROR, "Alert", "Erreur Saisie", "Veuillez saisir une adresse email valide.", StageStyle.DECORATED);
            return;
        }

        if (name.isEmpty() || !name.matches("[a-zA-Z]+")) {
            showAlert(Alert.AlertType.ERROR, "Alert", "Erreur Saisie", "Veuillez saisir un prénom valide.", StageStyle.DECORATED);
            return;
        }

        if (lastname.isEmpty() || !lastname.matches("[a-zA-Z]+")) {
            showAlert(Alert.AlertType.ERROR, "Alert", "Erreur Saisie", "Veuillez saisir un nom de famille valide.", StageStyle.DECORATED);
            return;
        }

        if (password.isEmpty() || password.length() < 6) {
            showAlert(Alert.AlertType.ERROR, "Alert", "Erreur Saisie", "Veuillez saisir un mot de passe d'au moins 6 caractères.", StageStyle.DECORATED);
            return;
        }

        int number;
        if (!numberText.matches("[2479]\\d{7}")) {
            showAlert(Alert.AlertType.ERROR, "Alert", "Erreur Saisie", "Le numéro de téléphone doit contenir 8 chiffres et commencer par 2, 4, 7 ou 9.", StageStyle.DECORATED);
            return;
        }


        try {
            number = Integer.parseInt(numberText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Alert", "Erreur Saisie", "Veuillez saisir un numéro de téléphone valide.", StageStyle.DECORATED);
            return;
        }

        UserService userService = new UserService();
        if (userService.isEmailUsed(email)) {
            showAlert(Alert.AlertType.ERROR, "Alert", "Erreur Saisie", "Cette adresse email est déjà utilisée.", StageStyle.DECORATED);
            return;
        }

        if (imagePath == null) {
            showAlert(Alert.AlertType.ERROR, "Alert", "Erreur Saisie", "Veuillez capturer ou sélectionner une image.", StageStyle.DECORATED);
            return;
        }

        String recaptchaToken = "6LeKGs0pAAAAAJkTYIf_U-YiIlCCu8Sqlief-0a3"; // Remplacez par le vrai token reCAPTCHA
        boolean isValidRecaptcha = validateRecaptchaToken(recaptchaToken);

        if (isValidRecaptcha) {
            System.out.println("Token reCAPTCHA valide. Enregistrement de l'utilisateur...");
            addUserToDatabase(name, lastname, roles, email, password, image, number, false, java.sql.Date.valueOf(datenaissance.getValue()));
        } else {
            showAlert(Alert.AlertType.ERROR, "Alert", "Erreur Saisie", "Captcha incorrect.", StageStyle.DECORATED);
        }


        clearInputFields();
    }

    private boolean validateCaptcha(String enteredCaptcha) {

        return enteredCaptcha.equals(captchaText);
    }

    private boolean validateRecaptchaToken(String recaptchaToken) {

        String secretKey = "6LeKGs0pAAAAAJkTYIf_U-YiIlCCu8Sqlief-0a3\n"; // Remplacez par votre clé secrète reCAPTCHA
        String response = sendRecaptchaValidationRequest(recaptchaToken, secretKey);
        return parseRecaptchaValidationResponse(response);
    }

    private String sendRecaptchaValidationRequest(String recaptchaToken, String secretKey) {

        return "{\"success\": true}"; // Réponse JSON simulée
    }

    private boolean parseRecaptchaValidationResponse(String response) {

        return response.contains("\"success\": true");
    }
    @FXML
    private void camera(ActionEvent event) {
        Webcam webcam = Webcam.getDefault();
        if (webcam != null) {
            webcam.open();

            // Ouvrir une fenêtre pour afficher l'aperçu de la caméra
            JFrame window = new JFrame("Camera Preview");
            window.setLayout(new BorderLayout());
            window.setSize(640, 480);

            WebcamPanel panel = new WebcamPanel(webcam);
            panel.setMirrored(true);
            window.add(panel, BorderLayout.CENTER);

            JButton captureButton = new JButton("Capture");
            captureButton.addActionListener(e -> {
                // Prendre une capture d'écran et la sauvegarder
                try {
                    Random rnd = new Random();
                    int number = rnd.nextInt(999999999);
                    String filename = number + "_capture.jpg";
                    String filePath = "C:\\Users\\siwar\\OneDrive\\Bureau\\JAVASPRINT\\Sprint2JAVA\\src\\main\\resources\\img\\uploads\\" + filename;
                    File file = new File(filePath);
                    ImageIO.write(webcam.getImage(), "JPG", file);
                    System.out.println("Capture saved: " + filename);

                    // Définir l'image capturée dans l'ImageView
                    Image image = new Image(file.toURI().toString());
                    imageView.setImage(image);

                    // Assigner le chemin de l'image capturée à l'attribut imagePath
                    imagePath = filePath;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            window.add(captureButton, BorderLayout.SOUTH);

            window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            window.setVisible(true);
        } else {
            System.out.println("Aucune webcam détectée.");
        }
    }



    @FXML
    void uploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files", "*.png", "*.jpg", "*.jpeg", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            String imagePath = selectedFile.toURI().toString();
            Image image = new Image(imagePath);
            imageView.setImage(image);
        }
    }



    private void showAlert(Alert.AlertType alertType, String title, String header, String message, StageStyle stageStyle) {
        Alert alert = new Alert(alertType);
        alert.initStyle(stageStyle);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo.png")));

        ButtonType buttonType = new ButtonType("Retour", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(buttonType);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonType) {
        }
    }
    private void addUserToDatabase(String name, String lastname, String roles , String email, String password, String image, int number, Boolean is_verified, Date datenaissance) {

        User user = new User(name, lastname, roles, email,password, image, number, is_verified, datenaissance);
        UserService userService = new UserService();
        userService.signUp(user);
    }


    private void clearInputFields() {
        Firstnamefield.clear();
        Lastnamefield.clear();
        Emailfield.clear();
        Numberfield.clear();
        Passwordfield.clear();
        imageView.setImage(null);
        imagePath = null;
        Datefield.setValue(null);


    }


    @FXML
    public void RetourLogin(MouseEvent mouseEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/UserInterface/login.fxml"));
            Scene scene = Emailfield.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
