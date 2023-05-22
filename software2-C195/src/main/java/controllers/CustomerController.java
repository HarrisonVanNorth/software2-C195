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
     * Lambda to fill observable list with First level Division name.
     *
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection connection = DBConnection.startConnection();

            ObservableList<CountryDao> countryDaoObservableList = CountryDao.getCountries();
            ObservableList<String> countryNamesObservableList = FXCollections.observableArrayList();
            ObservableList<FirstLevelDivisionDao> firstLevelDivisionDaoObservableList =
                    FirstLevelDivisionDao.getAllFirstLevelDivisions();
            ObservableList<String> firstLevelDivisionAllNames = FXCollections.observableArrayList();
            ObservableList<Customers> allCustomersList = CustomerDao.getAllCustomers(connection);

            customerRecordsTableID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            customerRecordsTableName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            customerRecordsTableAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
            customerRecordsTablePostalCode.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
            customerRecordsTablePhone.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
            customerRecordsTableState.setCellValueFactory(new PropertyValueFactory<>("divisionName"));

            countryDaoObservableList.stream().map(Country::getCountryName).forEach(countryNamesObservableList::add);
            customerCountry.setItems(countryNamesObservableList);

            // lambda
            firstLevelDivisionDaoObservableList.forEach(
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
    void navigateToAppointmentsMenu(ActionEvent event) throws IOException {
        Utils.navigationBase(event, "/application/appointments.fxml");
    }

    /**
     * Direct user to customer menu.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void navigateToCustomerMenu(ActionEvent event) throws IOException {
        Utils.navigationBase(event, "/application/customer.fxml");
    }

    /**
     * Direct user to reports menu.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void navigateToReportsMenu(ActionEvent event) throws IOException {
        Utils.navigationBase(event, "/application/reports.fxml");
    }

    /**
     * Exit the program.
     *
     * @param event
     */
    public void navigationExitProgram(ActionEvent event) {
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
    void deleteCustomerRecord(ActionEvent event) throws Exception {

        Connection connection = DBConnection.startConnection();
        ObservableList<Appointments> getAllAppointmentsList = AppointmentDao.getAllAppointments();
        try {
            Alert alert =
                    new Alert(Alert.AlertType.CONFIRMATION, "Delete the selected customer and all appointments?");
            Optional<ButtonType> confirmation = alert.showAndWait();
            if (confirmation.isPresent() && confirmation.get() == ButtonType.OK) {
                int selectedCustomerID = customerRecordsTable.getSelectionModel().getSelectedItem().getCustomerID();

                AppointmentDao.deleteAppointment(selectedCustomerID, connection);

                String sqlDeleteCustomerStatement = "DELETE FROM customers WHERE Customer_ID = ?";
                DBConnection.setPreparedStatement(DBConnection.getConnection(), sqlDeleteCustomerStatement);

                PreparedStatement preparedStatement = DBConnection.getPreparedStatement();
                int customerFromTable = customerRecordsTable.getSelectionModel().getSelectedItem().getCustomerID();

                //Delete all customer appointments from database.
                for (Appointments appointment : getAllAppointmentsList) {
                    int customerID = appointment.getCustomerID();
                    if (customerFromTable == customerID) {
                        String deleteAssociatedAppointments = "DELETE FROM appointments WHERE Appointment_ID = ?";
                        DBConnection.setPreparedStatement(DBConnection.getConnection(), deleteAssociatedAppointments);
                    }
                }
                preparedStatement.setInt(1, customerFromTable);
                preparedStatement.execute();
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
     * Fill in fields when customer is selected and Edit Customer button is clicked.
     *
     * @param event
     * @throws SQLException
     */

    // still don't like this one
    @FXML
    void editCustomerRecord(ActionEvent event) {
        try {
            DBConnection.startConnection();
            Customers selectedCustomer = customerRecordsTable.getSelectionModel().getSelectedItem();

            String divisionName = "", countryName = "";

            if (selectedCustomer != null) {
                ObservableList<CountryDao> countryDaoObservableList = CountryDao.getCountries();
                ObservableList<FirstLevelDivisionDao> firstLevelDivisionDaoObservableList =
                        FirstLevelDivisionDao.getAllFirstLevelDivisions();
                ObservableList<String> fistLevelDivisionList = FXCollections.observableArrayList();

                customerState.setItems(fistLevelDivisionList);

                customerID.setText(String.valueOf((selectedCustomer.getCustomerID())));
                customerName.setText(selectedCustomer.getCustomerName());
                customerAddress.setText(selectedCustomer.getCustomerAddress());
                customerPostalCode.setText(selectedCustomer.getCustomerPostalCode());
                customerPhoneNumber.setText(selectedCustomer.getCustomerPhone());

                for (firstLevelDivision flDivision : firstLevelDivisionDaoObservableList) {
                    fistLevelDivisionList.add(flDivision.getDivisionName());
                    int countryIDToSet = flDivision.getCountry_ID();

                    if (flDivision.getDivisionID() == selectedCustomer.getCustomerDivisionID()) {
                        divisionName = flDivision.getDivisionName();

                        for (Country country : countryDaoObservableList) {
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
     * Add customer record to database.
     *
     * @param event
     */
    @FXML
    void addCustomerRecord(ActionEvent event) {
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
            alert.setContentText("All fields must be filled before customer can be added.");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    /**
     * When Save button is clicked add new customer record.
     *
     * @param event
     */
    @FXML
    void saveCustomerRecord(ActionEvent event) throws SQLException {
        try {
            Connection connection = DBConnection.startConnection();
            if (!customerName.getText().isEmpty() || !customerAddress.getText().isEmpty() ||
                    !customerAddress.getText().isEmpty() || !customerPostalCode.getText().isEmpty() ||
                    !customerPhoneNumber.getText().isEmpty() || !customerCountry.getValue().isEmpty() ||
                    !customerState.getValue().isEmpty()) {

                int firstLevelDivisionName = 0;
                for (FirstLevelDivisionDao firstLevelDivision : FirstLevelDivisionDao.getAllFirstLevelDivisions()) {
                    if (customerState.getSelectionModel().getSelectedItem()
                            .equals(firstLevelDivision.getDivisionName())) {
                        firstLevelDivisionName = firstLevelDivision.getDivisionID();
                    }
                }

                String insertStatement =
                        "UPDATE customers SET Customer_ID = ?, Customer_Name = ?, Address = ?, Postal_Code = ?, " +
                                "Phone = ?, Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, " +
                                "Division_ID = ? WHERE Customer_ID = ?";
                DBConnection.setPreparedStatement(DBConnection.getConnection(), insertStatement);
                PreparedStatement preparedStatement = DBConnection.getPreparedStatement();
                preparedStatement.setInt(1, Integer.parseInt(customerID.getText()));
                preparedStatement.setString(2, customerName.getText());
                preparedStatement.setString(3, customerAddress.getText());
                preparedStatement.setString(4, customerPostalCode.getText());
                preparedStatement.setString(5, customerPhoneNumber.getText());
                preparedStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                preparedStatement.setString(7, "admin");
                preparedStatement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
                preparedStatement.setString(9, "admin");
                preparedStatement.setInt(10, firstLevelDivisionName);
                preparedStatement.setInt(11, Integer.parseInt(customerID.getText()));
                preparedStatement.execute();

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
            alert.setContentText("All fields must be filled before customer can be updated.");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    /**
     * Fill Drop Down menus with first-level division information.
     *
     * @param event
     * @throws SQLException
     */
    public void customerCountryDropDown(ActionEvent event) throws SQLException {
        try {
            DBConnection.startConnection();

            String selectedCountry = customerCountry.getSelectionModel().getSelectedItem();

            ObservableList<FirstLevelDivisionDao> getAllFirstLevelDivisions =
                    FirstLevelDivisionDao.getAllFirstLevelDivisions();

            ObservableList<String> firstLevelDivisionUS = FXCollections.observableArrayList();
            ObservableList<String> firstLevelDivisionUK = FXCollections.observableArrayList();
            ObservableList<String> firstLevelDivisionCanada = FXCollections.observableArrayList();

            getAllFirstLevelDivisions.forEach(firstLevelDivision -> {
                switch (firstLevelDivision.getCountry_ID()) {
                    case 1 -> firstLevelDivisionUS.add(firstLevelDivision.getDivisionName());
                    case 2 -> firstLevelDivisionUK.add(firstLevelDivision.getDivisionName());
                    case 3 -> firstLevelDivisionCanada.add(firstLevelDivision.getDivisionName());
                }
            });
            switch (selectedCountry) {
                case "U.S" -> customerState.setItems(firstLevelDivisionUS);
                case "UK" -> customerState.setItems(firstLevelDivisionUK);
                case "Canada" -> customerState.setItems(firstLevelDivisionCanada);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
