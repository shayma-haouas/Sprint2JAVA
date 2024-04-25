package controllers;

import entities.Sponsor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import utils.MyDatabase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Listsponsors {

    public ObservableList<Sponsor> data = FXCollections.observableArrayList();


    @FXML
    private AnchorPane content_area;

    @FXML
    private Button btnajouter;
    @FXML
    private ListView<Sponsor> listrec;





    @FXML
    private VBox SPListContainer;
    @FXML

    public void initialize() {
        // Set the SponsorListCell as the cell factory for the ListView
        listrec.setCellFactory(new SponsorCellFactory());
        listrec.setCellFactory(param -> new SponsorListCell());
        // Add listener to hide/show the scroll pane container based on list items
        // listrec.getItems().addListener((ListChangeListener<Object>) change -> {
        //   if (change.getList().isEmpty()) {
        //     SPListContainer.setVisible(false);
        //} else {
        //  SPListContainer.setVisible(true);
        //}
        //});
    }
    @FXML
    public void showw() {
        // Clear the existing data bsh metdupliquish l old data khtr k naaml show f testing o show houny ijouny mertyn
        listrec.getItems().clear();
        try {
            String requete = "SELECT * FROM sponsor";
            Statement st = MyDatabase.getInstance().getConnection().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                Sponsor r = new Sponsor(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getInt("number"));
                listrec.getItems().add(r); // Add Sponsor objects directly
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }
    public class SponsorCellFactory implements Callback<ListView<Sponsor>, ListCell<Sponsor>> {
        @Override
        public ListCell<Sponsor> call(ListView<Sponsor> param) {
            return new ListCell<Sponsor>() {
                @Override
                protected void updateItem(Sponsor sponsor, boolean empty) {
                    super.updateItem(sponsor, empty);
                    if (empty || sponsor == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        // Customize the display of the Sponsor object
                        setText(String.format("Name: %s, Number: %d, Email: %s", sponsor.getName(), sponsor.getNumber(), sponsor.getEmail()));
                    }
                }
            };
        }
    }

    public class SponsorListCell extends ListCell<Sponsor> {

        private final Button editButton;
        private final Button deleteButton;

        private final HBox buttonsContainer;
        public SponsorListCell() {
            editButton = new Button("Edit");
            deleteButton = new Button("Delete");

            editButton.setOnAction(event -> {
                Sponsor sponsor = getItem();
                // Handle edit button action for the sponsor
                System.out.println("Edit button clicked for sponsor: " + sponsor.getName());
            });

            deleteButton.setOnAction(event -> {
                Sponsor sponsor = getItem();
                // Handle delete button action for the sponsor
                System.out.println("Delete button clicked for sponsor: " + sponsor.getName());
            });


            buttonsContainer = new HBox(editButton, deleteButton);
            buttonsContainer.setAlignment(Pos.CENTER_RIGHT); // Align buttons to the right

            // Set spacing between buttons if necessary
            buttonsContainer.setSpacing(10); // Adjust the spacing as needed
        }



        @Override
        protected void updateItem(Sponsor sponsor, boolean empty) {
            super.updateItem(sponsor, empty);


            if (empty || sponsor == null) {
                setText(null);
                setGraphic(null);
            } else {
                setText("Name: " + sponsor.getName() + ", Number: " + sponsor.getNumber() + ", Email: " + sponsor.getEmail());

                setGraphic(buttonsContainer);
            }
        }
    }

    //By extending ListCell<Sponsor>, you're creating a
    // custom cell implementation tailored to display Sponsor objects in a ListView.
    //When you extend ListCell<Sponsor>, you need to override the updateItem method to define how each cell should be updated when its item changes or when it's being reused for a different item.
    // This method is crucial for customizing the appearance and behavior of your list cells.


    @FXML
    void opensponadd(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/eventback/addSponn.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            // Wait for the "addSponn" form to be closed
            stage.initModality(Modality.APPLICATION_MODAL);

            // Set onCloseRequest event handler to refresh the list of sponsors when the form is closed
            stage.setOnCloseRequest(e -> {
                // Refresh the list of sponsors
                showw();
            });

            // Show the stage and wait for it to be closed
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


