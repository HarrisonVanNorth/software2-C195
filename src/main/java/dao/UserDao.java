package dao;

import application.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao extends Users {

    public UserDao(int userID, String userName, String userPassword) {
        super(userID,userName,userPassword);
    }

    /**
     * User login validation.
     *
     * @param password
     * @param username
     */
    public static int validateUser(String username, String password) {
        try {
            String sqlQuery =
                    "SELECT * FROM users WHERE user_name = '" + username + "' AND password = '" + password + "'";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sqlQuery);
            ResultSet rs = ps.executeQuery();
            rs.next();
            if (rs.getString("User_Name").equals(username)) {
                if (rs.getString("Password").equals(password)) {
                    return rs.getInt("User_ID");

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Get all user data and set to observable-list.
     *
     * @return usersObservableList
     * @throws SQLException
     */
    public static ObservableList<UserDao> getAllUsers() throws SQLException {
        ObservableList<UserDao> usersObservableList = FXCollections.observableArrayList();
        String sql = "SELECT * from users";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int userID = rs.getInt("User_ID");
            String userName = rs.getString("User_Name");
            String userPassword = rs.getString("Password");
            UserDao user = new UserDao(userID, userName, userPassword);
            usersObservableList.add(user);
        }
        return usersObservableList;
    }
}
