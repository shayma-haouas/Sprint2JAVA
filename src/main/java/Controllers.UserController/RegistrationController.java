package Controllers.UserController;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import entities.User;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import javafx.util.Duration;
import services.UserService;
import nl.captcha.Captcha;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.noise.CurvedLineNoiseProducer;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

public class RegistrationController  implements Initializable {



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


    private String imagePath;

    @FXML
    private Button cam;
    String pic ;
    @FXML
    private ImageView cap;

    @FXML
    private TextField code;

    @FXML
    private Button reset;

    @FXML
    private Button submit;

    public Captcha setCaptcha() {
        Captcha captchaV = new Captcha.Builder(250, 200)
            .addText()
            .addBackground(new GradiatedBackgroundProducer()) // Ajout d'un fond gradient
            .addNoise()
            .addBorder()
            .build();

        System.out.println(captchaV.getImage());
        Image image = SwingFXUtils.toFXImage(captchaV.getImage(), null);

        cap.setImage(image);

        return captchaV;
    }


    Captcha captcha;



    @FXML
    public void initialize(URL url, ResourceBundle rb) {

        // Chargez le captcha dans le WebView
        captcha =  setCaptcha();

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


        String enteredCaptcha = code.getText();
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



        if (isValidCaptcha==false) {
            showAlert(Alert.AlertType.ERROR, "Alert", "Erreur Saisie", "Captcha incorrect.", StageStyle.DECORATED);


        } else {

            addUserToDatabase(name, lastname, password, email, roles, image, number, false, java.sql.Date.valueOf(datenaissance.getValue()));
        }



        clearInputFields();
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
            // Obtenez le chemin du dossier où les images capturées sont sauvegardées
            String folderPath = "C:\\Users\\siwar\\OneDrive\\Bureau\\JAVASPRINT\\Sprint2JAVA\\src\\main\\resources\\img\\uploads\\";

            // Obtenez le nom du fichier sélectionné
            String fileName = selectedFile.getName();

            // Concaténez le nom du fichier avec le chemin du dossier pour obtenir le chemin complet du fichier de destination
            String destinationPath = folderPath + fileName;

            // Essayez de copier le fichier sélectionné vers le dossier de destination
            try {
                Files.copy(selectedFile.toPath(), Paths.get(destinationPath), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image saved: " + destinationPath);

                // Charger l'image à partir du chemin du fichier de destination
                Image image = new Image(selectedFile.toURI().toString());

                // Afficher l'image dans l'imageView
                imageView.setImage(image);

                // Enregistrer le chemin du fichier sélectionné
                this.imagePath = destinationPath;

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to save image: " + destinationPath);
            }
        }
        Image image = new Image(new File(imagePath).toURI().toString());

        // Affichez l'image dans l'imageView destiné à afficher la photo de profil
        imageView.setImage(image);
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

        String imageName = new File(image).getPath(); // Récupérer le nom de fichier à partir du chemin complet
        User user = new User(name, lastname, roles, email, password, imageName, number, is_verified, datenaissance);
        UserService userService = new UserService();
        userService.signUp(user);
        boolean ajoutReussi = true;

        if (ajoutReussi) {
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur ajouté", "L'utilisateur a été ajouté avec succès à la base de données.", StageStyle.DECORATED);
        } else {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur d'ajout", "Une erreur s'est produite lors de l'ajout de l'utilisateur à la base de données.", StageStyle.DECORATED);
        }
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


    public void reseting(ActionEvent actionEvent) {
        captcha =  setCaptcha();
        code.setText("");
    }

    public void submit(ActionEvent actionEvent)  throws IOException{
        if (captcha.isCorrect(code.getText())) {

            String tilte = "Captcha";
            String message = "Vous avez saisi le code avec succés!";
            TrayNotification tray = new TrayNotification();
            AnimationType type = AnimationType.POPUP;

            tray.setAnimationType(type);
            tray.setTitle(tilte);
            tray.setMessage(message);
            tray.setNotificationType(NotificationType.SUCCESS);
            tray.showAndDismiss(Duration.millis(3000));

            //     try {

//            stage.show();
            //Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
          //  stage.close();

//        } catch (IOException ex) {
//            Logger.getLogger(Agent_mainController.class.getName()).log(Level.SEVERE, null, ex);
        }

        else {

            String tilte = "Captcha";
            String message = "Vous avez saisi un faux code !";
            TrayNotification tray = new TrayNotification();
            AnimationType type = AnimationType.POPUP;

            tray.setAnimationType(type);
            tray.setTitle(tilte);
            tray.setMessage(message);
            tray.setNotificationType(NotificationType.ERROR);
            tray.showAndDismiss(Duration.millis(3000));

            captcha =  setCaptcha();
            code.setText("");
        }


    }

    private boolean validateCaptcha(String enteredCaptcha) {
        System.out.println(enteredCaptcha.equalsIgnoreCase(code.getText()));
        // Comparer le captcha entré par l'utilisateur avec le texte du captcha généré
        return enteredCaptcha.equalsIgnoreCase(code.getText());

    }

}
