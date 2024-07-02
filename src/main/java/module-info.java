module com.sustainability.park {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.sustainability.park to javafx.fxml;
    exports com.sustainability.park;
}