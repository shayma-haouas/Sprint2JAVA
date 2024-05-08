package controllers;

import entities.Evenement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import services.ServiceEvenement;
import utils.MyDatabase;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;

public class ListEvback {



    //nalabo welyna


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
    }

    @FXML
    private Pane content_area;

    @FXML
    private Button btnajouter;

    @FXML
    private ListView<Evenement> listEv;

    @FXML
    private ComboBox<?> statusInput;

    @FXML
    private TextField searchInput;

    @FXML
    private Button trie1;

    @FXML
    private Button trie;


    private ObservableList<Evenement> originalEventList;
    @FXML
    private void searchevents(ActionEvent event) {
        // Get the search term from the input field
        String searchTerm = searchInput.getText().toLowerCase();

        // If the original list is null, store it for the first time
        if (originalEventList == null) {
            originalEventList = FXCollections.observableArrayList(listEv.getItems());
        }

        // Filter the original list based on the search term
        FilteredList<Evenement> filteredList = new FilteredList<>(originalEventList);
        filteredList.setPredicate(eventy -> eventy.getNameevent().toLowerCase().contains(searchTerm));

        // Update the ListView with the filtered list
        listEv.setItems(filteredList);
    }
    @FXML
    private void triNomA(ActionEvent event) {
        // Sort the list in ascending order by Event name
        listEv.getItems().sort(Comparator.comparing(Evenement::getNameevent));
    }
    @FXML
    private void triNomD(ActionEvent event) {
        // Sort the list in descending order by Event name
        listEv.getItems().sort(Comparator.comparing(Evenement::getNameevent).reversed());
    }

    @FXML
    private void initialize() {
        // Set the EventCellFactory as the cell factory for the ListView
        //listEv.setCellFactory(new EventCellFactory());

        // Set the CustomEventCellFactory as the cell factory for the ListView
        listEv.setCellFactory(new CombinedEventCellFactory());

        // Populate the ListView with event data
        showEvents();
    }
    // Populate the ListView with event data



    @FXML
    private void showEvents() {
        // Clear the existing data
        listEv.getItems().clear();
        try {
            String query = "SELECT * FROM evenement";
            Statement statement = MyDatabase.getInstance().getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                Evenement event = new Evenement(
                        resultSet.getInt("id"),
                        resultSet.getInt("sponsor_id"),
                        resultSet.getString("nameevent"),
                        resultSet.getString("type"),
                        resultSet.getString("datedebut"),
                        resultSet.getString("datefin"),
                        resultSet.getString("description"),
                        resultSet.getInt("nbparticipant"),
                        resultSet.getString("lieu"),
                        resultSet.getString("image")
                );
                listEv.getItems().add(event); // Add Event objects directly
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Custom ListCell factory to display Event data
    class CombinedEventCellFactory implements Callback<ListView<Evenement>, ListCell<Evenement>> {
        @Override
        public ListCell<Evenement> call(ListView<Evenement> param) {
            return new ListCell<Evenement>() {
                private final Button editButton = new Button();
                private final Button deleteButton = new Button();
                private final Button participateButton = new Button("Participate");
                private final HBox buttonsContainer = new HBox(editButton, deleteButton, participateButton);
                private final BorderPane cellPane = new BorderPane();

                {
                    // Set icons for edit and delete buttons
                    FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.EDIT);
                    editIcon.setSize("30");
                    editButton.setGraphic(editIcon);
                    editButton.setStyle("-fx-background-color: transparent;"); // Remove background color
                    editButton.setCursor(Cursor.HAND); // Set cursor to hand
                    FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
                    deleteIcon.setSize("30");
                    deleteButton.setGraphic(deleteIcon);
                    deleteButton.setStyle("-fx-background-color: transparent;"); // Remove background color
                    deleteButton.setCursor(Cursor.HAND); // Set cursor to hand
                    // Add action listeners for edit and delete buttons
                    editButton.setOnAction(event -> {
                        Evenement eventt = getItem();
                        if (eventt != null) {
                            openEditEventInterface(eventt);
                        }
                    });

                    deleteButton.setOnAction(event -> {
                        Evenement eventt = getItem();
                        if (eventt != null) {
                            deleteEvent(eventt.getId());
                            listEv.getItems().remove(eventt); // Remove the event from the ListView
                        }
                    });
                    // Add action listener for the Participate button
                    participateButton.setOnAction(event -> {
                        Evenement eventt = getItem();
                        if (eventt != null) {
                            participateEvent(eventt);
                        }
                    });
                    buttonsContainer.setSpacing(0);
                    cellPane.setRight(buttonsContainer);
                }


                @Override
                protected void updateItem(Evenement event, boolean empty) {
                    super.updateItem(event, empty);
                    if (empty || event == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        // Display event details and image as before
                        ImageView imageView = new ImageView();
                        String imagePath = event.getImage();
                        if (imagePath != null && !imagePath.isEmpty()) {
                            File file = new File(imagePath);
                            if (file.exists()) {
                                imageView.setImage(new Image(file.toURI().toString()));
                            } else {
                                imageView.setImage(new Image(getClass().getResourceAsStream(imagePath)));
                            }
                        }
                        imageView.setFitWidth(100);
                        imageView.setFitHeight(100);

                        // Customize the display of the Event object
                        Label eventDetailsLabel = new Label(String.format("Event Name: %s\nType: %s\nStart Date: %s\nEnd Date: %s\nDescription: %s\nParticipants: %d\nLocation: %s",
                                event.getNameevent(), event.getType(), event.getDatedebut(), event.getDatefin(),
                                event.getDescription(), event.getNbparticipant(), event.getLieu()));

                        // Set the event details label to the center of the BorderPane
                        cellPane.setCenter(eventDetailsLabel);

                        // Set the ImageView as the left graphic of the BorderPane
                        cellPane.setLeft(imageView);

                        // Set the BorderPane as the graphic of the cell
                        setGraphic(cellPane);
                    }
                }
            };
        }

        private void openEditEventInterface(Evenement event) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/eventback/addevent.fxml"));
                Parent root = loader.load();

                // Access the controller of the edit event interface
                Crudevent controller = loader.getController();

                // Pass the selected event to the controller
                //  controller.initData(event);

                // Create a new stage for the edit event interface
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Edit Event");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                // Optionally, refresh the list view after editing
                showEvents();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void deleteEvent(int id) {
            try {
                String deleteEventSQL = "DELETE FROM evenement WHERE id = ?";
                PreparedStatement pstDeleteEvent = MyDatabase.getInstance().getConnection().prepareStatement(deleteEventSQL);
                pstDeleteEvent.setInt(1, id);

                int rowsAffectedEvent = pstDeleteEvent.executeUpdate();

                if (rowsAffectedEvent > 0) {
                    System.out.println("Event with ID " + id + " deleted successfully.");
                    // Optionally, show a success alert
                } else {
                    System.out.println("Failed to delete event with ID " + id + ".");
                    // Optionally, show an error alert
                }

                pstDeleteEvent.close();
            } catch (SQLException ex) {
                System.err.println("Error deleting event: " + ex.getMessage());
                ex.printStackTrace();
                // Optionally, show an error alert
            }
        }
        private void participateEvent(Evenement event) {
            // Call the addUserToEvent method from the ServiceEvenement class
            ServiceEvenement serviceEvenement = new ServiceEvenement();
            // Assuming getCurrentUserId() returns the ID of the current user
            int userId =1; // Provide the user ID here
            serviceEvenement.updateParticipantsCount( event.getId());
        }
    }



    @FXML
    void openaddev(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eventback/addevent.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Wait for the "addSponn" form to be closed
            stage.initModality(Modality.APPLICATION_MODAL);

            // Set onCloseRequest event handler to refresh the list of sponsors when the form is closed
            stage.setOnCloseRequest(e -> {
                // Refresh the list of sponsors
                showEvents();
            });

            // Show the stage and wait for it to be closed
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }}


