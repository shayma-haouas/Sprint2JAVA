package MainJavaFx;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFx extends Application {

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/UserInterface/Login.fxml"));
            Scene scene = new Scene(root);

           // Stage.setFullScreen(false);
           // Stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/logo/favicon.png")));
           // Stage.setTitle("RadioHub");
           // Stage.setResizable(false);

            stage.setScene(scene);

            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
