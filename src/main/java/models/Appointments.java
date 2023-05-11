package models;

import java.time.LocalDateTime;

public class Appointments {
    private final int appointmentID;
    private final String appointmentTitle;
    private final String appointmentDescription;
    private final String appointmentLocation;
    private final String appointmentType;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    public int customerID;
    public int userID;
    public int contactID;

    public Appointments(int appointmentID, String appointmentTitle, String appointmentDescription,
                        String appointmentLocation, String appointmentType, LocalDateTime startTime,
                        LocalDateTime endTime,
                        int customerID,
                        int userID, int contactID) {
        this.appointmentID = appointmentID;
        this.appointmentTitle = appointmentTitle;
        this.appointmentDescription = appointmentDescription;
        this.appointmentLocation = appointmentLocation;
        this.appointmentType = appointmentType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }

    /**
     * @return appointmentID
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * @return appointmentTitle
     */
    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    /**
     * @return appointmentDescription
     */
    public String getAppointmentDescription() {
        return appointmentDescription;
    }

    /**
     * @return appointmentLocation
     */
    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    /**
     * @return appointmentType
     */
    public String getAppointmentType() {
        return appointmentType;
    }

    /**
     * @return start
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * @return end
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * @return customerID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * @return userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @return contactID
     */
    public int getContactID() {
        return contactID;
    }

}

