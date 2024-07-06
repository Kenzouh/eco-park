package com.sustainability.park;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PlantsController {
    @FXML
    private Label plants_id;

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

    @FXML
    public void initialize() {
        add.setOnAction(this::handleAdd);
        edit.setOnAction(this::handleEdit);
        update.setOnAction(this::handleUpdate);
        delete.setOnAction(this::handleDelete);
        menu.setOnAction(this::handleMenu);
    }


    @FXML
    private void handleEdit(ActionEvent actionEvent) {
        Stage stage = (Stage) edit.getScene().getWindow();
        stage.close();


    }

    @FXML
    private void handleAdd(ActionEvent actionEvent) {
        Stage stage = (Stage) add.getScene().getWindow();
        stage.close();

        AddApplication addApp = new AddApplication();
        Stage addStage = new Stage();

        try {
            addApp.start(addStage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to launch PlantsApplication", e);
        }
    }

    @FXML
    private void handleDelete(ActionEvent actionEvent) {
        Stage stage = (Stage) delete.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleUpdate(ActionEvent actionEvent) {
        Stage stage = (Stage) update.getScene().getWindow();
        stage.close();

    }

    @FXML
    private void handleMenu(ActionEvent actionEvent) {
        Stage stage = (Stage) menu.getScene().getWindow();
        stage.close();

        Menu_Page menu_page = new Menu_Page();
        Stage menuStage = new Stage();

        try {
            menu_page.start(menuStage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to launch PlantsApplication", e);
        }
    }
}
