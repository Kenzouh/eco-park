package com.example.projectsss;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class HelloController {

    @FXML
    private TextField searchTextField;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private StackPane contentStackPane;

    @FXML
    private AnchorPane plantsPane;

    @FXML
    private AnchorPane animalsPane;

    @FXML
    private AnchorPane wastePane;

    @FXML
    private TableView<?> plantsTableView;

    @FXML
    private TableView<?> animalsTableView;

    @FXML
    private TableView<?> wasteTableView;

    @FXML
    public void initialize() {
        // Set initial visibility
        plantsPane.setVisible(false);
        animalsPane.setVisible(false);
        wastePane.setVisible(false);

        // Add listener to ComboBox
        categoryComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case "Plants":
                    showPane(plantsPane);
                    break;
                case "Animals":
                    showPane(animalsPane);
                    break;
                case "Waste":
                    showPane(wastePane);
                    break;
            }
        });
    }

    private void showPane(AnchorPane pane) {
        plantsPane.setVisible(false);
        animalsPane.setVisible(false);
        wastePane.setVisible(false);
        pane.setVisible(true);
    }
}
