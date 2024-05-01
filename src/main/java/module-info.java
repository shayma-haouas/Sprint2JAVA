module edu.esprit.flo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires kernel;
    requires layout;
    requires stripe.java;

    opens edu.esprit.flo to javafx.fxml;
    opens edu.esprit.flo.entities to javafx.fxml;
    opens edu.esprit.flo.controllers to javafx.fxml;
    opens edu.esprit.flo.controllers.don to javafx.fxml;
    opens edu.esprit.flo.controllers.factureDon to javafx.fxml;
    opens edu.esprit.flo.controllers.don_back to javafx.fxml;
    opens edu.esprit.flo.controllers.factureDon_back to javafx.fxml;

    exports edu.esprit.flo;
    exports edu.esprit.flo.entities;
    exports edu.esprit.flo.controllers;
    exports edu.esprit.flo.controllers.don;
    exports edu.esprit.flo.controllers.factureDon;
    exports edu.esprit.flo.controllers.don_back;
    exports edu.esprit.flo.controllers.factureDon_back;
}