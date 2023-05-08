package controllers;

import application.Utils;
import models.*;
import dao.AppointmentDao;
import dao.ContactDao;
import dao.ReportDao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Month;
import java.util.Collections;

/**
 * Class and methods to display 3 reports.
 */

public class ReportController {

    @FXML private TableView<Appointments> allAppointmentsTable;
    @FXML private TableColumn<?, ?> appointmentContact;
    @FXML private TableColumn<?, ?> appointmentCustomerID;
    @FXML private TableColumn<?, ?> appointmentDescription;
    @FXML private TableColumn<?, ?> appointmentEnd;
    @FXML private TableColumn<?, ?> appointmentID;
    @FXML private TableColumn<?, ?> appointmentLocation;
    @FXML private TableColumn<?, ?> appointmentStart;
    @FXML private TableColumn<?, ?> appointmentTitle;
    @FXML private TableColumn<?, ?> appointmentTotalsAppointmentTypeCol;
    @FXML private TableColumn<?, ?> appointmentTotalsByMonth;
    @FXML private TableColumn<?, ?> appointmentTotalsMonthTotal;
    @FXML private TableColumn<?, ?> appointmentTotalsTypeTotalCol;
    @FXML private TableColumn<?, ?> appointmentType;
    @FXML private Button backToMainMenu;
    @FXML private ComboBox<String> contactScheduleContactBox;
    @FXML private TableColumn<?, ?> tableContactID;
    @FXML private TableView<ReportType> appointmentTotalsAppointmentType;
    @FXML private Tab appointmentTotalsTab;
    @FXML private TableView<ReportMonth> appointmentTotalAppointmentByMonth;
    @FXML private TableView<Reports> customerByCountry;
    @FXML private TableColumn<?, ?> countryName;
    @FXML private TableColumn<?, ?> countryCounter;
    @FXML private Button appointmentMenuButton;
    @FXML private Button customerMenuButton;
    @FXML private Button reportsMenuButton;
    @FXML private Button exitMenuButton;
    /**
     * Initialize and setup fields on the form.
     * @throws SQLException
     */
    public void initialize() throws SQLException {

        countryName.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        countryCounter.setCellValueFactory(new PropertyValueFactory<>("countryCount"));
        appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        appointmentDescription.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        tableContactID.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        appointmentTotalsAppointmentTypeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentTotalsTypeTotalCol.setCellValueFactory(new PropertyValueFactory<>("appointmentTotal"));
        appointmentTotalsByMonth.setCellValueFactory(new PropertyValueFactory<>("appointmentMonth"));
        appointmentTotalsMonthTotal.setCellValueFactory(new PropertyValueFactory<>("appointmentTotal"));

        ObservableList<Contacts> contactsObservableList = ContactDao.getAllContacts();
        ObservableList<String> allContactsNames = FXCollections.observableArrayList();
        contactsObservableList.forEach(contacts -> allContactsNames.add(contacts.getContactName()));
        contactScheduleContactBox.setItems(allContactsNames);

    }
    /**
     * Direct user to appointments menu.
     * @param event
     * @throws IOException
     */
    @FXML
    void navigationMenuAppointmentClick(ActionEvent event) throws IOException {
        Utils.navigationBase(event, "/application/appointments.fxml");
    }

    /**
     * Direct user to customers menu.
     * @param event
     * @throws IOException
     */
    @FXML
    void navigationMenuCustomerClick(ActionEvent event) throws IOException {
        Utils.navigationBase(event, "/application/customer.fxml");
    }

    /**
     * Direct user to reports menu.
     * @param event
     * @throws IOException
     */
    @FXML
    void navigationMenuReportsClick(ActionEvent event) throws IOException {
        Utils.navigationBase(event, "/application/reports.fxml");
    }

    /**
     * Exit the program.
     * @param ExitButton
     */
    public void navigationMenuExitClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Fill fxml form with appointment data by contact.
     */
    @FXML
    public void appointmentDataByContact() {
        try {

            int contactID = 0;

            ObservableList<Appointments> getAllAppointmentData = AppointmentDao.getAllAppointments();
            ObservableList<Appointments> appointmentInfo = FXCollections.observableArrayList();
            ObservableList<Contacts> getAllContacts = ContactDao.getAllContacts();

            Appointments contactAppointmentInfo;

            String contactName = contactScheduleContactBox.getSelectionModel().getSelectedItem();

            for (Contacts contact: getAllContacts) {
                if (contactName.equals(contact.getContactName())) {
                    contactID = contact.getId();
                }
            }

            for (Appointments appointment: getAllAppointmentData) {
                if (appointment.getContactID() == contactID) {
                    contactAppointmentInfo = appointment;
                    appointmentInfo.add(contactAppointmentInfo);
                }
            }
            allAppointmentsTable.setItems(appointmentInfo);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Total number of customer appointments by type and month report.
     * @throws SQLException
     */
    public void appointmentTotalsTab() throws SQLException {
        try {
            ObservableList<Appointments> getAllAppointments = AppointmentDao.getAllAppointments();
            ObservableList<Month> appointmentMonths = FXCollections.observableArrayList();
            ObservableList<Month> monthOfAppointments = FXCollections.observableArrayList();

            ObservableList<String> appointmentType = FXCollections.observableArrayList();
            ObservableList<String> uniqueAppointment = FXCollections.observableArrayList();

            ObservableList<ReportType> reportType = FXCollections.observableArrayList();
            ObservableList<ReportMonth> reportMonths = FXCollections.observableArrayList();


            //IDE converted to Lambda
            getAllAppointments.forEach(appointments -> {
                appointmentType.add(appointments.getAppointmentType());
            });

            //IDE converted to Lambda
            getAllAppointments.stream().map(appointment -> {
                return appointment.getStart().getMonth();
            }).forEach(appointmentMonths::add);

            //IDE converted to Lambda
            appointmentMonths.stream().filter(month -> {
                return !monthOfAppointments.contains(month);
            }).forEach(monthOfAppointments::add);

            for (Appointments appointments: getAllAppointments) {
                String appointmentsAppointmentType = appointments.getAppointmentType();
                if (!uniqueAppointment.contains(appointmentsAppointmentType)) {
                    uniqueAppointment.add(appointmentsAppointmentType);
                }
            }

            for (Month month: monthOfAppointments) {
                int totalMonth = Collections.frequency(appointmentMonths, month);
                String monthName = month.name();
                ReportMonth appointmentMonth = new ReportMonth(monthName, totalMonth);
                reportMonths.add(appointmentMonth);
            }
            appointmentTotalAppointmentByMonth.setItems(reportMonths);

            for (String type: uniqueAppointment) {
                String typeToSet = type;
                int typeTotal = Collections.frequency(appointmentType, type);
                ReportType appointmentTypes = new ReportType(typeToSet, typeTotal);
                reportType.add(appointmentTypes);
            }
            appointmentTotalsAppointmentType.setItems(reportType);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Custom report to display number of appointments in each Country.
     * @throws SQLException
     */
    public void customerByCountry() throws SQLException {
        try {

            ObservableList<Reports> aggregatedCountries = ReportDao.getCountries();
            ObservableList<Reports> countriesToAdd = FXCollections.observableArrayList();

            //IDE converted
            aggregatedCountries.forEach(countriesToAdd::add);

            customerByCountry.setItems(countriesToAdd);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
