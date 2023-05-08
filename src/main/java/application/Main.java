package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/** The main class creates an application for inventory management and adds sample data. */
public class Main extends Application {

    /**
     * Initializes application login view.
     *
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 720, 720);
        stage.setTitle("Inventory Management");
        stage.setScene(scene);
        stage.show();
    }
    /**
     * The main method loads all database connection and launches the fmxl.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        DBConnection.startConnection();
        launch(args);
        DBConnection.closeConnection();
    }
}