package controllers;

import application.DBConnection;
import application.Utils;
import dao.AppointmentDao;
import dao.CountryDao;
import dao.CustomerDao;
import dao.FirstLevelDivisionDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Appointments;
import models.Country;
import models.Customers;
import models.firstLevelDivision;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for adding, updateing,editing customer data.
 */

public class CustomerController implements Initializable {

    @FXML
    private Button customerRecordsAddCustomer;
    @FXML
    private Button appointmentMenuButton;
    @FXML
    private Button customerMenuButton;
    @FXML
    private Button reportsMenuButton;
    @FXML
    private Button exitMenuButton;
    @FXML
    private Button customerRecordsEdit;
    @FXML
    private TableColumn<?, ?> customerRecordsTableName;
    @FXML
    private TableView<Customers> customerRecordsTable;
    @FXML
    private TableColumn<?, ?> customerRecordsTableAddress;
    @FXML
    private TableColumn<?, ?> customerRecordsTableID;
    @FXML
    private TableColumn<?, ?> customerRecordsTablePhone;
    @FXML
    private TableColumn<?, ?> customerRecordsTablePostalCode;
    @FXML
    private TableColumn<?, ?> customerRecordsTableState;
    @FXML
    private TableColumn<?, ?> customerRecordsTableCountry;
    @FXML
    private TextField customerID;
    @FXML
    private TextField customerName;
    @FXML
    private TextField customerPhoneNumber;
    @FXML
    private TextField customerPostalCode;
    @FXML
    private ComboBox<String> customerState;
    @FXML
    private ComboBox<String> customerCountry;
    @FXML
    private TextField customerAddress;

    /**
     * Initialize customers view and populate table.
     * Lambda to fill observable list with getDivisionName().
     *
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection connection = DBConnection.startConnection();

            ObservableList<CountryDao> allCountries = CountryDao.getCountries();
            ObservableList<String> countryNames = FXCollections.observableArrayList();
            ObservableList<FirstLevelDivisionDao> allFirstLevelDivisions =
                    FirstLevelDivisionDao.getAllFirstLevelDivisions();
            ObservableList<String> firstLevelDivisionAllNames = FXCollections.observableArrayList();
            ObservableList<Customers> allCustomersList = CustomerDao.getAllCustomers(connection);

            customerRecordsTableID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            customerRecordsTableName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            customerRecordsTableAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
            customerRecordsTablePostalCode.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
            customerRecordsTablePhone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
            customerRecordsTableState.setCellValueFactory(new PropertyValueFactory<>("divisionName"));

            //IDE converted to forEach
            allCountries.stream().map(Country::getCountryName).forEach(countryNames::add);
            customerCountry.setItems(countryNames);

            // lambda #1
            allFirstLevelDivisions.forEach(
                    firstLevelDivision -> firstLevelDivisionAllNames.add(firstLevelDivision.getDivisionName()));

            customerState.setItems(firstLevelDivisionAllNames);
            customerRecordsTable.setItems(allCustomersList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Direct user appointments menu.
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
    public void navigationMenuExitClick(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


    /**
     * Delete customer records.
     *
     * @param event
     * @throws Exception
     */
    @FXML
    void customerRecordsDelete(ActionEvent event) throws Exception {

        Connection connection = DBConnection.startConnection();
        ObservableList<Appointments> getAllAppointmentsList = AppointmentDao.getAllAppointments();
        try {
            Alert alert =
                    new Alert(Alert.AlertType.CONFIRMATION, "Delete the selected customer and all appointments? ");
            Optional<ButtonType> confirmation = alert.showAndWait();
            if (confirmation.isPresent() && confirmation.get() == ButtonType.OK) {
                int deleteCustomerID = customerRecordsTable.getSelectionModel().getSelectedItem().getCustomerID();

                AppointmentDao.deleteAppointment(deleteCustomerID, connection);

                String sqlDelete = "DELETE FROM customers WHERE Customer_ID = ?";
                DBConnection.setPreparedStatement(DBConnection.getConnection(), sqlDelete);

                PreparedStatement psDelete = DBConnection.getPreparedStatement();
                int customerFromTable = customerRecordsTable.getSelectionModel().getSelectedItem().getCustomerID();

                //Delete all customer appointments from database.
                for (Appointments appointment : getAllAppointmentsList) {
                    int customerFromAppointments = appointment.getCustomerID();
                    if (customerFromTable == customerFromAppointments) {
                        String deleteStatementAppointments = "DELETE FROM appointments WHERE Appointment_ID = ?";
                        DBConnection.setPreparedStatement(DBConnection.getConnection(), deleteStatementAppointments);
                    }
                }
                psDelete.setInt(1, customerFromTable);
                psDelete.execute();
                ObservableList<Customers> refreshCustomersList = CustomerDao.getAllCustomers(connection);
                customerRecordsTable.setItems(refreshCustomersList);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Alert");
            alert.setContentText("Please select a customer to be deleted.");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    /**
     * Method that fills in boxes when customer is selected and edit button is clicked.
     *
     * @param event
     * @throws SQLException
     */

    // still don't like this one
    @FXML
    void customerRecordsEditCustomerButton(ActionEvent event) {
        try {
            DBConnection.startConnection();
            Customers selectedCustomer = customerRecordsTable.getSelectionModel().getSelectedItem();

            String divisionName = "", countryName = "";

            if (selectedCustomer != null) {
                ObservableList<CountryDao> getAllCountries = CountryDao.getCountries();
                ObservableList<FirstLevelDivisionDao> getFLDivisionNames =
                        FirstLevelDivisionDao.getAllFirstLevelDivisions();
                ObservableList<String> allFLDivision = FXCollections.observableArrayList();

                customerState.setItems(allFLDivision);

                customerID.setText(String.valueOf((selectedCustomer.getCustomerID())));
                customerName.setText(selectedCustomer.getCustomerName());
                customerAddress.setText(selectedCustomer.getCustomerAddress());
                customerPostalCode.setText(selectedCustomer.getCustomerPostalCode());
                customerPhoneNumber.setText(selectedCustomer.getCustomerPhone());

                for (firstLevelDivision flDivision : getFLDivisionNames) {
                    allFLDivision.add(flDivision.getDivisionName());
                    int countryIDToSet = flDivision.getCountry_ID();

                    if (flDivision.getDivisionID() == selectedCustomer.getCustomerDivisionID()) {
                        divisionName = flDivision.getDivisionName();

                        for (Country country : getAllCountries) {
                            if (country.getCountryID() == countryIDToSet) {
                                countryName = country.getCountryName();
                            }
                        }
                    }
                }
                customerState.setValue(divisionName);
                customerCountry.setValue(countryName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to add customer when button is clicked.
     * <p>
     * need to fix the customer id issue.
     *
     * @param event
     */
    @FXML
    void customerRecordsAddCustomer(ActionEvent event) {
        try {
            Connection connection = DBConnection.startConnection();

            if (!customerName.getText().isEmpty() || !customerAddress.getText().isEmpty() ||
                    !customerAddress.getText().isEmpty() || !customerPostalCode.getText().isEmpty() ||
                    !customerPhoneNumber.getText().isEmpty() || !customerCountry.getValue().isEmpty() ||
                    !customerState.getValue().isEmpty()) {

                //create random ID for new customer id
                Integer newCustomerID = (int) (Math.random() * 100);

                int firstLevelDivisionName = 0;
                for (FirstLevelDivisionDao firstLevelDivision : FirstLevelDivisionDao.getAllFirstLevelDivisions()) {
                    if (customerState.getSelectionModel().getSelectedItem()
                            .equals(firstLevelDivision.getDivisionName())) {
                        firstLevelDivisionName = firstLevelDivision.getDivisionID();
                    }
                }
                String insertStatement =
                        "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES (?,?,?,?,?,?,?,?,?,?)";
                DBConnection.setPreparedStatement(DBConnection.getConnection(), insertStatement);
                PreparedStatement ps = DBConnection.getPreparedStatement();
                ps.setInt(1, newCustomerID);
                ps.setString(2, customerName.getText());
                ps.setString(3, customerAddress.getText());
                ps.setString(4, customerPostalCode.getText());
                ps.setString(5, customerPhoneNumber.getText());
                ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(7, "admin");
                ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(9, "admin");
                ps.setInt(10, firstLevelDivisionName);
                ps.execute();

                customerID.clear();
                customerName.clear();
                customerAddress.clear();
                customerPostalCode.clear();
                customerPhoneNumber.clear();

                ObservableList<Customers> refreshCustomersList = CustomerDao.getAllCustomers(connection);
                customerRecordsTable.setItems(refreshCustomersList);

            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Alert");
            alert.setContentText("All fields must be filled!");
            alert.showAndWait();
            e.printStackTrace();
        }

    }

    /**
     * Method to add customer when the Save button is clicked.
     *
     * @param event
     */
    @FXML
    void customerRecordsSaveCustomer(ActionEvent event) throws SQLException {
        Connection connection = DBConnection.startConnection();
        if (!customerName.getText().isEmpty() || !customerAddress.getText().isEmpty() ||
                !customerAddress.getText().isEmpty() || !customerPostalCode.getText().isEmpty() ||
                !customerPhoneNumber.getText().isEmpty() || !customerCountry.getValue().isEmpty() ||
                !customerState.getValue().isEmpty()) {

            int firstLevelDivisionName = 0;
            for (FirstLevelDivisionDao firstLevelDivision : FirstLevelDivisionDao.getAllFirstLevelDivisions()) {
                if (customerState.getSelectionModel().getSelectedItem().equals(firstLevelDivision.getDivisionName())) {
                    firstLevelDivisionName = firstLevelDivision.getDivisionID();
                }
            }

            String insertStatement =
                    "UPDATE customers SET Customer_ID = ?, Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
            DBConnection.setPreparedStatement(DBConnection.getConnection(), insertStatement);
            PreparedStatement ps = DBConnection.getPreparedStatement();
            ps.setInt(1, Integer.parseInt(customerID.getText()));
            ps.setString(2, customerName.getText());
            ps.setString(3, customerAddress.getText());
            ps.setString(4, customerPostalCode.getText());
            ps.setString(5, customerPhoneNumber.getText());
            ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(7, "admin");
            ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(9, "admin");
            ps.setInt(10, firstLevelDivisionName);
            ps.setInt(11, Integer.parseInt(customerID.getText()));
            ps.execute();

            customerID.clear();
            customerName.clear();
            customerAddress.clear();
            customerPostalCode.clear();
            customerPhoneNumber.clear();

            ObservableList<Customers> refreshCustomersList = CustomerDao.getAllCustomers(connection);
            customerRecordsTable.setItems(refreshCustomersList);

        }
    }

    /**
     * Fill Drop Down menus with first-level division information.
     *
     * @param event
     * @throws SQLException
     */
    public void customerEditCountryDropDown(ActionEvent event) throws SQLException {
        try {
            DBConnection.startConnection();

            String selectedCountry = customerCountry.getSelectionModel().getSelectedItem();

            ObservableList<FirstLevelDivisionDao> getAllFirstLevelDivisions =
                    FirstLevelDivisionDao.getAllFirstLevelDivisions();

            ObservableList<String> flDivisionUS = FXCollections.observableArrayList();
            ObservableList<String> flDivisionUK = FXCollections.observableArrayList();
            ObservableList<String> flDivisionCanada = FXCollections.observableArrayList();

            getAllFirstLevelDivisions.forEach(firstLevelDivision -> {
                if (firstLevelDivision.getCountry_ID() == 1) {
                    flDivisionUS.add(firstLevelDivision.getDivisionName());
                } else if (firstLevelDivision.getCountry_ID() == 2) {
                    flDivisionUK.add(firstLevelDivision.getDivisionName());
                } else if (firstLevelDivision.getCountry_ID() == 3) {
                    flDivisionCanada.add(firstLevelDivision.getDivisionName());
                }
            });

            //needs a little revision
            if (selectedCountry.equals("U.S")) {
                customerState.setItems(flDivisionUS);
            } else if (selectedCountry.equals("UK")) {
                customerState.setItems(flDivisionUK);
            } else if (selectedCountry.equals("Canada")) {
                customerState.setItems(flDivisionCanada);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
