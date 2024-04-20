package tests;

import utils.MyDatabase;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        MyDatabase dbCon = MyDatabase.getInstance();
        Connection connection = dbCon.getConnection();

        if (connection != null) {
            System.out.println("Connection successful!");
        } else {
            System.out.println("Failed to make connection!");
        }
    }
}