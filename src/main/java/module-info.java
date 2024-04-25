module com.example.flo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.flo to javafx.fxml;
    opens com.example.flo.entities to javafx.fxml;
    opens com.example.flo.controllers to javafx.fxml;
    opens com.example.flo.controllers.back to javafx.fxml;
    opens com.example.flo.controllers.front to javafx.fxml;
    opens com.example.flo.controllers.front.don to javafx.fxml;
    opens com.example.flo.controllers.front.factureDon to javafx.fxml;
    opens com.example.flo.controllers.back.don to javafx.fxml;
    opens com.example.flo.controllers.back.factureDon to javafx.fxml;

    exports com.example.flo;
    exports com.example.flo.entities;
    exports com.example.flo.controllers;
    exports com.example.flo.controllers.back;
    exports com.example.flo.controllers.front;
    exports com.example.flo.controllers.front.don;
    exports com.example.flo.controllers.front.factureDon;
    exports com.example.flo.controllers.back.don;
    exports com.example.flo.controllers.back.factureDon;
}