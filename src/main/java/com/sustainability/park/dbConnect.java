package com.sustainability.park;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;

public class dbConnect {

    private static final Logger logger = Logger.getLogger(LogIn_PageController.class.getName());

    public static void main(String[] args) {
        getConnection();
    }

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            logger.log(Level.SEVERE, "Database error during login", e);
            return null;
        }

        // Proceed to establish the connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/ecoparksustain", "root", "");
        } catch (Exception e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
            logger.log(Level.SEVERE, "Database error during login", e);
        }
        return conn;
    }

}
