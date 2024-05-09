module com.example.flo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.controlsfx.controls;
    requires java.mail;
    requires org.apache.poi.poi;
    requires java.desktop;
    requires itextpdf;

    opens com.example.flo to javafx.fxml;
    opens com.example.flo.entities to javafx.fxml;
    opens com.example.flo.controllers to javafx.fxml;
    opens com.example.flo.controllers.back to javafx.fxml;
    opens com.example.flo.controllers.front to javafx.fxml;
    opens com.example.flo.controllers.front.reclamation to javafx.fxml;
    opens com.example.flo.controllers.front.reponse to javafx.fxml;
    opens com.example.flo.controllers.back.reclamation to javafx.fxml;
    opens com.example.flo.controllers.back.reponse to javafx.fxml;

    exports com.example.flo;
    exports com.example.flo.entities;
    exports com.example.flo.controllers;
    exports com.example.flo.controllers.back;
    exports com.example.flo.controllers.front;
    exports com.example.flo.controllers.front.reclamation;
    exports com.example.flo.controllers.front.reponse;
    exports com.example.flo.controllers.back.reclamation;
    exports com.example.flo.controllers.back.reponse;
}