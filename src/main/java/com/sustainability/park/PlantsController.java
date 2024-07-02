package com.sustainability.park;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PlantsController {
    @FXML
    private Label plants_id;

    @FXML
    private Button plants_get_id;

    @FXML
    protected void onHelloButtonClick() {
        int num = 10;
        plants_id.setText(Integer.toString(num));
    }
}
