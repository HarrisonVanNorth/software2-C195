package dao;

import application.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.firstLevelDivision;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FirstLevelDivisionDao extends firstLevelDivision {

    public FirstLevelDivisionDao(int divisionID, String divisionName, int country_ID) {
        super(divisionID, divisionName, country_ID);
    }

    /**
     * Gets all first_level_divisions data and sets it to an Observable-list.
     *
     * @return firstLevelDivisionsObservableList
     * @throws SQLException
     */
    public static ObservableList<FirstLevelDivisionDao> getAllFirstLevelDivisions() throws SQLException {
        ObservableList<FirstLevelDivisionDao> firstLevelDivisionsObservableList = FXCollections.observableArrayList();
        String sql = "SELECT * from first_level_divisions";
        PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int divisionID = rs.getInt("Division_ID");
            String divisionName = rs.getString("Division");
            int country_ID = rs.getInt("COUNTRY_ID");
            FirstLevelDivisionDao firstLevelDivision = new FirstLevelDivisionDao(divisionID, divisionName, country_ID);
            firstLevelDivisionsObservableList.add(firstLevelDivision);
        }
        return firstLevelDivisionsObservableList;
    }
}
