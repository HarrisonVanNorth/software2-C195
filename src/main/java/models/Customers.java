package models;

public class Customers {

    private final String divisionName;
    private final int customerID;
    private final String customerName;
    private final String customerAddress;
    private final String customerPostalCode;
    private final String customerPhoneNumber;
    private final int divisionID;

    public Customers(int customerID, String customerName, String customerAddress, String customerPostalCode,
                     String customerPhoneNumber, int divisionID, String divisionName) {

        this.customerID = customerID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostalCode = customerPostalCode;
        this.customerPhoneNumber = customerPhoneNumber;
        this.divisionID = divisionID;
        this.divisionName = divisionName;
    }


    /**
     * @return customerID
     */
    public Integer getCustomerID() {
        return customerID;
    }

    /**
     * @return customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @return customerAddress
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     * @return customerPostalCode
     */
    public String getCustomerPostalCode() {
        return customerPostalCode;
    }

    /**
     * @return customerPhoneNumber
     */
    public String getCustomerPhone() {
        return customerPhoneNumber;
    }

    /**
     * @return divisionID
     */
    public Integer getCustomerDivisionID() {
        return divisionID;
    }

    /**
     * @return divisionID
     */
    public String getDivisionName() {
        return divisionName;
    }

}
