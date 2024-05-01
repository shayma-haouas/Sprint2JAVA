package Controllers.UserController;

import entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Optional;
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


    public void initialize(URL url, ResourceBundle rb) {

        // Chargez le captcha dans le WebView
        WebEngine engine = captchaWebView.getEngine();
        engine.load("https://www.google.com/recaptcha/api2/anchor?ar=1&k=6LeKGs0pAAAAAJkTYIf_U-YiIlCCu8Sqlief-0a3\n");
    }
    @FXML
    void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files", "*.png", "*.jpg", "*.jpeg", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            imagePath = selectedFile.getAbsolutePath(); // Stockez le chemin de l'image sélectionnée dans la variable de classe
            // Chargez l'image dans l'ImageView si nécessaire
            imageView.setImage(new Image(selectedFile.toURI().toString()));
        } }


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


        if (email.isEmpty() || !email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            showAlert(Alert.AlertType.ERROR, "Alert", "Erreur Saisie", "Veuillez saisir une adresse email valide.", StageStyle.DECORATED);

            return;
        }

        if (name.isEmpty() || !name.matches("[a-zA-Z]+")) {
            showAlert(Alert.AlertType.ERROR, "Alert", "Erreur Saisie" , "Veuillez saisir un nombre minimum du lettre.", StageStyle.DECORATED);

            return;
        }

        if (lastname.isEmpty() || !lastname.matches("[a-zA-Z]+")) {
            showAlert(Alert.AlertType.ERROR, "Alert", "Erreur Saisie", "Veuillez saisir un nombre minimum du lettre .", StageStyle.DECORATED);

            return;
        }

        if (password.isEmpty() || password.length() < 6) {
            showAlert(Alert.AlertType.ERROR, "Alert", "Erreur Saisie", "Veuillez saisir une Mot de passe minimum 6 caractères.", StageStyle.DECORATED);

            return;
        }
        int number;
        try {
            number = Integer.parseInt(numberText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Alert", "Erreur Saisie", "Veuillez saisir une Numero De Telephone valide.", StageStyle.DECORATED);

            return;
        }
        UserService userService = new UserService();
        if (userService.isEmailUsed(email)) {
            showAlert(Alert.AlertType.ERROR, "Alert", "Erreur Saisie", "Veuillez saisir une adresse email valide.", StageStyle.DECORATED);

            return;
        }
        String recaptchaToken = "6LeKGs0pAAAAAJkTYIf_U-YiIlCCu8Sqlief-0a3\n"; // Remplacez par la méthode pour récupérer le token
        boolean isValidRecaptcha = validateRecaptchaToken(recaptchaToken);

        if (isValidRecaptcha) {
            // Le token reCAPTCHA est valide, continuez avec l'enregistrement de l'utilisateur
            // Votre logique de traitement ici
            System.out.println("Token reCAPTCHA valide. Enregistrement de l'utilisateur...");
        } else {
            // Le token reCAPTCHA est invalide, affichez un message d'erreur à l'utilisateur
            Alert alert = new Alert(Alert.AlertType.ERROR, "Captcha incorrect.");
            alert.showAndWait();
            return;
        }

        addUserToDatabase(name, lastname, roles, email, password, image, number, false, java.sql.Date.valueOf(datenaissance.getValue()));
        clearInputFields();
    }
    private boolean validateRecaptchaToken(String recaptchaToken) {
        // Envoyez le token reCAPTCHA au serveur Google reCAPTCHA pour validation
        // Utilisez les API reCAPTCHA pour valider le token
        // Retournez true si le token est valide, sinon false
        // Remplacez YOUR_SECRET_KEY par votre clé secrète reCAPTCHA
        String secretKey = "VOTRE_CLE_SECRETE_RECAPTCHA"; // Remplacez par votre clé secrète reCAPTCHA
        String response = sendRecaptchaValidationRequest(recaptchaToken, secretKey);
        return parseRecaptchaValidationResponse(response);
    }

    private String sendRecaptchaValidationRequest(String recaptchaToken, String secretKey) {
        // Envoyez une requête POST au serveur Google reCAPTCHA pour valider le token
        // Utilisez la bibliothèque HTTP de votre choix pour effectuer la requête POST
        // Retournez la réponse JSON du serveur
        // Exemple : Apache HttpClient, OkHttp, etc.
        // Voici un exemple de requête HTTP POST à envoyer au serveur reCAPTCHA
        // Assurez-vous d'utiliser la bibliothèque HTTP appropriée (par exemple, Apache HttpClient)
        // pour envoyer la requête POST
        // Vous devrez remplacer la ligne suivante par votre implémentation réelle
        return "{\"success\": true}"; // Réponse JSON simulée
    }

    private boolean parseRecaptchaValidationResponse(String response) {

        return response.contains("\"success\": true");
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
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("Alert.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");


        Scene scene = alert.getDialogPane().getScene();
        scene.getStylesheets().add(getClass().getResource("Alert.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/css/CustomAlertStyle.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("custom-alert");
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
