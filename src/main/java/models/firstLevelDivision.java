package models;

public class firstLevelDivision {
    public int country_ID;
    private final int divisionID;
    private final String divisionName;

    /**
     * @param divisionID
     * @param country_ID
     * @param divisionName
     */
    public firstLevelDivision(int divisionID, String divisionName, int country_ID) {
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.country_ID = country_ID;
    }

    /**
     * @return divisionID
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * @return divisionName
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * @return country_ID
     */
    public int getCountry_ID() {
        return country_ID;
    }

}
