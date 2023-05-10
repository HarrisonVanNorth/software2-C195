package controllers;

import application.Utils;
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
import models.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Month;
import java.util.Collections;

/**
 * Reports controller displays three table view tabs.
 */

public class ReportController {
    @FXML
    private TableView<Appointments> tabOneAllAppointmentsTableView;
    @FXML
    private TableView<ReportType> tabTwoAppointmentTypeTableView;
    @FXML
    private TableView<ReportMonth> tabTwoAppointmentByMonthTableView;
    @FXML
    private TableView<Reports> tabThreeCustomerByCountryTableView;
    @FXML
    private TableColumn<?, ?> tabOneAppointmentID;
    @FXML
    private TableColumn<?, ?> tabOneAppointmentTitle;
    @FXML
    private TableColumn<?, ?> tabOneAppointmentDescription;
    @FXML
    private TableColumn<?, ?> tabOneAppointmentLocation;
    @FXML
    private TableColumn<?, ?> tabOneAppointmentType;
    @FXML
    private TableColumn<?, ?> tabOneAppointmentStartTime;
    @FXML
    private TableColumn<?, ?> tabOneAppointmentEndTime;
    @FXML
    private TableColumn<?, ?> tabOneCustomerId;
    @FXML
    private TableColumn<?, ?> tabOneContactId;
    @FXML
    private TableColumn<?, ?> tabTwoAppointmentType;
    @FXML
    private TableColumn<?, ?> tabTwoAppointmentTypeTotal;
    @FXML
    private TableColumn<?, ?> tabTwoAppointmentByMonth;
    @FXML
    private TableColumn<?, ?> tabTwoAppointmentMonthTotals;
    @FXML
    private TableColumn<?, ?> tabThreeCountryName;
    @FXML
    private TableColumn<?, ?> tabThreeCountryTotal;
    @FXML
    private ComboBox<String> contactScheduleContactBox;
    @FXML
    private Tab appointmentTotalsTab;
    @FXML
    private Tab customerByCountryTab;
    @FXML
    private Button appointmentMenuButton;
    @FXML
    private Button customerMenuButton;
    @FXML
    private Button reportsMenuButton;
    @FXML
    private Button exitMenuButton;

    /**
     * Initialize and fill data on tabs.
     *
     * @throws SQLException
     */
    public void initialize() throws SQLException {


        //Contact Schedule
        tabOneAppointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        tabOneAppointmentTitle.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        tabOneAppointmentDescription.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        tabOneAppointmentLocation.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        tabOneAppointmentType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        tabOneAppointmentStartTime.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        tabOneAppointmentEndTime.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        tabOneCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        tabOneContactId.setCellValueFactory(new PropertyValueFactory<>("contactID"));

        //Appointments Total
        tabTwoAppointmentType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        tabTwoAppointmentTypeTotal.setCellValueFactory(new PropertyValueFactory<>("appointmentTotal"));
        tabTwoAppointmentByMonth.setCellValueFactory(new PropertyValueFactory<>("appointmentMonth"));
        tabTwoAppointmentMonthTotals.setCellValueFactory(new PropertyValueFactory<>("appointmentTotal"));

        //Customer by Country
        tabThreeCountryName.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        tabThreeCountryTotal.setCellValueFactory(new PropertyValueFactory<>("countryCount"));

        ObservableList<Contacts> contactsObservableList = ContactDao.getAllContacts();
        ObservableList<String> allContactsNames = FXCollections.observableArrayList();
        contactsObservableList.forEach(contacts -> allContactsNames.add(contacts.getContactName()));
        contactScheduleContactBox.setItems(allContactsNames);

    }

    /**
     * Direct user to appointments menu.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void navigationMenuAppointmentClick(ActionEvent event) throws IOException {
        Utils.navigationBase(event, "/application/appointments.fxml");
    }

    /**
     * Direct user to customers menu.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void navigationMenuCustomerClick(ActionEvent event) throws IOException {
        Utils.navigationBase(event, "/application/customer.fxml");
    }

    /**
     * Direct user to reports menu.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void navigationMenuReportsClick(ActionEvent event) throws IOException {
        Utils.navigationBase(event, "/application/reports.fxml");
    }

    /**
     * Exit the program.
     *
     * @param event
     */
    @FXML
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

            ObservableList<Appointments> appointmentsObservableList = AppointmentDao.getAllAppointments();
            ObservableList<Appointments> appointmentInfo = FXCollections.observableArrayList();
            ObservableList<Contacts> contactsObservableList = ContactDao.getAllContacts();

            String contactName = contactScheduleContactBox.getSelectionModel().getSelectedItem();

            for (Contacts contact : contactsObservableList) {
                if (contactName.equals(contact.getContactName())) {
                    contactID = contact.getId();
                }
            }

            for (Appointments appointment : appointmentsObservableList) {
                if (appointment.getContactID() == contactID) {
                    appointmentInfo.add(appointment);
                }
            }
            tabOneAllAppointmentsTableView.setItems(appointmentInfo);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Total number of customer appointments by type and month report.
     *
     * @throws SQLException
     */
    public void appointmentTotalsTab() throws SQLException {
        try {
            ObservableList<Appointments> appointmentsObservableList = AppointmentDao.getAllAppointments();
            ObservableList<Month> appointmentMonthsList = FXCollections.observableArrayList();
            ObservableList<Month> appointmentsInThatMonth = FXCollections.observableArrayList();

            ObservableList<String> appointmentType = FXCollections.observableArrayList();
            ObservableList<String> uniqueAppointment = FXCollections.observableArrayList();

            ObservableList<ReportType> reportType = FXCollections.observableArrayList();
            ObservableList<ReportMonth> reportMonths = FXCollections.observableArrayList();


            //Lambda
            appointmentsObservableList.forEach(appointments -> {
                appointmentType.add(appointments.getAppointmentType());
            });

            //Lambda map
            appointmentsObservableList.stream().map(appointment -> appointment.getStartTime().getMonth())
                    .forEach(appointmentMonthsList::add);

            //Lambda
            appointmentMonthsList.stream().filter(month -> !appointmentsInThatMonth.contains(month))
                    .forEach(appointmentsInThatMonth::add);

            for (Appointments appointments : appointmentsObservableList) {
                String appointmentsAppointmentType = appointments.getAppointmentType();
                if (!uniqueAppointment.contains(appointmentsAppointmentType)) {
                    uniqueAppointment.add(appointmentsAppointmentType);
                }
            }

            for (Month month : appointmentsInThatMonth) {
                int totalMonth = Collections.frequency(appointmentMonthsList, month);
                ReportMonth appointmentMonth = new ReportMonth(month.name(), totalMonth);
                reportMonths.add(appointmentMonth);
            }
            tabTwoAppointmentByMonthTableView.setItems(reportMonths);

            for (String type : uniqueAppointment) {
                int typeTotal = Collections.frequency(appointmentType, type);
                ReportType appointmentTypes = new ReportType(type, typeTotal);
                reportType.add(appointmentTypes);
            }
            tabTwoAppointmentTypeTableView.setItems(reportType);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Custom report to display number of appointments in each Country.
     *
     * @throws SQLException
     */
    public void customerByCountry() throws SQLException {
        try {

            ObservableList<Reports> aggregatedCountries = ReportDao.getCountries();
            ObservableList<Reports> countriesToAdd = FXCollections.observableArrayList();

            //IDE converted
            aggregatedCountries.forEach(countriesToAdd::add);

            tabThreeCustomerByCountryTableView.setItems(countriesToAdd);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
