package models;

public class Reports {

    public String appointmentMonth;
    public int appointmentTotal;
    private final int countryCount;
    private final String countryName;

    /**
     * @param countryName
     * @param countryCount
     */
    public Reports(String countryName, int countryCount) {
        this.countryCount = countryCount;
        this.countryName = countryName;

    }

    /**
     * Returns country name for custom report.
     *
     * @return countryName
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Total for each country.
     *
     * @return countryCount
     */
    public int getCountryCount() {
        return countryCount;
    }

}
