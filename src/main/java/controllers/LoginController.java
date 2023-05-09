package controllers;

import dao.AppointmentDao;
import dao.UserDao;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Login Controller
 */

public class LoginController implements Initializable {


    /**
     * Current Locale for internationalization.
     */
    private final static Locale CURRENT_LOCALE = Locale.getDefault();
    /**
     * Resource Bundle for internationalization.
     */
    private final static ResourceBundle MESSAGES = ResourceBundle.getBundle("language/login", CURRENT_LOCALE);
    Stage stage;
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
    private void loginButton() throws SQLException, IOException, Exception {
        try {
            int userId = UserDao.validateUser(loginUsername.getText(), loginPassword.getText());

            FileWriter fileWriter = new FileWriter("login_activity.txt", true);
            PrintWriter outputFile = new PrintWriter(fileWriter);

            if (userId > 0) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/application/reports.fxml"));
                Parent root = loader.load();

                stage = (Stage) loginButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

                outputFile.print("user: " + loginUsername.getText() + " successfully logged in at: " +
                        Timestamp.valueOf(LocalDateTime.now()) + "\n");

                appointmentCalculator();
            } else if (userId < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(MESSAGES.getString("Error"));
                alert.setContentText(MESSAGES.getString("Incorrect"));
                alert.show();

                //log the failed login attempt
                outputFile.print("user: " + loginUsername.getText() + " failed login attempt at: " +
                        Timestamp.valueOf(LocalDateTime.now()) + "\n");
            }
            outputFile.close();
        } catch (IOException | SQLException ioException) {
            ioException.printStackTrace();
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
     * Initializes the Login,
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Locale locale = Locale.getDefault();
        Locale.setDefault(locale);
        ZoneId zone = ZoneId.systemDefault();
        loginLocationField.setText(String.valueOf(zone));

        usernameField.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                try {
                    loginButton();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        passwordField.addEventHandler(KeyEvent.KEY_PRESSED, ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                try {
                    loginButton();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void appointmentCalculator() throws SQLException {
        ObservableList<Appointments> getAllAppointments = AppointmentDao.getAllAppointments();
        LocalDateTime startTime;
        int getAppointmentID = 0;
        LocalDateTime displayTime = null;
        boolean appointmentWithin15Min = false;


        for (Appointments appointment : getAllAppointments) {
            startTime = appointment.getStartTime();
            displayTime = startTime;
            if ((startTime.isAfter(LocalDateTime.now().minusMinutes(15)) ||
                    startTime.isEqual(LocalDateTime.now().minusMinutes(15)) &&
                            (startTime.isBefore(LocalDateTime.now().plusMinutes(15)) ||
                                    (startTime.isEqual(LocalDateTime.now().plusMinutes(15)))))) {
                getAppointmentID = appointment.getAppointmentID();
                appointmentWithin15Min = true;
            }
        }

        if (!appointmentWithin15Min) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Appointment " + getAppointmentID + " starts within 15 minutes. Start time: " +
                            displayTime);
            alert.setTitle("Alert");
            alert.setContentText("Please select a customer to be deleted.");
            Optional<ButtonType> confirmation = alert.showAndWait();
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "No upcoming appointments.");
            Optional<ButtonType> confirmation = alert.showAndWait();
        }
    }
}



