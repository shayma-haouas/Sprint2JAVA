package services;

import entities.User;
import javafx.scene.Node;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface UserCrud<T> {

    public void add(T user) throws SQLException;

    public void update (T user);

    public void delete(T user);
    public List<User> show();

    void signUp(User user);

    boolean login(String email, String password);

    void updateEmail(int userId, String newEmail);

    void updateName(User user);

    void updateLastName(User user);

    List<User> sortByEmail();

    List<User> sortByName();

    List<User> sortById();

    List<User> searchByName(String name);

    User searchById(int id);


    List<User> searchByDateOfBirth(Date dateOfBirth);

    boolean isEmailUsed(String email);

    User getUserByEmail(String email);





}
