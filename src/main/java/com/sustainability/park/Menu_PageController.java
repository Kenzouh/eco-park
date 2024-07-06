package com.sustainability.park;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Menu_PageController {
    @FXML
    private Button Animals;

    @FXML
    private Button Plants;

    @FXML
    private Button Waste;

    @FXML
    private Button SignOut;

    @FXML
    private static final Logger logger = Logger.getLogger(LogIn_PageController.class.getName());

    @FXML
    public void initialize() {
        SignOut.setOnAction(this::handleSignOut);
        Waste.setOnAction(this::handleWaste);
        Plants.setOnAction(this::handlePlants);
        Animals.setOnAction(this::handleAnimals);
    }

    @FXML
    private void handleAnimals(ActionEvent actionEvent) {
        Stage stage = (Stage) Animals.getScene().getWindow();
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
    private void handlePlants(ActionEvent actionEvent) {
        Stage stage = (Stage) Plants.getScene().getWindow();
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
    private void handleSignOut(ActionEvent event) {
        Stage stage = (Stage) SignOut.getScene().getWindow();
        stage.close();

        LogIn_Page logInPage = new LogIn_Page();
        Stage logInStage = new Stage();

        try {
            logInPage.start(logInStage);
        }catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to launch SignOut", e);
        }
    }

    @FXML
    private void handleWaste(ActionEvent actionEvent) {
        Stage stage = (Stage) Waste.getScene().getWindow();
        stage.close();

        WasteApplication wasteApp = new WasteApplication();
        Stage wasteStage = new Stage();

        try {
            wasteApp.start(wasteStage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to launch PlantsApplication", e);
        }
    }
}
