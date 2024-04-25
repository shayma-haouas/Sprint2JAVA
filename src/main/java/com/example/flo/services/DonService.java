package com.example.flo.services;

import com.example.flo.entities.Don;
import com.example.flo.entities.User;
import com.example.flo.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonService {

    private static DonService instance;
    PreparedStatement preparedStatement;
    Connection connection;

    public DonService() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public static DonService getInstance() {
        if (instance == null) {
            instance = new DonService();
        }
        return instance;
    }

    public List<Don> getAll() {
        List<Don> listDon = new ArrayList<>();
        try {

            String query = "SELECT * FROM `don` AS x "
                    + "RIGHT JOIN `user` AS y1 ON x.user_id = y1.id "
                    + "WHERE  x.user_id = y1.id  " ;
            preparedStatement = connection.prepareStatement(query);


            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Don don = new Don();
                don.setId(resultSet.getInt("id"));
                don.setType(resultSet.getString("type"));
                don.setDescription(resultSet.getString("description"));
                don.setDateDon(resultSet.getDate("date_don") != null ? resultSet.getDate("date_don").toLocalDate() : null);

                User user = new User();
                user.setId(resultSet.getInt("y1.id"));
                user.setName(resultSet.getString("y1.name"));
                don.set(user);

                listDon.add(don);
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) don : " + exception.getMessage());
        }
        return listDon;
    }

    public List<User> getAllUsers() {
        List<User> lists = new ArrayList<>();
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM `user`");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                lists.add(user);
            }
        } catch (SQLException exception) {
            System.out.println("Error (getAll) users : " + exception.getMessage());
        }
        return lists;
    }


    public boolean add(Don don) {


        String request = "INSERT INTO `don`(`type`, `description`, `date_don`, `user_id`) VALUES(?, ?, ?, ?)" ;

        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, don.getType());
            preparedStatement.setString(2, don.getDescription());
            preparedStatement.setDate(3, Date.valueOf(don.getDateDon()));

            preparedStatement.setInt(4, don.getUser().getId());


            preparedStatement.executeUpdate();
            System.out.println("Don added");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (add) don : " + exception.getMessage());
        }
        return false;
    }

    public boolean edit(Don don) {

        String request = "UPDATE `don` SET `type` = ?, `description` = ?, `date_don` = ?, `user_id` = ? WHERE `id` = ?" ;

        try {
            preparedStatement = connection.prepareStatement(request);

            preparedStatement.setString(1, don.getType());
            preparedStatement.setString(2, don.getDescription());
            preparedStatement.setDate(3, Date.valueOf(don.getDateDon()));

            preparedStatement.setInt(4, don.getUser().getId());

            preparedStatement.setInt(5, don.getId());

            preparedStatement.executeUpdate();
            System.out.println("Don edited");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (edit) don : " + exception.getMessage());
        }
        return false;
    }

    public boolean delete(int id) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM `don` WHERE `id`=?");
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("Don deleted");
            return true;
        } catch (SQLException exception) {
            System.out.println("Error (delete) don : " + exception.getMessage());
        }
        return false;
    }
}
