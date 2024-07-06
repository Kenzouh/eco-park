package com.sustainability.park;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.logging.Logger;

public class WasteController {
    @FXML
    public Button add;

    @FXML
    public Button edit;

    @FXML
    public Button update;

    @FXML
    public Button delete;

    @FXML
    public Button menu;

    @FXML
    private static final Logger logger = Logger.getLogger(LogIn_PageController.class.getName());

//    @FXML
//    public void initialize() {
//        add.setOnAction(this::handleAdd);
//        edit.setOnAction(this::handleEdit);
//        update.setOnAction(this::handleUpdate);
//        delete.setOnAction(this::handleDelete);
//        menu.setOnAction(this::handleMenu);
//    }
}
