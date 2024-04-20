package services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import entities.User;

import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class UserService implements  UserCrud<User>{
    Connection connection;
    public UserService() {
        connection = MyDatabase.getInstance().getConnection();



    }
        @Override
        public void add(User user) throws SQLException {
        String query = "INSERT INTO user (name, lastname, roles, email,password, image, number, is_verified, datenaissance) " +
            "VALUES (?, ?, ?, ?, ?,?, ?, ?,?)";

        int cost = 13; // Exemple de coût utilisé par Symfony

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getLastname());
            preparedStatement.setString(3, "[\"" + user.getRoles() + "\"]");
            preparedStatement.setString(4, user.getEmail());

            BCrypt.Hasher hasher = BCrypt.with(BCrypt.Version.VERSION_2Y);
            String saltedPassword = hasher.hashToString(cost, user.getPassword().toCharArray());
            preparedStatement.setString(5, saltedPassword);


            preparedStatement.setString(6, user.getImage());
            preparedStatement.setInt(7, user.getNumber());
            preparedStatement.setBoolean(8, user.getIs_verified());
            preparedStatement.setDate(9, new java.sql.Date(user.getDatenaissance().getTime()));

            preparedStatement.executeUpdate();
            System.out.println("User added successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


        @Override
    public void update(User user) {
        String query = "UPDATE user SET name=?, lastname=?, roles=?, email=?, image=?, number=?, is_verified=?,datenaissance=? WHERE id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getLastname());
            preparedStatement.setString(3, "[\"" + user.getRoles() + "\"]");
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getImage());
            preparedStatement.setInt(6, user.getNumber());
            preparedStatement.setBoolean(7, user.getIs_verified());
            preparedStatement.setDate(8, new java.sql.Date(user.getDatenaissance().getTime()));

            preparedStatement.setInt(9, user.getId());

            preparedStatement.executeUpdate();
            System.out.println("User updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    @Override
    public void delete(User user) {
        String requete = "DELETE FROM user WHERE name = ? AND lastname = ? AND email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(requete)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getLastname());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.executeUpdate();
            System.out.println("User deleted successfully.");
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @Override
    public List<User> show() {
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM user";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setLastname(resultSet.getString("lastname"));
                user.setRoles(resultSet.getString("roles"));
                user.setEmail(resultSet.getString("email"));
                user.setImage(resultSet.getString("image"));
                user.setNumber(resultSet.getInt("number"));
                user.setIs_verified(resultSet.getBoolean("is_verified"));

                userList.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return userList;
    }

    @Override
    public void signUp(User user) {
        if (!isEmailUsed(user.getEmail())) {
            user.setRoles("ROLE_CLIENT");

            try {
                add(user);
                System.out.println("User signed up successfully.");

            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Email is already used.");
        }}
    @Override
    public boolean login(String email, String password) {
        String query = "SELECT * FROM user WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String hashedPasswordFromDB = resultSet.getString("password");
                if (BCrypt.verifyer().verify(password.toCharArray(), hashedPasswordFromDB).verified) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

   /* public boolean login(String email, String password) {
        String query = "SELECT * FROM user WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // L'utilisateur avec l'email donné existe dans la base de données
                // Maintenant, vérifions si le mot de passe correspond
                String hashedPasswordFromDB = resultSet.getString("password");
                if (BCrypt.checkpw(password, hashedPasswordFromDB)) {
                    // Le mot de passe est correct
                    return true; // Connexion réussie
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }

        // L'utilisateur n'existe pas ou le mot de passe est incorrect
        return false; // Connexion échouée
    }
*/

    @Override
    public void updateEmail(int userId, String newEmail) {
        String query = "UPDATE user SET email=? WHERE id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newEmail);
            preparedStatement.setInt(2, userId);

            preparedStatement.executeUpdate();
            System.out.println("Email updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
 @Override
    public void updateName(User user) {
        String requete = "UPDATE user SET name=? WHERE id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(requete)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setInt(2, user.getId());
            preparedStatement.executeUpdate();
            System.out.println("Name updated successfully.");
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
@Override
    public void updateLastName(User user) {
        String requete = "UPDATE user SET lastname=? WHERE id=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(requete)) {
            preparedStatement.setString(1, user.getLastname());
            preparedStatement.setInt(2, user.getId());
            preparedStatement.executeUpdate();
            System.out.println("Last name updated successfully.");
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

@Override
public List<User> sortByEmail() {
    List<User> userList = new ArrayList<>();
    String query = "SELECT * FROM user ORDER BY email";

    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            User user = new User(resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("lastname" ),
                resultSet.getString("password"),
                resultSet.getString("email"),
                resultSet.getString("roles"),
                resultSet.getString("image"),
                resultSet.getInt("Number"),
                resultSet.getBoolean("is_verified"),
                resultSet.getDate("datenaissance")

            );
            userList.add(user);
        }
    } catch (SQLException ex) {
        Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
    }

    return userList;
}
@Override
    public List<User> sortByName() {
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM user ORDER BY name";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("lastname"),
                    resultSet.getString("password"),
                    resultSet.getString("email"),
                    resultSet.getString("roles"),
                    resultSet.getString("image"),
                    resultSet.getInt("Number"),
                    resultSet.getBoolean("is_verified"),
                    resultSet.getDate("datenaissance")
                );
                userList.add(user);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return userList;
    }

    @Override
    public List<User> sortById() {
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM user ORDER BY id";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("lastname"),
                    resultSet.getString("password"),
                    resultSet.getString("email"),
                    resultSet.getString("roles"),
                    resultSet.getString("image"),
                    resultSet.getInt("Number"),
                    resultSet.getBoolean("is_verified"),
                    resultSet.getDate("datenaissance")
                );
                userList.add(user);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return userList;
    }

@Override
    public List<User> searchByName(String name) {
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM user WHERE nom LIKE ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Utilisation du caractère Joker '%' pour rechercher des correspondances partielles du nom
            preparedStatement.setString(1, "%" + name + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User(
                    resultSet.getInt("id"),
                    resultSet.getString("nom"),
                    resultSet.getString("prenom"),
                    resultSet.getString("password"),
                    resultSet.getString("email"),
                    resultSet.getString("roles"),
                    resultSet.getString("image"),
                    resultSet.getInt("Number"),
                    resultSet.getBoolean("is_verified"),
                    resultSet.getDate("datenaissance")

                );
                userList.add(user);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return userList;
    }


    @Override
    public User searchById(int id) {
        User user = null;
        String query = "SELECT * FROM user WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("lastname"),
                    resultSet.getString("password"),
                    resultSet.getString("email"),
                    resultSet.getString("roles"),
                    resultSet.getString("image"),
                    resultSet.getInt("Number"),
                    resultSet.getBoolean("is_verified"),
                    resultSet.getDate("datenaissance")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return user;

    }
    @Override
    public List<User> searchByDateOfBirth(Date dateOfBirth) {
        List<User> userList = new ArrayList<>();
        String query = "SELECT * FROM user WHERE datenaissance = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1, new java.sql.Date(dateOfBirth.getTime()));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("lastname"),
                    resultSet.getString("password"),
                    resultSet.getString("email"),
                    resultSet.getString("roles"),
                    resultSet.getString("image"),
                    resultSet.getInt("Number"),
                    resultSet.getBoolean("is_verified"),
                    resultSet.getDate("datenaissance")
                );
                userList.add(user);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return userList;
    }

     @Override
     public boolean isEmailUsed(String email) {
         List<User> userList = getUsersFromDatabase();
         for (User user : userList) {
             if (user.getEmail().equals(email)) {
                 return true;
             }
         }
         return false;
     }
     private List<User> getUsersFromDatabase() {
        return new ArrayList<>();
    }

    @Override
    public User getUserByEmail(String email) {
        String query = "SELECT * FROM user WHERE email = ?";
        User user = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("lastname"),
                    resultSet.getString("password"),
                    resultSet.getString("email"),
                    resultSet.getString("roles"),
                    resultSet.getString("image"),
                    resultSet.getInt("Number"),
                    resultSet.getBoolean("is_verified"),
                    resultSet.getDate("datenaissance")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return user;
    }

}

