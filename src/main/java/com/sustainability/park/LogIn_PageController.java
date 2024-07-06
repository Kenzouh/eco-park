package com.sustainability.park;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogIn_PageController {

    @FXML
    private TextField inputUsername;
    @FXML
    private TextField inputPassword;
    @FXML
    private Button LogIn;
    @FXML
    private ImageView imageView;

    private static final Logger logger = Logger.getLogger(LogIn_PageController.class.getName());

    @FXML
    public void initialize() {
        Image image = new Image(getClass().getResourceAsStream("/com/sustainability/park/Images/Dam.png"));
        imageView.setImage(image);
    }

    @FXML
    protected void onLogInButton(ActionEvent event) {
        String username = inputUsername.getText();
        String password = inputPassword.getText();

        Connection connection = dbConnect.getConnection();

        if (connection != null) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM admin WHERE admin_username = ? AND admin_password = ?")) {
                statement.setString(1, username);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {

                    resultSet.close();
                    connection.close();

                    Stage stage = (Stage) LogIn.getScene().getWindow();
                    stage.close();

                    Menu_Page menu_page = new Menu_Page();
                    Stage menuStage = new Stage();

                    try {
                        menu_page.start(menuStage);
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Failed to launch PlantsApplication", e);
                    }
                } else {
                    System.out.println("Invalid username or password.");
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Database error during login", e);
            }
        }
    }
}
