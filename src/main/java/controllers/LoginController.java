package controllers;

import application.Utils;
import dao.AppointmentDao;
import dao.UserDao;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Appointments;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Login Controller
 */

public class LoginController implements Initializable {
    private final static ResourceBundle
            RESOURCE_BUNDLE = ResourceBundle.getBundle("MessageBundle", Locale.getDefault());
    @FXML
    private Button closeButton;
    @FXML
    private Button loginButton;
    @FXML
    private TextField loginLocationField;
    @FXML
    private TextField loginPassword;
    @FXML
    private TextField loginUsername;
    @FXML
    private Label passwordField;
    @FXML
    private Label usernameField;
    @FXML
    private Label loginField;
    @FXML
    private Label locationField;

    /**
     * Login button
     *
     * @throws SQLException
     * @throws IOException
     * @throws Exception
     **/

    @FXML
    private void loginButton(ActionEvent event) throws SQLException, IOException, Exception {
        try {
            int userId = UserDao.validateUser(loginUsername.getText(), loginPassword.getText());

            FileWriter fileWriter = new FileWriter("login_activity.txt", true);
            PrintWriter outputFile = new PrintWriter(fileWriter);

            if (userId > 0) {
                Utils.navigationBase(event, "/application/reports.fxml");

                outputFile.print("user: " + loginUsername.getText() + " successfully logged in at: " +
                        Timestamp.valueOf(LocalDateTime.now()) + "\n");

                appointmentCalculator();
            } else if (userId < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(RESOURCE_BUNDLE.getString("Error"));
                alert.setContentText(RESOURCE_BUNDLE.getString("Incorrect"));
                alert.show();

                //log the failed login attempt
                outputFile.print("user: " + loginUsername.getText() + " failed login attempt at: " +
                        Timestamp.valueOf(LocalDateTime.now()) + "\n");
            }
            outputFile.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Exits the application.
     *
     * @param event
     */

    public void closeButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Initializes the Login.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Locale locale = Locale.getDefault();
            Locale.setDefault(locale);
            ZoneId zone = ZoneId.systemDefault();
            loginLocationField.setText(String.valueOf(zone));

            loginField.setText(RESOURCE_BUNDLE.getString("Login"));
            usernameField.setText(RESOURCE_BUNDLE.getString("usernamePrompt"));
            passwordField.setText(RESOURCE_BUNDLE.getString("passwordPrompt"));
            loginButton.setText(RESOURCE_BUNDLE.getString("Login"));
            closeButton.setText(RESOURCE_BUNDLE.getString("Exit"));
            locationField.setText(RESOURCE_BUNDLE.getString("Location"));
        } catch (MissingResourceException e) {
            e.printStackTrace();
            System.out.println("Resource file missing: " + e);
        }
    }

    /**
     * Calculates if user has appointment within 15 minutes.
     *
     * @throws SQLException
     */
    private void appointmentCalculator() throws SQLException {
        ObservableList<Appointments> getAllAppointments = AppointmentDao.getAllAppointments();
        LocalDateTime localDateTimeMinus15Min = LocalDateTime.now().minusMinutes(15);
        LocalDateTime localDateTimePlus15Min = LocalDateTime.now().plusMinutes(15);
        LocalDateTime startTime;
        int getAppointmentID = 0;
        LocalDateTime displayTime = null;
        boolean appointmentWithin15Min = false;

        for (Appointments appointment : getAllAppointments) {
            startTime = appointment.getStartTime();
            if ((startTime.isAfter(localDateTimeMinus15Min) ||
                    startTime.isEqual(localDateTimeMinus15Min) &&
                            (startTime.isBefore(localDateTimePlus15Min) ||
                                    (startTime.isEqual(localDateTimePlus15Min))))) {
                getAppointmentID = appointment.getAppointmentID();
                displayTime = startTime;
                appointmentWithin15Min = true;
            }
        }

        Alert alert;
        if (appointmentWithin15Min) {
            alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Appointment " + getAppointmentID + " starts within 15 minutes. Start time: " +
                            displayTime);
        } else {
            alert = new Alert(Alert.AlertType.CONFIRMATION, "You have no upcoming appointments.");
        }
        alert.showAndWait();
    }
}



