package MainJavaFx;



    import javafx.application.Application;
    import javafx.fxml.FXMLLoader;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.input.MouseEvent;
    import javafx.scene.paint.Color;
    import javafx.stage.Stage;
    import javafx.stage.StageStyle;

public class MainFx extends Application {

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/UserInterface/SideBar.fxml"));
            Scene scene = new Scene(root);


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
