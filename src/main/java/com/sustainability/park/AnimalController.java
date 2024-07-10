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

public class AnimalController {

    @FXML
    private TableView<Animal> tableView;

    @FXML
    private TableColumn<Animal, Integer> idColumn;

    @FXML
    private TableColumn<Animal, String> nameColumn;

    @FXML
    private TableColumn<Animal, String> speciesColumn;

    @FXML
    private TableColumn<Animal, String> areaColumn;

    @FXML
    private TableColumn<Animal, String> dietColumn;

    @FXML
    private TableColumn<Animal, String> typeColumn;

    @FXML
    private TableColumn<Animal, Integer> populationColumn;

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
    private static final Logger logger = Logger.getLogger(AnimalController.class.getName());

    private final dbConnect dbConnect = new dbConnect(); // Assuming you have a class for database connection

    @FXML
    public void initialize() {
        add.setOnAction(this::handleAdd);
        edit.setOnAction(this::handleEdit);
        update.setOnAction(this::handleUpdate);
        delete.setOnAction(this::handleDelete);
        menu.setOnAction(this::handleMenu);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        speciesColumn.setCellValueFactory(new PropertyValueFactory<>("species"));
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("area"));
        dietColumn.setCellValueFactory(new PropertyValueFactory<>("diet"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        populationColumn.setCellValueFactory(new PropertyValueFactory<>("population"));

        // Fetch and display animal records when the application initializes
        tableView.setItems(getAnimalRecords());
    }

    @FXML
    private void handleAdd(ActionEvent actionEvent) {
        closeWindow(add);
        openAddWindow();
    }

    @FXML
    private void handleEdit(ActionEvent actionEvent) {
        closeWindow(edit);
        openEditWindow();
    }

    @FXML
    private void handleUpdate(ActionEvent actionEvent) {
        // Refresh the table with updated records
        tableView.setItems(getAnimalRecords());
    }

    @FXML
    private void handleDelete(ActionEvent actionEvent) {
        closeWindow(delete);
    }

    @FXML
    private void handleMenu(ActionEvent actionEvent) {
        closeWindow(menu);
        openMenuPage();
    }

    private void closeWindow(Button button) {
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

    private void openAddWindow() {
        AddApplication addApp = new AddApplication();
        Stage addStage = new Stage();

        try {
            addApp.start(addStage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to launch AddApplication", e);
        }
    }

    private void openEditWindow() {
        EditApplication editApp = new EditApplication();
        Stage editStage = new Stage();

        try {
            editApp.start(editStage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to launch EditApplication", e);
        }
    }

    private void openMenuPage() {
        Menu_Page menuPage = new Menu_Page();
        Stage menuStage = new Stage();

        try {
            menuPage.start(menuStage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to launch Menu_Page", e);
        }
    }

    private ObservableList<Animal> getAnimalRecords() {
        ObservableList<Animal> animalRecords = FXCollections.observableArrayList();
        Connection connection = null;
        try {
            connection = dbConnect.getConnection();
            if (connection != null) {
                String query = "SELECT ai.animal_id, asp.common_name AS animal_name, asp.animal_species_id, ai.animal_area_id AS animal_area, at.diet, at.animal_type AS animal_type_id, at.population " +
                        "FROM animal_information ai " +
                        "JOIN animal_species asp ON ai.animal_species_id = asp.animal_species_id " +
                        "JOIN park_areas pa ON ai.animal_area_id = pa.area_id " +
                        "JOIN animal_types at ON asp.animal_type_id = at.animal_type_id";

                try (PreparedStatement statement = connection.prepareStatement(query);
                     ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("animal_id");
                        String name = resultSet.getString("animal_name");
                        String species = resultSet.getString("animal_species_id");
                        String area = resultSet.getString("animal_area");
                        String diet = resultSet.getString("diet");
                        String type = resultSet.getString("animal_type_id");
                        int population = resultSet.getInt("population");
                        animalRecords.add(new Animal(id, name, species, area, diet, type, population));
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
        return animalRecords;
    }

    public static class Animal {
        private final int id;
        private final String name;
        private final String species;
        private final String area;
        private final String diet;
        private final String type;
        private final int population;

        public Animal(int id, String name, String species, String area, String diet, String type, int population) {
            this.id = id;
            this.name = name;
            this.species = species;
            this.area = area;
            this.diet = diet;
            this.type = type;
            this.population = population;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getSpecies() { return species; }
        public String getArea() { return area; }
        public String getDiet() { return diet; }
        public String getType() { return type; }
        public int getPopulation() { return population; }
    }
}
