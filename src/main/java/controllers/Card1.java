package controllers;
import entities.Evenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import controllers.Mylistener;

public class Card1 {
    @FXML
    private Label nameLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private ImageView img;
    @FXML
    private Label datedebutLabel;
    @FXML
    private Label datefinLabel;
    @FXML
    private Label placeLabel;
    private Evenement evenement;
    private Mylistener Mylistener;

    public void setData(Evenement evenement, Mylistener Mylistener) {
        this.evenement = evenement;
        this.Mylistener = Mylistener;
        nameLabel.setText(evenement.getNameevent());
        typeLabel.setText("Type: " + evenement.getType());

        placeLabel.setText("Location: " + evenement.getLieu());
        Image  image = new Image("file:" + evenement.getImage());
        img.setImage(image);
    }

    @FXML
    private void click(MouseEvent mouseEvent) {
        Mylistener.onClickListener(evenement);
    }
}