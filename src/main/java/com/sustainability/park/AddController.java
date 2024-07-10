package com.sustainability.park;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

public class AddController {
    @FXML
    private TextField habitatField;

    @FXML
    private TextField commonNameField;

    @FXML
    private TextField dietField;

    @FXML
    private TextField populationField;

    @FXML
    private TextField animalLifespanField;

    @FXML
    private ComboBox<String> areaComboBox;

    @FXML
    private ComboBox<String> animalTypeComboBox;

    @FXML
    private TextField plantCommonNameField;

    @FXML
    private TextField plantLifespanField;

    @FXML
    private ComboBox<String> plantTypeComboBox;

    @FXML
    private ComboBox<String> plantAreaComboBox;

    @FXML
    private ComboBox<String> wasteTypeComboBox;

    @FXML
    private ComboBox<String> wasteAreaComboBox;

    @FXML
    private DatePicker disposalDateField;

    @FXML
    public Button addAnimal;

    @FXML
    public Button addPlants;

    @FXML
    public Button addWaste;

    @FXML
    public Button exitAnimals;

    @FXML
    public Button exitPlants;

    @FXML
    public Button exitWaste;

    @FXML
    private static final Logger logger = Logger.getLogger(LogIn_PageController.class.getName());

    @FXML
    public void initialize() {
        initializeComboBoxes();
        addAnimal.setOnAction(this::handleAddAnimal);
        addPlants.setOnAction(this::handleAddPlants);
        addWaste.setOnAction(this::handleAddWaste);
        exitAnimals.setOnAction(this::handleExitAnimal);
        exitPlants.setOnAction(this::handleExitPlants);
        exitWaste.setOnAction(this::handleExitWaste);
    }

    @FXML
    private void handleExitWaste(ActionEvent actionEvent) {
        Stage stage = (Stage) exitWaste.getScene().getWindow();
        stage.close();

        WasteApplication wasteApp = new WasteApplication();
        Stage wasteStage = new Stage();

        try {
            wasteApp.start(wasteStage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to launch PlantsApplication", e);
        }
    }

    @FXML
    private void handleExitPlants(ActionEvent actionEvent) {
        Stage stage = (Stage) exitPlants.getScene().getWindow();
        stage.close();

        PlantsApplication plantsApp = new PlantsApplication();
        Stage plantsStage = new Stage();

        try {
            plantsApp.start(plantsStage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to launch PlantsApplication", e);
        }
    }

    @FXML
    private void handleExitAnimal(ActionEvent actionEvent) {
        Stage stage = (Stage) exitAnimals.getScene().getWindow();
        stage.close();

        AnimalApplication animalApp = new AnimalApplication();
        Stage animalStage = new Stage();

        try {
            animalApp.start(animalStage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to launch PlantsApplication", e);
        }
    }

    @FXML
    private void handleAddWaste(ActionEvent actionEvent) {
        String wasteType = wasteTypeComboBox.getValue();
        String disposalDate = disposalDateField.getValue().toString();
        String wasteArea = wasteAreaComboBox.getValue();

        Connection connection = null;
        PreparedStatement parkAreaStatement = null;
        PreparedStatement wasteManagementStatement = null;

        try {
            connection = dbConnect.getConnection();
            if (connection != null) {
                connection.setAutoCommit(false);

                // Insert into park_areas
                String insertParkArea = "INSERT INTO park_areas (park_area_name) VALUES (?)";
                parkAreaStatement = connection.prepareStatement(insertParkArea, Statement.RETURN_GENERATED_KEYS);
                parkAreaStatement.setString(1, wasteArea);
                int affectedRows = parkAreaStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Inserting park area failed, no rows affected.");
                }

                // Retrieve the generated area_id
                ResultSet generatedKeys = parkAreaStatement.getGeneratedKeys();
                int areaId = -1;
                if (generatedKeys.next()) {
                    areaId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to retrieve generated area_id for park_areas.");
                }

                // Insert into waste_management with area_id
                String insertWasteManagement = "INSERT INTO waste_management (disposal_date, waste_type, waste_area_id) VALUES (?, ?, ?)";
                wasteManagementStatement = connection.prepareStatement(insertWasteManagement);
                wasteManagementStatement.setString(1, disposalDate);
                wasteManagementStatement.setString(2, wasteType);
                wasteManagementStatement.setInt(3, areaId);
                wasteManagementStatement.executeUpdate();

                connection.commit();
                System.out.println("Added successfully");
            }
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();  // Rollback transaction on error
                }
            } catch (SQLException rollbackEx) {
                logger.log(Level.SEVERE, "Failed to rollback transaction", rollbackEx);
            }
            logger.log(Level.SEVERE, "Failed to add waste management entry", e);
        } finally {
            try {
                if (parkAreaStatement != null) {
                    parkAreaStatement.close();
                }
                if (wasteManagementStatement != null) {
                    wasteManagementStatement.close();
                }
                if (connection != null) {
                    connection.setAutoCommit(true);  // Reset autocommit to true
                    connection.close();  // Close the connection
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Failed to close statements or connection", ex);
            }
        }
        clearWasteFields();
    }

    @FXML
    private void handleAddPlants(ActionEvent actionEvent) {
        String commonName = plantCommonNameField.getText();
        String plantType = plantTypeComboBox.getValue();
        String lifespanText = plantLifespanField.getText();
        String area = plantAreaComboBox.getValue();  // Corrected from areaComboBox to plantAreaComboBox

        if (commonName == null || commonName.isEmpty() ||
                plantType == null || plantType.isEmpty() ||
                lifespanText == null || lifespanText.isEmpty() ||
                area == null || area.isEmpty()) {
            System.out.println("Please make sure all fields are filled out.");
            return;
        }

        int lifespan;
        try {
            lifespan = Integer.parseInt(lifespanText);
        } catch (NumberFormatException e) {
            System.out.println("Lifespan must be a valid integer.");
            return;
        }

        Connection connection = null;
        try {
            connection = dbConnect.getConnection();
            if (connection != null) {
                connection.setAutoCommit(false);

                // Insert into plant_species (assuming common_name is unique)
                String insertPlantSpecies = "INSERT INTO plant_information (common_name, plant_type, lifespan) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE plant_species_id = LAST_INSERT_ID(plant_species_id)";
                int speciesId = -1;
                try (PreparedStatement speciesStatement = connection.prepareStatement(insertPlantSpecies, Statement.RETURN_GENERATED_KEYS)) {
                    speciesStatement.setString(1, commonName);
                    speciesStatement.setString(2, plantType);
                    speciesStatement.setInt(3, lifespan);
                    speciesStatement.executeUpdate();

                    // Retrieve the generated plant_species_id
                    ResultSet generatedSpeciesKeys = speciesStatement.getGeneratedKeys();
                    if (generatedSpeciesKeys.next()) {
                        speciesId = generatedSpeciesKeys.getInt(1);
                    } else {
                        throw new SQLException("Inserting plant species failed, no ID obtained.");
                    }
                }

                // Insert into park_areas if not exists
                String insertParkArea = "INSERT INTO park_areas (park_area_name) VALUES (?) ON DUPLICATE KEY UPDATE park_area_name = park_area_name";
                try (PreparedStatement parkAreaStatement = connection.prepareStatement(insertParkArea, Statement.RETURN_GENERATED_KEYS)) {
                    parkAreaStatement.setString(1, area);
                    parkAreaStatement.executeUpdate();
                }

                // Get area_id based on area name
                String getAreaIdQuery = "SELECT area_id FROM park_areas WHERE park_area_name = ?";
                int areaId = -1;
                try (PreparedStatement areaStatement = connection.prepareStatement(getAreaIdQuery)) {
                    areaStatement.setString(1, area);
                    ResultSet areaResult = areaStatement.executeQuery();
                    if (areaResult.next()) {
                        areaId = areaResult.getInt("area_id");
                    } else {
                        throw new SQLException("Area not found: " + area);
                    }
                }

                // Insert into plant_information
                String insertPlantInfo = "INSERT INTO plant_species (plant_species_id, plant_area_id) VALUES (?, ?)";
                try (PreparedStatement plantInfoStatement = connection.prepareStatement(insertPlantInfo)) {
                    plantInfoStatement.setInt(1, speciesId);
                    plantInfoStatement.setInt(2, areaId);
                    plantInfoStatement.executeUpdate();
                }

                // Commit transaction
                connection.commit();
                System.out.println("Plant, Species, Type, and Area information added successfully.");
            }
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();  // Rollback transaction on error
                }
            } catch (SQLException rollbackEx) {
                System.out.println("Failed to rollback transaction: " + rollbackEx.getMessage());
            }
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);  // Reset autocommit to true
                    connection.close();  // Close the connection
                }
            } catch (SQLException ex) {
                System.out.println("Failed to reset auto commit or close connection: " + ex.getMessage());
            }
        }

        clearPlantFields();
    }


    @FXML
    private void handleAddAnimal(ActionEvent actionEvent) {
        String commonName = commonNameField.getText();
        String diet = dietField.getText();
        int population = Integer.parseInt(populationField.getText());
        int lifespan = Integer.parseInt(animalLifespanField.getText());
        String animalType = animalTypeComboBox.getValue();
        String area = areaComboBox.getValue();
        String habitat = habitatField.getText();

        Connection connection = null;
        try {
            connection = dbConnect.getConnection();
            if (connection != null) {
                connection.setAutoCommit(false);

                // Insert into animal_types (assuming animal_type is unique)
                String insertAnimalTypes = "INSERT INTO animal_types (animal_type, diet, population, lifespan) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE animal_type_id = LAST_INSERT_ID(animal_type_id)";
                int typeId = -1;
                try (PreparedStatement typesStatement = connection.prepareStatement(insertAnimalTypes, Statement.RETURN_GENERATED_KEYS)) {
                    typesStatement.setString(1, animalType);
                    typesStatement.setString(2, diet);
                    typesStatement.setInt(3, population);
                    typesStatement.setInt(4, lifespan);
                    typesStatement.executeUpdate();

                    // Retrieve the generated type_id
                    ResultSet generatedTypeKeys = typesStatement.getGeneratedKeys();
                    if (generatedTypeKeys.next()) {
                        typeId = generatedTypeKeys.getInt(1);
                    } else {
                        throw new SQLException("Inserting animal type failed, no ID obtained.");
                    }
                }

                // Insert into animal_species (assuming common_name is unique)
                String insertAnimalSpecies = "INSERT INTO animal_species (common_name, animal_type_id) VALUES (?, ?)";
                int speciesId = -1;
                try (PreparedStatement speciesStatement = connection.prepareStatement(insertAnimalSpecies, Statement.RETURN_GENERATED_KEYS)) {
                    speciesStatement.setString(1, commonName);
                    speciesStatement.setInt(2, typeId);
                    speciesStatement.executeUpdate();

                    // Retrieve the generated species_id
                    ResultSet generatedSpeciesKeys = speciesStatement.getGeneratedKeys();
                    if (generatedSpeciesKeys.next()) {
                        speciesId = generatedSpeciesKeys.getInt(1);
                    } else {
                        throw new SQLException("Inserting animal species failed, no ID obtained.");
                    }
                }

                // Insert into park_areas if not exists
                String insertParkArea = "INSERT INTO park_areas (park_area_name) VALUES (?) ON DUPLICATE KEY UPDATE park_area_name = park_area_name";
                try (PreparedStatement parkAreaStatement = connection.prepareStatement(insertParkArea, Statement.RETURN_GENERATED_KEYS)) {
                    parkAreaStatement.setString(1, area);
                    parkAreaStatement.executeUpdate();
                }

                // Get area_id based on area name
                String getAreaIdQuery = "SELECT area_id FROM park_areas WHERE park_area_name = ?";
                int areaId = -1;
                try (PreparedStatement areaStatement = connection.prepareStatement(getAreaIdQuery)) {
                    areaStatement.setString(1, area);
                    ResultSet areaResult = areaStatement.executeQuery();
                    if (areaResult.next()) {
                        areaId = areaResult.getInt("area_id");
                    } else {
                        throw new SQLException("Area not found: " + area);
                    }
                }

                // Insert into animal_information
                String insertAnimalInfo = "INSERT INTO animal_information (animal_species_id, habitat, animal_area_id) VALUES (?, ?, ?)";
                try (PreparedStatement animalInfoStatement = connection.prepareStatement(insertAnimalInfo)) {
                    animalInfoStatement.setInt(1, speciesId);
                    animalInfoStatement.setString(2, habitat);
                    animalInfoStatement.setInt(3, areaId);
                    animalInfoStatement.executeUpdate();
                }

                // Commit transaction
                connection.commit();
                System.out.println("Animal, Species, Type, and Area information added successfully.");
            }
        } catch (SQLException | NumberFormatException e) {
            try {
                if (connection != null) {
                    connection.rollback();  // Rollback transaction on error
                }
            } catch (SQLException rollbackEx) {
                System.out.println("Failed to rollback transaction: " + rollbackEx.getMessage());
            }
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);  // Reset autocommit to true
                    connection.close();  // Close the connection
                }
            } catch (SQLException ex) {
                System.out.println("Failed to reset auto commit or close connection: " + ex.getMessage());
            }
        }

        clearAnimalFields();
    }


    private void initializeComboBoxes() {
        animalTypeComboBox.getItems().addAll(
                "Mammals", "Birds", "Reptiles", "Amphibians", "Fish", "Invertebrates"
        );

        plantTypeComboBox.getItems().addAll(
                "Flowering Plants", "Non-flowering Plants", "Trees", "Shrubs",
                "Herbs", "Ferns", "Cacti and Succulents", "Aquatic Plants",
                "Grasses", "Epiphytes"
        );

        wasteTypeComboBox.getItems().addAll(
                "Solid Waste", "Liquid Waste", "Hazardous Waste", "Organic Waste",
                "Recyclable Waste", "Electronic Waste (e-waste)", "Radioactive Waste",
                "Construction and Demolition Waste", "Biomedical Waste", "Agricultural Waste"
        );

        areaComboBox.getItems().addAll(
                "Big Mammal Enclosure", "Small Mammal Enclosure", "Lagoon Enclosure",
                "Crocodile Enclosure", "Reptile House", "Flightless Area", "Big Dome"
        );

        plantAreaComboBox.getItems().addAll(
                "Big Mammal Enclosure", "Small Mammal Enclosure", "Lagoon Enclosure",
                "Crocodile Enclosure", "Reptile House", "Flightless Area", "Big Dome"
        );

        wasteAreaComboBox.getItems().addAll(
                "Big Mammal Enclosure", "Small Mammal Enclosure", "Lagoon Enclosure",
                "Crocodile Enclosure", "Reptile House", "Flightless Area", "Big Dome"
        );
    }

    @FXML
    public void exit(ActionEvent actionEvent) {

    }

    private void clearAnimalFields() {
        commonNameField.clear();
        dietField.clear();
        populationField.clear();
        animalLifespanField.clear();
        areaComboBox.getSelectionModel().clearSelection();
        animalTypeComboBox.getSelectionModel().clearSelection();
        habitatField.clear();
    }

    private void clearPlantFields() {
        plantCommonNameField.clear();
        plantLifespanField.clear();
        plantTypeComboBox.getSelectionModel().clearSelection();
        plantAreaComboBox.getSelectionModel().clearSelection();
    }

    private void clearWasteFields() {
        wasteTypeComboBox.getSelectionModel().clearSelection();
        disposalDateField.getEditor().clear();
        wasteAreaComboBox.getSelectionModel().clearSelection();
    }
}
