package application;

import javafx.scene.paint.Stop;

import java.sql.*;

public class DBConnection {
    private static PreparedStatement preparedStatement;
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String port = "//localhost:3306/";
    private static final String dataBase = "client_schedule";
    private static final String jdbcURL = protocol + vendorName + port + dataBase +"?connectionTimeZone=SERVER";
    private static final String MYSQLJDBCDriver = "com.mysql.cj.jdbc.Driver";
    public static Connection connection = null;
    private static final String username = "sqlUser";
    private static final String password = "passw0rd!";

    /**
     * Start the databsae connection.
     * @return conn
     */
    public static Connection startConnection() {
        try {
            Class.forName(MYSQLJDBCDriver);
            connection = DriverManager.getConnection(jdbcURL, username, password);
            Statement stmt = connection.createStatement();
            ResultSet myRs = stmt.executeQuery("select * from users");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Method to get the current connection.
     * @return current connection.
     */
    public static Connection getConnection() {

        return connection;
    }

    /**
     * Close the connection.
     */
    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setPreparedStatement(Connection connection, String sqlStatement) throws SQLException {

        preparedStatement = connection.prepareStatement(sqlStatement);
    }

    public static PreparedStatement getPreparedStatement() {

        return preparedStatement;
    }

}


