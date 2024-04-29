module edu.esprit.flo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens edu.esprit.flo to javafx.fxml;
    opens edu.esprit.flo.entities to javafx.fxml;
    opens edu.esprit.flo.controllers to javafx.fxml;
    opens edu.esprit.flo.controllers.produit to javafx.fxml;
    opens edu.esprit.flo.controllers.commande to javafx.fxml;
    opens edu.esprit.flo.controllers.produit_back to javafx.fxml;
    opens edu.esprit.flo.controllers.commande_back to javafx.fxml;

    exports edu.esprit.flo;
    exports edu.esprit.flo.entities;
    exports edu.esprit.flo.controllers;
    exports edu.esprit.flo.controllers.produit;
    exports edu.esprit.flo.controllers.commande;
    exports edu.esprit.flo.controllers.produit_back;
    exports edu.esprit.flo.controllers.commande_back;
}