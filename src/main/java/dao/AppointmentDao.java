package dao;

import application.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Appointments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static application.Utils.convertTimeDateLocal;


public class AppointmentDao {

    /**
     * Get all appointments and set Observable-list.
     *
     * @return appointmentsObservableList
     * @throws SQLException
     */
    public static ObservableList<Appointments> getAllAppointments() throws SQLException {
        ObservableList<Appointments> appointmentsObservableList = FXCollections.observableArrayList();
        String sql = "SELECT * from appointments";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String appointmentTitle = rs.getString("Title");
            String appointmentDescription = rs.getString("Description");
            String appointmentLocation = rs.getString("Location");
            String appointmentType = rs.getString("Type");
            LocalDateTime start = convertTimeDateLocal(rs.getString("Start"));
            LocalDateTime end = convertTimeDateLocal(rs.getString("End"));
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");
            Appointments appointment =
                    new Appointments(appointmentID, appointmentTitle, appointmentDescription, appointmentLocation,
                            appointmentType, start, end, customerID, userID, contactID);
            appointmentsObservableList.add(appointment);
        }
        return appointmentsObservableList;
    }

    /**
     * Delete appointment given appointment ID.
     *
     * @param customer
     * @param connection
     * @return result
     * @throws SQLException
     */
    public static int deleteAppointment(int customer, Connection connection) throws SQLException {
        String query = "DELETE FROM appointments WHERE Appointment_ID=?";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, customer);
        int result = ps.executeUpdate();
        ps.close();
        return result;
    }
}
