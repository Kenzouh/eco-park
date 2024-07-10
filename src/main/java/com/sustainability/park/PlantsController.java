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

public class PlantsController {

    @FXML
    private TableView<Plant> tableView;

    @FXML
    private TableColumn<Plant, Integer> idColumn;

    @FXML
    private TableColumn<Plant, String> nameColumn;

    @FXML
    private TableColumn<Plant, String> speciesColumn;

    @FXML
    private TableColumn<Plant, String> areaColumn;

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
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        speciesColumn.setCellValueFactory(new PropertyValueFactory<>("species"));
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("area"));

        // Fetch and display plant records when the application initializes
        tableView.setItems(getPlantRecords());
        add.setOnAction(this::handleAdd);
        edit.setOnAction(this::handleEdit);
        update.setOnAction(this::handleUpdate);
        delete.setOnAction(this::handleDelete);
        menu.setOnAction(this::handleMenu);
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
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEdit(ActionEvent actionEvent) {
        Stage stage = (Stage) edit.getScene().getWindow();
        stage.close();

        EditApplication editApp = new EditApplication();
        Stage editStage = new Stage();

        try {
            editApp.start(editStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate(ActionEvent actionEvent) {
        // Refresh the table with updated records
        tableView.setItems(getPlantRecords());
    }

    @FXML
    private void handleDelete(ActionEvent actionEvent) {
        Stage stage = (Stage) delete.getScene().getWindow();
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
            e.printStackTrace();
        }
    }

    private ObservableList<Plant> getPlantRecords() {
        ObservableList<Plant> plantRecords = FXCollections.observableArrayList();
        Connection connection = null;
        try {
            connection = dbConnect.getConnection();
            if (connection != null) {
                String query = "SELECT pi.plant_species_id, pi.common_name, pi.plant_type, ps.plant_area_id " +
                        "FROM plant_information pi " +
                        "JOIN plant_species ps ON pi.plant_species_id = ps.plant_species_id";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    ResultSet resultSet = statement.executeQuery();
                    while (resultSet.next()) {
                        int id = resultSet.getInt("plant_species_id");
                        String name = resultSet.getString("common_name");
                        String species = resultSet.getString("plant_type");
                        String area = resultSet.getString("plant_area_id");  // Assuming plant_area_id is what you want to display as "area"
                        plantRecords.add(new Plant(id, name, species, area));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return plantRecords;
    }

    public static class Plant {
        private final int id;
        private final String name;
        private final String species;
        private final String area;

        public Plant(int id, String name, String species, String area) {
            this.id = id;
            this.name = name;
            this.species = species;
            this.area = area;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getSpecies() {
            return species;
        }

        public String getArea() {
            return area;
        }
    }
}
