package com.example.flo.controllers.back.reponse;


import com.example.flo.controllers.back.MainWindowController;
import com.example.flo.entities.Reclamation;
import com.example.flo.entities.Reponse;
import com.example.flo.services.ReponseService;
import com.example.flo.utils.AlertUtils;
import com.example.flo.utils.BadWords;
import com.example.flo.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.URL;
import java.time.LocalDate;
import java.util.Properties;
import java.util.ResourceBundle;

public class ManageController implements Initializable {

    @FXML
    public TextField descriptionTF;
    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    Reponse currentReponse;

    public static Reclamation selectedReclamation;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        currentReponse = ShowAllController.currentReponse;

        if (currentReponse != null) {
            topText.setText("Modifier reponse");
            btnAjout.setText("Modifier");

            try {
                descriptionTF.setText(currentReponse.getDescription());
            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter reponse");
            btnAjout.setText("Ajouter");
        }
    }

    @FXML
    private void manage(ActionEvent ignored) {

        if (controleDeSaisie()) {

            Reponse reponse = new Reponse();
            reponse.setDescription(descriptionTF.getText());
            reponse.setDatemodif(LocalDate.now());
            reponse.setReclamation(selectedReclamation);

            if (currentReponse == null) {
                reponse.setDateajout(LocalDate.now());

                if (ReponseService.getInstance().add(reponse)) {
                    try {
<<<<<<< HEAD
                        sendMail("grimhype97@gmail.com");
=======
                        sendMail("oussama.bahrouni@esprit.tn");
>>>>>>> 06e48e4029121d080aecfbb04575f148468b618c
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    AlertUtils.successNotification("Reponse ajouté avec succés");
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_REPONSE);
                } else {
                    AlertUtils.errorNotification("Error");
                }
            } else {
                reponse.setId(currentReponse.getId());
                if (ReponseService.getInstance().edit(reponse)) {
                    AlertUtils.successNotification("Reponse modifié avec succés");
                    ShowAllController.currentReponse = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_REPONSE);
                } else {
                    AlertUtils.errorNotification("Error");
                }
            }

        }
    }


    private boolean controleDeSaisie() {
        if (descriptionTF.getText().isEmpty()) {
            AlertUtils.informationNotification("description ne doit pas etre vide");
            return false;
        }

        if (BadWords.filterText(descriptionTF.getText())) {
            AlertUtils.informationNotification("description contient des mots interdits");
            return false;
        }

        return true;
    }

    public static void sendMail(String recipient) throws Exception {
        System.out.println("Preparing to send email");
        Properties properties = new Properties();

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.zoho.com");
        properties.put("mail.smtp.port", "587");
        String myAccountEmail = "pidev.esprit@zohomail.com";
        String password = "tFS7s957HzVd";

        // Create a session with account credentials
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myAccountEmail, password);
            }
        });

        // Prepare email message
        Message message;
        try {
            message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Réponse reclamation");
            message.setContent("Votre reclamation a été traitée avec succès", "text/html");
        } catch (MessagingException ex) {
            System.out.println("Error in sending email");
            ex.printStackTrace();
            return;
        }

        // Send mail
        Transport.send(message);
        System.out.println("Mail sent successfully");
    }

}