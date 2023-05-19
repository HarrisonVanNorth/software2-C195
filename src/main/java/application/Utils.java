package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {


    /**
     * Method to convert local time to UTC for storage in database.
     * @param dateTime
     * @return
     */
    public static String convertTimeDateUTC(String localDateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        LocalDateTime currentLocalDateTime = LocalDateTime.parse(localDateTimeString, formatter);
        TimeZone localTimeZone = TimeZone.getDefault();
        Instant instant = currentLocalDateTime.atZone(localTimeZone.toZoneId()).toInstant();
        ZonedDateTime uTCDateTime = instant.atZone(ZoneId.of("Etc/UTC"));
        LocalDateTime localUTCDateTime = uTCDateTime.toLocalDateTime();
        String utcOUT = localUTCDateTime.format(formatter);
        return utcOUT;
    }


    /**
     * Method to convert time to UTC for storage in database.
     * @param dateTime
     * @return
     */
    public static LocalDateTime convertTimeDateLocal(String utcDateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        LocalDateTime currentUTCDateTime = LocalDateTime.parse(utcDateTimeString, formatter);
        Instant instant = currentUTCDateTime.atZone(ZoneId.of("Etc/UTC")).toInstant();
        TimeZone localTimeZone = TimeZone.getDefault();
        ZonedDateTime zonedDateTime = instant.atZone(localTimeZone.toZoneId());
        LocalDateTime localZonedDateTime = zonedDateTime.toLocalDateTime();
//        String utcOUT = localZonedDateTime.format(formatter);
        return localZonedDateTime;
    }


    /**
     * Navigation base to views.
     * @param event
     * @param filePath
     * @throws IOException
     */
    public static void navigationBase(ActionEvent event, String filePath) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Utils.class.getResource(filePath));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

}
