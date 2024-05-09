package com.example.flo.controllers.back.reclamation;

public class pagination {
    import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

    public class PaginationExample extends Application {

        private static final int ITEMS_PER_PAGE = 5;
        private static final int TOTAL_ITEMS = 20;

        @Override
        public void start(Stage primaryStage) {
            BorderPane root = new BorderPane();

            // Create pagination control
            Pagination pagination = new Pagination();
            pagination.setPageCount(getPageCount());
            pagination.setPageFactory(this::createPage);

            root.setCenter(pagination);

            Scene scene = new Scene(root, 400, 300);
            primaryStage.setTitle("Pagination Example");
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        private int getPageCount() {
            return (int) Math.ceil((double) TOTAL_ITEMS / ITEMS_PER_PAGE);
        }

        private VBox createPage(int pageIndex) {
            VBox pageBox = new VBox();
            int startIndex = pageIndex * ITEMS_PER_PAGE;
            int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, TOTAL_ITEMS);

            // Add items to the page
            for (int i = startIndex; i < endIndex; i++) {
                pageBox.getChildren().add(new javafx.scene.control.Label("Item " + (i + 1)));
            }

            return pageBox;
        }

        public static void main(String[] args) {
            launch(args);
        }
    }

}
