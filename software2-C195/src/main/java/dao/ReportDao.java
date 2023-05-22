package dao;

import application.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Appointments;
import models.Reports;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ReportDao extends Appointments {

    public ReportDao(int appointmentID, String appointmentTitle, String appointmentDescription,
                     String appointmentLocation, String appointmentType, LocalDateTime start, LocalDateTime end,
                     int customerID, int userID, int contactID) {
        super(appointmentID, appointmentTitle, appointmentDescription, appointmentLocation, appointmentTitle, start,
                end, customerID, userID, customerID);
    }

    /**
     * Gets all Countries and total appointment per country. Set to Observable-list
     *
     * @return countriesObservableList
     * @throws SQLException
     */
    public static ObservableList<Reports> getCountries() throws SQLException {
        ObservableList<Reports> countriesObservableList = FXCollections.observableArrayList();
        String sql =
                "select countries.Country, count(*) as countryCount from customers inner join first_level_divisions on customers.Division_ID = first_level_divisions.Division_ID inner join countries on countries.Country_ID = first_level_divisions.Country_ID where  customers.Division_ID = first_level_divisions.Division_ID group by first_level_divisions.Country_ID order by count(*) desc";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {

            String countryName = rs.getString("Country");
            int countryCount = rs.getInt("countryCount");
            Reports report = new Reports(countryName, countryCount);
            countriesObservableList.add(report);
        }
        return countriesObservableList;
    }
}
