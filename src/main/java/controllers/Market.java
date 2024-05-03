package controllers;
import entities.Evenement;
import entities.Sponsor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import services.ServiceEvenement;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
public class Market  implements Initializable {
    @FXML
    private VBox chosenEventCard;

    @FXML
    private Label eventNameLabel;

    @FXML
    private Label eventTypeLabel;

    @FXML
    private Label eventDateLabel1;
    @FXML
    private Label eventDateLabel2;
    @FXML
    private Label eventSponsorLabel;

    @FXML
    private Label eventDescriptionLabel;

    @FXML
    private Label eventLocationLabel;

    @FXML
    private ImageView eventImg;

    @FXML
    private ScrollPane scroll;

    @FXML
    private GridPane grid;

    private List<Evenement> evenements = new ArrayList<>();
    private Image image;
    private Mylistener Mylistener;

    private ServiceEvenement serviceEvenement = new ServiceEvenement();


    private List<Evenement> getDataFromDatabase() {
        return serviceEvenement.afficherEVWithSponsors();
    }
    private void setChosenEvent(Evenement evenement) {
        eventNameLabel.setText(evenement.getNameevent());
        // Retrieve sponsor name directly from the database using the sponsor's ID
        Sponsor sponsor = evenement.getSponsor();
        if (sponsor != null) {
            eventSponsorLabel.setText("Sponsored By: " + sponsor.getName());
        } else {
            eventSponsorLabel.setText("Sponsor: Not available");
        }
        eventTypeLabel.setText("Type: " + evenement.getType());
        eventDateLabel1.setText("Start date: " + evenement.getDatedebut());
        eventDateLabel2.setText("End date: " + evenement.getDatefin());
      //aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa


        eventDescriptionLabel.setText("Description: " + evenement.getDescription());
        eventDescriptionLabel.setWrapText(true); // Enable text wrapping
        eventLocationLabel.setText("Location: " + evenement.getLieu());
        image = new Image("file:" + evenement.getImage());
        eventImg.setImage(image);
      //  chosenEventCard.setStyle("-fx-background-color: #" + evenement.getColor() + ";\n" +
        //        "    -fx-background-radius: 30;");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        evenements.addAll(getDataFromDatabase());
        if (!evenements.isEmpty()) {
            setChosenEvent(evenements.get(0));
            Mylistener = new Mylistener() {
                @Override
                public void onClickListener(Evenement evenement) {
                    setChosenEvent(evenement);
                }
            };
        }
        int column = 0;
        int row = 1;
        try {
            for (Evenement evenement : evenements) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/eventfront/card1.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                Card1 itemController = fxmlLoader.getController();
                itemController.setData(evenement, Mylistener);

                if (column == 3) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row);
                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


