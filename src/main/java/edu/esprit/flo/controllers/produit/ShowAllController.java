package edu.esprit.flo.controllers.produit;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import edu.esprit.flo.controllers.MainWindowController;
import edu.esprit.flo.controllers.commande.ManageController;
import edu.esprit.flo.entities.Produit;
import edu.esprit.flo.services.ProduitService;
import edu.esprit.flo.utils.AlertUtils;
import edu.esprit.flo.utils.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class ShowAllController implements Initializable {

    public static Produit currentProduit;

    @FXML
    public Text topText;
    @FXML
    public VBox mainVBox;
    public TextField searchTF;

    List<Produit> listProduit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listProduit = ProduitService.getInstance().getAll();

        displayData("");
    }

    void displayData(String search) {
        mainVBox.getChildren().clear();

        Collections.reverse(listProduit);

        listProduit.stream()
                .filter(produit -> search.isEmpty() || produit.getNomp().toLowerCase().contains(search.toLowerCase()))
                .map(this::makeProduitModel)
                .forEach(mainVBox.getChildren()::add);

        if (listProduit.isEmpty()) {
            StackPane stackPane = new StackPane();
            stackPane.setAlignment(Pos.CENTER);
            stackPane.setPrefHeight(200);
            stackPane.getChildren().add(new Text("Aucune donnée"));
            mainVBox.getChildren().add(stackPane);
        }
    }


    public Parent makeProduitModel(
            Produit produit
    ) {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.FXML_FRONT_MODEL_PRODUIT)));

            HBox innerContainer = ((HBox) ((AnchorPane) ((AnchorPane) parent).getChildren().get(0)).getChildren().get(0));
            ((Text) innerContainer.lookup("#nompText")).setText("Nomp : " + produit.getNomp());
            ((Text) innerContainer.lookup("#descpText")).setText("Descp : " + produit.getDescp());
            ((Text) innerContainer.lookup("#catgText")).setText("Catg : " + produit.getCatg());
            ((Text) innerContainer.lookup("#prixText")).setText("Prix : " + produit.getPrix());

            Path selectedImagePath = FileSystems.getDefault().getPath(produit.getImage());
            if (selectedImagePath.toFile().exists()) {
                ((ImageView) innerContainer.lookup("#imageIV")).setImage(new Image(selectedImagePath.toUri().toString()));
            }

            try {
                String data = produit.allAttrToString();
                String path = "./qr_code.jpg";
                BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 500, 500);
                MatrixToImageWriter.writeToPath(matrix, "jpg", Paths.get(path));

                Path qrPath = FileSystems.getDefault().getPath(path);
                if (qrPath.toFile().exists()) {
                    ((ImageView) innerContainer.lookup("#qrImage")).setImage(new Image(qrPath.toUri().toString()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            ((Button) innerContainer.lookup("#cmdBtn")).setOnAction((event) -> commanderProduit(produit));
            ((Button) innerContainer.lookup("#pdfBtn")).setOnAction((event) -> genererPDF(produit));


        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return parent;
    }

    private void commanderProduit(Produit produit) {
        ManageController.selectedProduit = produit;
        currentProduit = produit;
        MainWindowController.getInstance().loadInterface(Constants.FXML_FRONT_MANAGE_COMMANDE);
    }

    public void search(KeyEvent keyEvent) {
        displayData(searchTF.getText());
    }

    private void genererPDF(Produit produit) {
        String filename = "produit.pdf";

        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, Files.newOutputStream(Paths.get(filename)));
            document.open();

            com.itextpdf.text.Font font = new com.itextpdf.text.Font();
            font.setSize(20);

            document.add(new Paragraph("- Reclamation -"));

            try {
                com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(produit.getImage());
                image.scaleAbsoluteWidth(300);
                image.scaleAbsoluteHeight(300);
                image.isScaleToFitHeight();
                document.add(image);
            } catch (FileNotFoundException e) {
                System.out.println("Image not found");
            }

            document.add(new Paragraph("Nom : " + produit.getNomp(), font));
            document.add(new Paragraph("Description : " + produit.getDescp(), font));
            document.add(new Paragraph("Categorie : " + produit.getCatg(), font));
            document.add(new Paragraph("Prix : " + produit.getPrix(), font));

            document.newPage();
            document.close();

            writer.close();

            Desktop.getDesktop().open(new File(filename));
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}
