package controllers;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import entities.Evenement;
import entities.Sponsor;
import entities.User;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



import services.Scalendar;
import services.ServiceEvenement;
import javafx.scene.image.ImageView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.scene.control.TextField;
import utils.MyDatabase;
import javafx.scene.control.DatePicker;
import com.google.api.services.calendar.model.Event;
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
    private Label eventID;
    @FXML
    private Label eventLocationLabel;
    @FXML
    private Label nbpartc;

    @FXML
    private ImageView eventImg;
    @FXML
    private ImageView qrCodeImageView;
    @FXML
    private ScrollPane scroll;

    @FXML
    private GridPane grid;

    private List<Evenement> evenements = new ArrayList<>();
    private Image image;
    private Mylistener Mylistener;

    private ServiceEvenement serviceEvenement = new ServiceEvenement();
    private int selectedEventId; // Variable to store the selected event ID
    private Mylistener mylistener;
    // private User currentUser;
    private Scalendar scalendarService;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField typeFilterTextField;

    @FXML
    private TextField locationFilterTextField;

    @FXML
    private DatePicker startDateFilterDatePicker;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate grid with initial data
        populateGrid();

        // Attach event listener to the searchTextField
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchEvents(newValue);
        });

        // Fetch data from the database
        evenements.addAll(getDataFromDatabase());

        // Set listener for event clicks
        Mylistener = evenement -> setChosenEvent(evenement);

        // Display events in the grid
        displayEventsInGrid();
    }


    private List<Evenement> getDataFromDatabase() {
        return serviceEvenement.afficherEVWithSponsors();
    }
    @FXML
    private void goToRecommendedEvents(MouseEvent event) {
        try {
            // Load the FXML file of the recommended events view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eventfront/RECCEV.fxml"));
            Parent root = loader.load();

            // Get the controller instance
            REC recommendedEventsController = loader.getController();

            // Populate recommended events
            recommendedEventsController.populateRecommendedEvents();

            // Set up the stage and scene
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Show the recommended events view
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setChosenEvent(Evenement evenement) {
        selectedEventId = evenement.getId();

        // Event Name
        eventNameLabel.setText("Event: " + evenement.getNameevent());

        eventNameLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 24; -fx-font-weight: bold;");


        // Event Type
        eventTypeLabel.setText("Type: " + evenement.getType());
        eventTypeLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 18;");
        eventTypeLabel.setGraphic(createIcon("/imgs/type.png"));

        // Start Date
        eventDateLabel1.setText("Start Date: " + evenement.getDatedebut());
        eventDateLabel1.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 18;");
        eventDateLabel1.setGraphic(createIcon("/imgs/calendar.png"));

        // End Date
        eventDateLabel2.setText("End Date: " + evenement.getDatefin());
        eventDateLabel2.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 18;");
        eventDateLabel2.setGraphic(createIcon("/imgs/calendar.png"));

        // Sponsor
        eventSponsorLabel.setText("Sponsor: " + evenement.getSponsor().getName());
        eventSponsorLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 18;");
        eventSponsorLabel.setGraphic(createIcon("/imgs/calendar.png"));

        // Description
        eventDescriptionLabel.setText("Description: " + evenement.getDescription());
        eventDescriptionLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 18;");
        eventDescriptionLabel.setGraphic(createIcon("/imgs/calendar.png"));

        // Location
        eventLocationLabel.setText("Location: " + evenement.getLieu());
        eventLocationLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 18;");
        eventLocationLabel.setGraphic(createIcon("/imgs/calendar.png"));

        // Number of Participants
        nbpartc.setText("Participants: " + evenement.getNbparticipant());
        nbpartc.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 18;");
        nbpartc.setGraphic(createIcon("/imgs/calendar.png"));

        // Load event image
        Image image = new Image("file:" + evenement.getImage());
        eventImg.setImage(image);

        // Set alignment for labels


    }
    private Node createIcon(String imagePath) {
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
        icon.setFitWidth(16); // Set width of the icon
        icon.setFitHeight(16); // Set height of the icon
        return icon;
    }




    @FXML
    private void participate(ActionEvent event) {
        int eventId = selectedEventId;
        int userId = 1;
        try {
            serviceEvenement.updateParticipantsCount(eventId);
            serviceEvenement.addUserToEvent(userId, eventId);

            System.out.println("Participation recorded for event ID: " + eventId + " and user ID: " + userId);
            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setTitle("Participation Confirmed");
            infoAlert.setHeaderText(null);
            infoAlert.setContentText("You have successfully participated in the event!");
            infoAlert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Could not update participation");
            alert.setContentText("An error occurred: " + e.getMessage());
            alert.showAndWait();
        }
    }

   /* @FXML
    private void participate(ActionEvent event) {
        // Use the stored selected event ID
        int eventId = selectedEventId;
        int userId = 1; // Replace with the actual user ID
        // Use the stored selected event ID

        // Call the addUserToEvent function
        ServiceEvenement serviceEvenement = new ServiceEvenement();
        serviceEvenement.updateParticipantsCount( eventId);

        // Add any further logic after participating in the event
    }
*/


    private void searchEvents(String keyword) {
        // Filter events based on the search keyword
        List<Evenement> searchResults = evenements.stream()
                .filter(event -> event.getNameevent().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());

        // Update the grid with the search results
        displayEventsInGrid(searchResults);
    }

    private void displayEventsInGrid(List<Evenement> events) {
        // Clear existing content in the grid
        grid.getChildren().clear();

        // Populate the grid with events
        int column = 0;
        int row = 1;
        try {
            for (Evenement evenement : events) {
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

    private void displayEventsInGrid() {
        // Clear existing content in the grid
        grid.getChildren().clear();

        // Populate the grid with events
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


    private void populateGrid() {
        // Your existing code to populate the grid with initial data
    }
    @FXML
    private void applyFilters(ActionEvent event) {
        String typeFilter = typeFilterTextField.getText().trim().toLowerCase();
        String locationFilter = locationFilterTextField.getText().trim().toLowerCase();
        LocalDate startDateFilter = startDateFilterDatePicker.getValue();

        // Filter the event list based on the criteria
        List<Evenement> filteredEvents = evenements.stream()
                .filter(evenement -> evenement.getType().toLowerCase().contains(typeFilter))
                .filter(evenement -> evenement.getLieu().toLowerCase().contains(locationFilter))
                .filter(evenement -> startDateFilter == null ||
                        LocalDate.parse(evenement.getDatedebut()).isAfter(startDateFilter))
                .collect(Collectors.toList());

        // Update the displayed events with the filtered list
        updateDisplayedEvents(filteredEvents);
    }

    private void updateDisplayedEvents(List<Evenement> events) {
        grid.getChildren().clear(); // Clear existing display

        // Populate the grid with the filtered events
        int column = 0;
        int row = 1;
        try {
            for (Evenement evenement : events) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/eventfront/card1.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                Card1 itemController = fxmlLoader.getController();
                itemController.setData(evenement, mylistener);

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

    //EKHR MERAAA SINON BEDLO
    //
    //
    //
    //
    //private Evenement evenement;
   //
    /*@FXML
    private void participate(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/eventfront/details.fxml"));
        Parent root;
        try {
            root = loader.load();
            // Get the controller of the loaded FXML
            Details controller = loader.getController();
            // Pass the selected event details to the controller
            controller.setEvent(evenement);

            // Create a new scene with the details interface
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/

}













/*
    private List<Evenement> getDataFromDatabase() {
        return serviceEvenement.afficherEVWithSponsors();
    }
    private void setChosenEvent(Evenement evenement) {
        selectedEventId = evenement.getId(); //BRASS OMK EKHDMY Y ZEBY
        System.out.println("Selected Event ID: " + selectedEventId);
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

        nbpartc.setText("nb: " + evenement.getNbparticipant());
        eventDescriptionLabel.setText("Description: " + evenement.getDescription());
        eventDescriptionLabel.setWrapText(true); // Enable text wrapping
        eventLocationLabel.setText("Location: " + evenement.getLieu());
        image = new Image("file:" + evenement.getImage());
        eventImg.setImage(image);
      //  chosenEventCard.setStyle("-fx-background-color: #" + evenement.getColor() + ";\n" +
        //        "    -fx-background-radius: 30;");
    }




    private void populateGrid() {
        // Your existing code to populate the grid with initial data
    }





    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateGrid();

        // Attach event listener to the searchTextField
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchEvents(newValue);
        });
        evenements.addAll(getDataFromDatabase());
        if (!evenements.isEmpty()) {
            setChosenEvent(evenements.get(0));
            Mylistener = new Mylistener() {
                @Override
                public void onClickListener(Evenement evenement) {
                    setChosenEvent(evenement);
                   // Set the selected event
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


    @FXML
    private void applyFilters(ActionEvent event) {
        String typeFilter = typeFilterTextField.getText().trim().toLowerCase();
        String locationFilter = locationFilterTextField.getText().trim().toLowerCase();
        LocalDate startDateFilter = startDateFilterDatePicker.getValue();

        // Filter the event list based on the criteria
        List<Evenement> filteredEvents = evenements.stream()
                .filter(evenement -> evenement.getType().toLowerCase().contains(typeFilter))
                .filter(evenement -> evenement.getLieu().toLowerCase().contains(locationFilter))
                .filter(evenement -> startDateFilter == null ||
                        LocalDate.parse(evenement.getDatedebut()).isAfter(startDateFilter))
                .collect(Collectors.toList());

        // Update the displayed events with the filtered list
        updateDisplayedEvents(filteredEvents);
    }

    private void updateDisplayedEvents(List<Evenement> events) {
        grid.getChildren().clear(); // Clear existing display

        // Populate the grid with the filtered events
        int column = 0;
        int row = 1;
        try {
            for (Evenement evenement : events) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/eventfront/card1.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                Card1 itemController = fxmlLoader.getController();
                itemController.setData(evenement, mylistener);

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
    private void searchEvents(String keyword) {
        List<Evenement> searchResults = evenements.stream()
                .filter(event -> event.getNameevent().toLowerCase().contains(keyword.toLowerCase())
                        || event.getLieu().toLowerCase().contains(keyword.toLowerCase())
                        || event.getType().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());

        // Clear previous content in the grid
        grid.getChildren().clear();

        int column = 0;
        int row = 1;
        try {
            for (Evenement evenement : searchResults) {
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




*/









/*
    @FXML
    private void goToRecommendedEvents(ActionEvent event) {
        try {
            // Load the FXML file of the recommended events view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eventfront/RECCEV.fxml"));
            Parent root = loader.load();

            // Get the controller instance
           REC recommendedEventsController = loader.getController();

            // Populate recommended events
            recommendedEventsController.populateRecommendedEvents();

            // Set up the stage and scene
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Show the recommended events view
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
/*
    @FXML
    private void showCalendar() {
        try {
            List<Event> googleEvents = scalendarService.fetchEventsFromEvenementEntity();
            if (googleEvents.isEmpty()) {
                System.out.println("No events found.");
                return; // Exit early if no events are found
            }

            System.out.println("Displaying events...");
            for (Event event : googleEvents) {
                System.out.println(event.getSummary());
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch events: " + e.getMessage());
            e.printStackTrace();
        }
    }*/




    /* int userId = 1;
    @FXML
    private void addToCartButtonClicked(ActionEvent event) {
        if (selectedEventId != -1 && userId != 0) {
            serviceEvenement.addUserToEvent(userId, selectedEventId);
            showAlert("You have successfully participated in the event!");
            // Update the UI or take further actions as needed
        } else {
            showAlert("Please select an event first!");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }*/

    //   Make sure to adjust the User entity according to your application's structure and to set the currentUser appropriately when a user logs in. This controller








//    private int getUserId() {
    // Implement this method to retrieve the user ID
    //      return 1; // Placeholder value
    // }




    /*@FXML
    private void generateQRCode(ActionEvent event) {
        String nom = eventNameLabel.getText();
        String date = eventDateLabel1.getText();
        String datefin = eventDateLabel2.getText();
        String lieu = eventTypeLabel.getText();

        // Example value for demonstration purpose, replace with actual value if necessary
        int nbParticipant = 100;
        System.out.println("Nom: " + nom);
        System.out.println("Date: " + date);
        System.out.println("Date de fin: " + datefin);
        System.out.println("Lieu: " + lieu);

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        String information = "Nom: " + nom + "\n" + "Lieu: " + lieu + "\n" + "Date: " + date + "\n" + "Nombre de participants restant: " + nbParticipant + "\n" + "Date de fin: " + datefin;
        int width = 300; // Adjust width and height as needed
        int height = 300;

        BufferedImage bufferedImage = null;
        try {
            BitMatrix byteMatrix = qrCodeWriter.encode(information, BarcodeFormat.QR_CODE, width, height);
            bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    bufferedImage.setRGB(i, j, byteMatrix.get(i, j) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
                }
            }
            qrCodeImageView.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
        } catch (WriterException ex) {
            ex.printStackTrace();
        }
    }*/




