package edu.esprit.flo.controllers.reservationDechets;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.Properties;

import edu.esprit.flo.controllers.MainWindowController;
import edu.esprit.flo.entities.Dechets;
import edu.esprit.flo.entities.ReservationDechets;
import edu.esprit.flo.entities.User;
import edu.esprit.flo.services.DechetsService;
import edu.esprit.flo.services.ReservationDechetsService;
import edu.esprit.flo.utils.AlertUtils;
import edu.esprit.flo.utils.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import static com.twilio.Twilio.init;

public class ManageController implements Initializable {

    @FXML
    public DatePicker dateDP;
    @FXML
    public DatePicker dateRamassageDP;
    @FXML
    public TextField nomFournisseurTF;
    @FXML
    public TextField numeroTellTF;
    @FXML
    public TextField quantiteTF;

    @FXML
    public ComboBox<User> userCB;
    @FXML
    public ComboBox<Dechets> dechetsCB;

    @FXML
    public Button btnAjout;
    @FXML
    public Text topText;

    ReservationDechets currentReservationDechets;
    //mailing
    private final String username = "mehdi.bouazra@esprit.tn";
    private final String password = "211JMT4841";
    private final String recipientEmail = "haydar.boudhrioua@esprit.tn";
    private final String subject = "Reservation Confirmation";
    private final String messageContent = "Your reservation has been confirmed.";
    //sms
    private final String ACCOUNT_SID = "AC0cc518338273e58b968d9112e6262d8f";
    private final String AUTH_TOKEN = "941833b24afbef687dac330bb5775f97";
    public static Dechets currentDechets;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        for (User user : ReservationDechetsService.getInstance().getAllUsers()) {
            userCB.getItems().add(user);
        }

        /*or (Dechets dechets : DechetsService.getInstance().getAll()) {
            dechetsCB.getItems().add(dechets);
        }*/



        if (currentDechets != null) {
            dechetsCB.setValue(currentDechets);
        }

        currentReservationDechets = ShowAllController.currentReservationDechets;

        if (currentReservationDechets != null) {
            topText.setText("Modifier reservationDechets");
            btnAjout.setText("Modifier");

            try {
                dateDP.setValue(currentReservationDechets.getDate());
                dateRamassageDP.setValue(currentReservationDechets.getDateRamassage());
                nomFournisseurTF.setText(currentReservationDechets.getNomFournisseur());
                numeroTellTF.setText(currentReservationDechets.getNumeroTell());
                quantiteTF.setText(String.valueOf(currentReservationDechets.getQuantite()));

                userCB.setValue(currentReservationDechets.getUser());
                dechetsCB.setValue(currentReservationDechets.getDechets());

            } catch (NullPointerException ignored) {
                System.out.println("NullPointerException");
            }
        } else {
            topText.setText("Ajouter reservationDechets");
            btnAjout.setText("Ajouter");
            dateDP.setValue(LocalDate.now());

        }
    }

    @FXML
    private void manage(ActionEvent ignored) {

        if (controleDeSaisie()) {

            ReservationDechets reservationDechets = new ReservationDechets();
            reservationDechets.setDate(dateDP.getValue());
            reservationDechets.setDateRamassage(dateRamassageDP.getValue());
            reservationDechets.setNomFournisseur(nomFournisseurTF.getText());
            reservationDechets.setNumeroTell(numeroTellTF.getText());
            reservationDechets.setQuantite(Integer.parseInt(quantiteTF.getText()));

            reservationDechets.setUser(userCB.getValue());
            reservationDechets.setDechets(dechetsCB.getValue());

            if (currentReservationDechets == null) {
                if (ReservationDechetsService.getInstance().add(reservationDechets)) {
                     // New line for sending SMS confirmation
//
                    AlertUtils.makeSuccessNotification("ReservationDechets ajouté avec succés");
                    sendConfirmationEmail();
                    sendSMSConfirmation();
                    MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_RESERVATION_DECHETS);
                } else {
                    AlertUtils.makeError("Error");
                }
            } else {
                reservationDechets.setId(currentReservationDechets.getId());
                if (ReservationDechetsService.getInstance().edit(reservationDechets)) {

//
                    AlertUtils.makeSuccessNotification("ReservationDechets modifié avec succés");
                    // New line for sending SMS confirmation
                    ShowAllController.currentReservationDechets = null;
                    MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_RESERVATION_DECHETS);
                } else {
                    AlertUtils.makeError("Error");
                }
            }

        }
    }


    private boolean controleDeSaisie() {
        LocalDate today = LocalDate.now();

        if (dateDP.getValue() == null || dateDP.getValue().isBefore(today)) {
            AlertUtils.makeInformation("Veuillez choisir une date valide pour date");
            return false;
        }

        if (dateRamassageDP.getValue() == null || dateRamassageDP.getValue().isBefore(today)) {
            AlertUtils.makeInformation("Veuillez choisir une date valide pour dateRamassage");
            return false;
        }

        if (nomFournisseurTF.getText().isEmpty() || nomFournisseurTF.getText().length() < 4 || nomFournisseurTF.getText().matches(".*\\d.*")) {
            AlertUtils.makeInformation("nomFournisseur doit avoir au moins 4 caractères et ne doit pas contenir de chiffres");
            return false;
        }

        if (numeroTellTF.getText().isEmpty() || numeroTellTF.getText().length() != 8 || !numeroTellTF.getText().matches("\\d{8}")) {
            AlertUtils.makeInformation("numeroTell doit contenir exactement 8 chiffres et aucun caractère");
            return false;
        }

        if (quantiteTF.getText().isEmpty()) {
            AlertUtils.makeInformation("quantite ne doit pas etre vide");
            return false;
        }

        try {
            Integer.parseInt(quantiteTF.getText());
        } catch (NumberFormatException ignored) {
            AlertUtils.makeInformation("quantite doit etre un nombre");
            return false;
        }

        if (userCB.getValue() == null) {
            AlertUtils.makeInformation("Veuillez choisir un user");
            return false;
        }

        if (dechetsCB.getValue() == null) {
            AlertUtils.makeInformation("Veuillez choisir un dechet");
            return false;
        }

        return true;
    }


    private void sendConfirmationEmail() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            javax.mail.Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(messageContent);

            Transport.send(message);
           // System.out.println("Recipient Email: " + recipientEmail);
            System.out.println("Email sent successfully!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    private void sendSMSConfirmation() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        String TWILIO_NUMBER = "+13344544654";
        Message message = Message.creator(
                        new PhoneNumber("+21650767065"), // recipient phone number
                        new PhoneNumber(TWILIO_NUMBER), // Twilio number
                        "Your reservation has been confirmed.") // SMS message content
                .create();
        System.out.println("SMS sent successfully!");
    }

}