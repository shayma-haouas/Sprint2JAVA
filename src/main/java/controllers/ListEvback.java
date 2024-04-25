package controllers;

import entities.Evenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import utils.MyDatabase;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ListEvback {
    @FXML
    private Pane content_area;

    @FXML
    private Button btnajouter;

    @FXML
    private ListView<Evenement> listEv;

    @FXML
    private ComboBox<?> statusInput;


    @FXML
    private void initialize() {
        // Set the EventCellFactory as the cell factory for the ListView
        listEv.setCellFactory(new EventCellFactory());

        // Populate the ListView with event data
        showEvents();
    }

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
    class EventCellFactory implements Callback<ListView<Evenement>, ListCell<Evenement>> {
        @Override
        public ListCell<Evenement> call(ListView<Evenement> param) {
            return new ListCell<Evenement>() {
                @Override
                protected void updateItem(Evenement event, boolean empty) {
                    super.updateItem(event, empty);
                    if (empty || event == null) {
                        setText(null);
                        setGraphic(null);

                    } else{


                        // Create an ImageView to display the event image


                        // Set the image to the ImageView
                        // Assuming event.getImage() returns the path to the image file
                        // String imagePath = "file:///C:\\Users\\Chaima\\Downloads\\Teach Green_ Lesson Plans on Recycling.jpg";
                        //imageView.setImage(new Image(imagePath));
                        // Create an ImageView to display the event image
                        ImageView imageView = new ImageView();
                        String imagePath = event.getImage(); // Assuming this returns the correct path

// Check if the path is null or empty
                        if (imagePath != null && !imagePath.isEmpty()) {
                            // Create a File object to check if it's a local file
                            File file = new File(imagePath);
                            if (file.exists()) {
                                // Load the image directly from the file path
                                imageView.setImage(new Image(file.toURI().toString()));
                            } else {
                                // Load the image as a resource
                                imageView.setImage(new Image(getClass().getResourceAsStream(imagePath)));
                            }
                        }
                        // Set the size of the ImageView (optional)
                        imageView.setFitWidth(100);
                        imageView.setFitHeight(100);

                        // Customize the display of the Event object
                        setText(String.format("Event Name: %s\nType: %s\nStart Date: %s\nEnd Date: %s\nDescription: %s\nParticipants: %d\nLocation: %s",
                                event.getNameevent(), event.getType(), event.getDatedebut(), event.getDatefin(),
                                event.getDescription(), event.getNbparticipant(), event.getLieu()));

                        // Set the ImageView as the graphic of the cell
                        setGraphic(imageView);
                    }
                }
            };
        }
    }





    @FXML
    void openaddev(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/evenement/addevent.fxml"));
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
    }

}


