package dao;

import application.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Country;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryDao extends Country {

    public CountryDao(int countryID, String countryName) {
        super(countryID, countryName);
    }

    /**
     * Gets all Country_ID and Country and sets Observable-list.
     *
     * @return countriesObservableList
     * @throws SQLException
     */
    public static ObservableList<CountryDao> getCountries() throws SQLException {
        ObservableList<CountryDao> countriesObservableList = FXCollections.observableArrayList();
        String sql = "SELECT Country_ID, Country from countries";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int countryID = rs.getInt("Country_ID");
            String countryName = rs.getString("Country");
            CountryDao country = new CountryDao(countryID, countryName);
            countriesObservableList.add(country);
        }
        return countriesObservableList;
    }
}
