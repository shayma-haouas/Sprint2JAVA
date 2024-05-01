module edu.esprit.flo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires de.jensd.fx.glyphs.fontawesome;
    requires kernel;
    requires layout;
    requires javax.mail.api;
    requires twilio;

    opens edu.esprit.flo to javafx.fxml;
    opens edu.esprit.flo.entities to javafx.fxml;
    opens edu.esprit.flo.controllers to javafx.fxml;
    opens edu.esprit.flo.controllers.dechets to javafx.fxml;
    opens edu.esprit.flo.controllers.reservationDechets to javafx.fxml;
    opens edu.esprit.flo.controllers.dechets_back to javafx.fxml;
    opens edu.esprit.flo.controllers.reservationDechets_back to javafx.fxml;

    exports edu.esprit.flo;
    exports edu.esprit.flo.entities;
    exports edu.esprit.flo.controllers;
    exports edu.esprit.flo.controllers.dechets;
    exports edu.esprit.flo.controllers.reservationDechets;
    exports edu.esprit.flo.controllers.dechets_back;
    exports edu.esprit.flo.controllers.reservationDechets_back;
}