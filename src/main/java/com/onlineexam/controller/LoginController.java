package com.onlineexam.controller;

import com.onlineexam.Main;
import com.onlineexam.dao.UserDAO;
import com.onlineexam.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Login view
 * Handles user authentication and navigation to appropriate dashboard
 */
public class LoginController implements Initializable {
    
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Button registerButton;
    @FXML private Label statusLabel;
    
    private UserDAO userDAO;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userDAO = new UserDAO();
        
        // Set default button
        loginButton.setDefaultButton(true);
        
        // Add enter key support
        usernameField.setOnKeyPressed(this::handleKeyPressed);
        passwordField.setOnKeyPressed(this::handleKeyPressed);
        
        // Clear status label when user starts typing
        usernameField.textProperty().addListener((obs, oldText, newText) -> clearStatus());
        passwordField.textProperty().addListener((obs, oldText, newText) -> clearStatus());
    }
    
    /**
     * Handle login button click
     */
    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            showStatus("Please enter both username and password", false);
            return;
        }
        
        try {
            User user = userDAO.authenticateUser(username, password);
            if (user != null) {
                Main.setCurrentUser(user);
                clearFields();
                
                if (user.isAdmin()) {
                    Main.showAdminDashboard();
                } else {
                    Main.showStudentDashboard();
                }
            } else {
                showStatus("Invalid username or password", false);
            }
        } catch (Exception e) {
            showStatus("Login failed: " + e.getMessage(), false);
            e.printStackTrace();
        }
    }
    
    /**
     * Handle register button click
     */
    @FXML
    private void handleRegister() {
        Main.showRegisterView();
    }
    
    /**
     * Handle key press events
     */
    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleLogin();
        }
    }
    
    /**
     * Clear input fields
     */
    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
    }
    
    /**
     * Clear status label
     */
    private void clearStatus() {
        statusLabel.setText("");
    }
    
    /**
     * Show status message
     * @param message the message to show
     * @param isSuccess true if success message, false if error
     */
    private void showStatus(String message, boolean isSuccess) {
        statusLabel.setText(message);
        statusLabel.setStyle(isSuccess ? "-fx-text-fill: #27ae60;" : "-fx-text-fill: #e74c3c;");
    }
}
