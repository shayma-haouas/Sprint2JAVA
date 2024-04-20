package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {

    final String URL = "jdbc:mysql://localhost:3306/flo_dbtfin";

    final String USERNAME = "root";
    final String PASSWORD = "";
    Connection connection;

    static MyDatabase instance;

    private MyDatabase() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connexion établie");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    // Static method to get the singleton instance
    public static MyDatabase getInstance() {
        if (instance == null) {
            instance = new MyDatabase();
        }
        return instance;
    }

    // Method to get the database connection
    public Connection getConnection() {
        return connection;
    }
}
