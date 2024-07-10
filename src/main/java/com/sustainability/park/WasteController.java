package com.sustainability.park;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WasteController {

    @FXML
    private TableView<Waste> wasteTableView;

    @FXML
    private TableColumn<Waste, Integer> idColumn;

    @FXML
    private TableColumn<Waste, String> typeColumn;

    @FXML
    private TableColumn<Waste, String> disposalDateColumn;

    @FXML
    private TableColumn<Waste, String> areaColumn;

    @FXML
    private Button add;

    @FXML
    private Button edit;

    @FXML
    private Button update;

    @FXML
    private Button delete;

    @FXML
    private Button menu;

    private static final Logger logger = Logger.getLogger(WasteController.class.getName());

    @FXML
    public void initialize() {
        add.setOnAction(this::handleAdd);
        edit.setOnAction(this::handleEdit);
        update.setOnAction(this::handleUpdate);
        delete.setOnAction(this::handleDelete);
        menu.setOnAction(this::handleMenu);

        // Initialize TableView columns with model properties
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        disposalDateColumn.setCellValueFactory(new PropertyValueFactory<>("disposalDate"));
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("area"));

        // Load data into TableView
        loadWasteRecords();
    }

    private void loadWasteRecords() {
        ObservableList<Waste> wasteRecords = FXCollections.observableArrayList();
        Connection connection = null;
        try {
            connection = dbConnect.getConnection();
            if (connection != null) {
                String query = "SELECT * FROM waste_management";
                try (PreparedStatement statement = connection.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("waste_id");
                        int area_id = resultSet.getInt("waste_area_id");
                        String disposalDate = resultSet.getString("disposal_date");
                        String wasteType = resultSet.getString("waste_type");

                        // Fetch area name from park_areas table based on areaId
                        String park_area_name = fetchAreaName(area_id);

                        wasteRecords.add(new Waste(id, wasteType, disposalDate, park_area_name));
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error loading waste records", e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        wasteTableView.setItems(wasteRecords);
    }

    private String fetchAreaName(int areaId) {
        String areaName = "";
        Connection connection = null;
        try {
            connection = dbConnect.getConnection();
            if (connection != null) {
                String query = "SELECT park_area_name FROM park_areas WHERE area_id = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, areaId);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        areaName = resultSet.getString("park_area_name");
                    }
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching area name", e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return areaName;
    }

    // Event handler methods for buttons
    @FXML
    private void handleAdd(ActionEvent event) {
        Stage stage = (Stage) add.getScene().getWindow();
        stage.close();

        // Implement handleAdd functionality
        AddApplication addApp = new AddApplication();
        Stage addStage = new Stage();

        try {
            addApp.start(addStage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to launch AddApplication", e);
        }
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        Stage stage = (Stage) edit.getScene().getWindow();
        stage.close();

        // Implement handleEdit functionality
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        Stage stage = (Stage) update.getScene().getWindow();
        stage.close();

        // Implement handleUpdate functionality
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        Stage stage = (Stage) delete.getScene().getWindow();
        stage.close();

        // Implement handleDelete functionality
    }

    @FXML
    private void handleMenu(ActionEvent event) {
        Stage stage = (Stage) menu.getScene().getWindow();
        stage.close();

        Menu_Page menuPage = new Menu_Page();
        Stage menuStage = new Stage();

        try {
            menuPage.start(menuStage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to launch Menu_Page", e);
        }
    }

    public static class Waste {
        private int id;
        private String type;
        private String disposalDate;
        private String area;

        public Waste(int id, String type, String disposalDate, String area) {
            this.id = id;
            this.type = type;
            this.disposalDate = disposalDate;
            this.area = area;
        }

        // Getters and setters (generated or manually implemented)
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDisposalDate() {
            return disposalDate;
        }

        public void setDisposalDate(String disposalDate) {
            this.disposalDate = disposalDate;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }
    }
}
