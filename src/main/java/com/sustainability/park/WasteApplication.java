package com.sustainability.park;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class WasteApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/sustainability/park/waste_view.fxml")));
        Scene scene = new Scene(root);

        stage.setTitle("WASTE");
        stage.setScene(scene);
        stage.show();
    }
}