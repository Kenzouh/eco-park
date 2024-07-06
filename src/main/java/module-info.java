module com.sustainability.park {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires jdk.jdi;


    opens com.sustainability.park to javafx.fxml;
    exports com.sustainability.park;
}