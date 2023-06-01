package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnection {
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String port = "//localhost/";
    private static final String dataBase = "client_schedule";
    private static final String jdbcURL = protocol + vendorName + port + dataBase + "?connectionTimeZone=LOCAL";
    private static final String MYSQLJDBCDriver = "com.mysql.cj.jdbc.Driver";
    private static final String username = "sqlUser";
    private static final String password = "Passw0rd!";
    public static Connection connection;
    private static PreparedStatement preparedStatement;

//    local DB
//  private static final String username = "root";
//  private static final String password = "sqlUser!";

    /**
     * Start the databsae connection.
     *
     * @return conn
     */
    public static Connection startConnection() {
        try {
            Class.forName(MYSQLJDBCDriver);
            connection = DriverManager.getConnection(jdbcURL, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Method to get the current connection.
     *
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


