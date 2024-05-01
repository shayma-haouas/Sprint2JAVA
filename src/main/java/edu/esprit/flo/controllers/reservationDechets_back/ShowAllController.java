package edu.esprit.flo.controllers.reservationDechets_back;

import edu.esprit.flo.controllers.MainWindowControllerBack;
import edu.esprit.flo.entities.Dechets;
import edu.esprit.flo.entities.ReservationDechets;
import edu.esprit.flo.services.ReservationDechetsService;
import edu.esprit.flo.utils.AlertUtils;
import edu.esprit.flo.utils.Constants;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ShowAllController implements Initializable {

    public static ReservationDechets currentReservationDechets;

    @FXML
    public Text topText;
    @FXML
    public VBox mainVBox;

    @FXML
    private TextField searchField4;
    List<ReservationDechets> listReservationDechets;
    @FXML
    private Button trie2;

    @FXML
    private Button trie3;

    @FXML
    private Button print;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listReservationDechets = ReservationDechetsService.getInstance().getAll();

        displayData();
        searchField4.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.isEmpty()) {
                    List<ReservationDechets> searchResults = ReservationDechetsService.getInstance().search(newValue);
                    if (!searchResults.isEmpty()) {
                        listReservationDechets.clear();
                        listReservationDechets.addAll(searchResults);
                        displayData();
                    } else {
                        AlertUtils.makeInformation("No matching results found.");
                    }
                } else {
                    // If the search field is empty, display all data
                    listReservationDechets.clear();
                    listReservationDechets.addAll(ReservationDechetsService.getInstance().getAll());
                    displayData();
                }
            }
        });
    }

    void displayData() {
        mainVBox.getChildren().clear();

        Collections.reverse(listReservationDechets);

        if (!listReservationDechets.isEmpty()) {
            for (ReservationDechets reservationDechets : listReservationDechets) {

                mainVBox.getChildren().add(makeReservationDechetsModel(reservationDechets));

            }
        } else {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune Donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeReservationDechetsModel(
            ReservationDechets reservationDechets
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_BACK_MODEL_RESERVATION_DECHETS)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#dateText")).setText("Date : " + reservationDechets.getDate());
            ((Text) innerContainer.lookup("#dateRamassageText")).setText("DateRamassage : " + reservationDechets.getDateRamassage());
            ((Text) innerContainer.lookup("#nomFournisseurText")).setText("NomFournisseur : " + reservationDechets.getNomFournisseur());
            ((Text) innerContainer.lookup("#numeroTellText")).setText("NumeroTell : " + reservationDechets.getNumeroTell());
            ((Text) innerContainer.lookup("#quantiteText")).setText("Quantite : " + reservationDechets.getQuantite());
            ((Text) innerContainer.lookup("#userText")).setText("User : " + (reservationDechets.getUser() == null ? "null" : reservationDechets.getUser().toString()));
            ((Text) innerContainer.lookup("#dechetTextt")).setText("Dechet : " + (reservationDechets.getDechets() == null ? "null" : reservationDechets.getDechets().toString()));

            ((Button) innerContainer.lookup("#editButton")).setOnAction((event) -> modifierReservationDechets(reservationDechets));
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((event) -> supprimerReservationDechets(reservationDechets));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    private void modifierReservationDechets(ReservationDechets reservationDechets) {
        currentReservationDechets = reservationDechets;
        MainWindowControllerBack.getInstance().loadInterface(Constants.FXML_BACK_MANAGE_RESERVATION_DECHETS);
    }

    private void supprimerReservationDechets(ReservationDechets reservationDechets) {
        currentReservationDechets = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer reservationDechets ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.isPresent()) {
            if (action.get() == ButtonType.OK) {
                if (ReservationDechetsService.getInstance().delete(reservationDechets.getId())) {
                    MainWindowControllerBack.getInstance().loadInterface(Constants.FXML_BACK_DISPLAY_ALL_RESERVATION_DECHETS);
                } else {
                    AlertUtils.makeError("Could not delete reservationDechets");
                }
            }
        }
    }

    @FXML
    private void triNomD(ActionEvent event) {
        // Sort the list in descending order by Sponsor name
        listReservationDechets.sort(Comparator.comparing(ReservationDechets::getQuantite).reversed());
        displayData();
    }

    // Method to perform sorting in ascending order
    @FXML
    private void triNomA(ActionEvent event) {
        // Sort the list in ascending order by Sponsor name
        listReservationDechets.sort(Comparator.comparing(ReservationDechets::getQuantite));
        displayData();
    }
    public void generatePDF(List<ReservationDechets> reservationDechetsList) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File selectedFile = fileChooser.showSaveDialog(new Stage());

        if (selectedFile != null) {
            String dest = selectedFile.getAbsolutePath();
            try {
                PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
                Document document = new Document(pdfDoc);

                // Add a title to the document
                Paragraph title = new Paragraph("Liste des réservations de déchets");
                title.setFontSize(16).setBold();
                document.add(title);

                // Create a table to display the data
                Table table = new Table(7);
                table.addCell("Date");
                table.addCell("Date de Ramassage");
                table.addCell("Nom du Fournisseur");
                table.addCell("Numéro de Téléphone");
                table.addCell("Quantité");
                table.addCell("User");
                table.addCell("Déchet");

                // Fill the table with data from the reservationDechetsList
                for (ReservationDechets reservationDechets : reservationDechetsList) {
                    table.addCell(reservationDechets.getDate().toString()); // Assuming you want to convert LocalDate to String
                    table.addCell(reservationDechets.getDateRamassage().toString()); // Assuming you want to convert LocalDate to String
                    table.addCell(reservationDechets.getNomFournisseur());
                    table.addCell(reservationDechets.getNumeroTell());
                    table.addCell(String.valueOf(reservationDechets.getQuantite()));
                    table.addCell(reservationDechets.getUser() != null ? reservationDechets.getUser().toString() : "null");
                    table.addCell(reservationDechets.getDechets() != null ? reservationDechets.getDechets().toString() : "null");
                }

                // Add the table to the document
                document.add(table);

                // Close the document
                document.close();

                // Show a success alert
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("PDF file saved successfully!");
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No file selected.");
        }
    }
    @FXML
    void print(ActionEvent event) {
        List<ReservationDechets> reservationDechetsList = listReservationDechets;
        generatePDF(reservationDechetsList);
    }
}
