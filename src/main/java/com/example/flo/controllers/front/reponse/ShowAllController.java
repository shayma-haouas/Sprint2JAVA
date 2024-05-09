package com.example.flo.controllers.front.reponse;

import com.example.flo.controllers.front.MainWindowController;
import com.example.flo.entities.Reponse;
import com.example.flo.services.ReponseService;
import com.example.flo.utils.AlertUtils;
import com.example.flo.utils.Constants;
<<<<<<< HEAD
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.event.ActionEvent;
=======
>>>>>>> 06e48e4029121d080aecfbb04575f148468b618c
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
<<<<<<< HEAD
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
=======
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
>>>>>>> 06e48e4029121d080aecfbb04575f148468b618c
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

<<<<<<< HEAD
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
=======
import java.io.IOException;
import java.net.URL;
>>>>>>> 06e48e4029121d080aecfbb04575f148468b618c
import java.util.*;

public class ShowAllController implements Initializable {

    public static Reponse currentReponse;

    @FXML
    public Text topText;
    @FXML
    public VBox mainVBox;
<<<<<<< HEAD
    @FXML
    public ComboBox<String> sortCB;
    @FXML
    public TextField searchTF;
=======

>>>>>>> 06e48e4029121d080aecfbb04575f148468b618c

    List<Reponse> listReponse;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listReponse = ReponseService.getInstance().getAll();

<<<<<<< HEAD
        sortCB.getItems().addAll(
                "Tri par description",
                "Tri par date ajout",
                "Tri par date modif"
        );

        displayData("");
    }

    void displayData(String searchText) {
=======
        displayData();
    }

    void displayData() {
>>>>>>> 06e48e4029121d080aecfbb04575f148468b618c
        mainVBox.getChildren().clear();

        Collections.reverse(listReponse);

<<<<<<< HEAD
        listReponse.stream()
                .filter(reponse -> searchText.isEmpty()
                        || reponse.getDescription().toLowerCase().contains(searchText.toLowerCase()))
                .map(this::makeReponseModel)
                .forEach(mainVBox.getChildren()::add);

        if (listReponse.isEmpty()) {
=======
        if (!listReponse.isEmpty()) {
            for (Reponse reponse : listReponse) {

                mainVBox.getChildren().add(makeReponseModel(reponse));

            }
        } else {
>>>>>>> 06e48e4029121d080aecfbb04575f148468b618c
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }

    public Parent makeReponseModel(
            Reponse reponse
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_FRONT_MODEL_REPONSE)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#descriptionText")).setText("Description : " + reponse.getDescription());
            ((Text) innerContainer.lookup("#dateajoutText")).setText("Dateajout : " + reponse.getDateajout());
            ((Text) innerContainer.lookup("#datemodifText")).setText("Datemodif : " + reponse.getDatemodif());
            ((Text) innerContainer.lookup("#reclamationText")).setText("Reclamation : " + reponse.getReclamation().toString());

<<<<<<< HEAD
            ((javafx.scene.control.Button) innerContainer.lookup("#pdfButton")).setOnAction((ignored) -> genererPDF(reponse));
=======
>>>>>>> 06e48e4029121d080aecfbb04575f148468b618c
            ((Button) innerContainer.lookup("#deleteButton")).setOnAction((ignored) -> supprimerReponse(reponse));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    private void supprimerReponse(Reponse reponse) {
        currentReponse = null;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText(null);
        alert.setContentText("Etes vous sûr de vouloir supprimer reponse ?");
        Optional<ButtonType> action = alert.showAndWait();

        if (action.isPresent()) {
            if (action.get() == ButtonType.OK) {
                if (ReponseService.getInstance().delete(reponse.getId())) {
                    MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_DISPLAY_ALL_REPONSE);
                } else {
                    AlertUtils.errorNotification("Could not delete reponse");
                }
            }
        }
    }
<<<<<<< HEAD

    @FXML
    public void sort(ActionEvent actionEvent) {
        Reponse.compareVar = sortCB.getValue();
        listReponse.sort(Reponse::compareTo);
        displayData(searchTF.getText() == null ? "" : searchTF.getText());
    }

    @FXML
    public void search(KeyEvent keyEvent) {
        displayData(searchTF.getText());
    }

    private void genererPDF(Reponse reponse) {
        String filename = "reponse.pdf";

        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, Files.newOutputStream(Paths.get(filename)));
            document.open();

            com.itextpdf.text.Font font = new com.itextpdf.text.Font();
            font.setSize(20);

            document.add(new Paragraph("- Reponse -"));

            document.add(new Paragraph("Description : " + reponse.getDescription()));
            document.add(new Paragraph("Dateajout : " + reponse.getDateajout()));
            document.add(new Paragraph("Datemodif : " + reponse.getDatemodif()));
            document.add(new Paragraph("Reclamation : " + reponse.getReclamation()));

            document.newPage();
            document.close();

            writer.close();

            Desktop.getDesktop().open(new File(filename));
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

=======
>>>>>>> 06e48e4029121d080aecfbb04575f148468b618c
}
