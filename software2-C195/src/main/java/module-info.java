module application {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens application to javafx.fxml;
    opens controllers to javafx.fxml;
    opens models to javafx.fxml;

    exports application;
    exports controllers;
    exports models;
}