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

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Controller for the Registration view
 * Handles new student registration
 */
public class RegisterController implements Initializable {
    
    @FXML private TextField fullNameField;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button registerButton;
    @FXML private Button backButton;
    @FXML private Label statusLabel;
    
    private UserDAO userDAO;
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userDAO = new UserDAO();
        
        // Set default button
        registerButton.setDefaultButton(true);
        
        // Clear status label when user starts typing
        fullNameField.textProperty().addListener((obs, oldText, newText) -> clearStatus());
        usernameField.textProperty().addListener((obs, oldText, newText) -> clearStatus());
        emailField.textProperty().addListener((obs, oldText, newText) -> clearStatus());
        passwordField.textProperty().addListener((obs, oldText, newText) -> clearStatus());
        confirmPasswordField.textProperty().addListener((obs, oldText, newText) -> clearStatus());
    }
    
    /**
     * Handle register button click
     */
    @FXML
    private void handleRegister() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        // Validate input
        if (!validateInput(fullName, username, email, password, confirmPassword)) {
            return;
        }
        
        try {
            // Check if username already exists
            if (userDAO.usernameExists(username)) {
                showStatus("Username already exists. Please choose a different username.", false);
                return;
            }
            
            // Create new user
            User newUser = new User(username, password, "STUDENT", email, fullName);
            
            if (userDAO.registerUser(newUser)) {
                showStatus("Registration successful! You can now login.", true);
                clearFields();
                
                // Auto-redirect to login after 2 seconds
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        javafx.application.Platform.runLater(() -> Main.showLoginView());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            } else {
                showStatus("Registration failed. Please try again.", false);
            }
        } catch (Exception e) {
            showStatus("Registration failed: " + e.getMessage(), false);
            e.printStackTrace();
        }
    }
    
    /**
     * Handle back button click
     */
    @FXML
    private void handleBack() {
        Main.showLoginView();
    }
    
    /**
     * Validate input fields
     * @param fullName the full name
     * @param username the username
     * @param email the email
     * @param password the password
     * @param confirmPassword the password confirmation
     * @return true if valid, false otherwise
     */
    private boolean validateInput(String fullName, String username, String email, 
                                String password, String confirmPassword) {
        if (fullName.isEmpty()) {
            showStatus("Please enter your full name", false);
            return false;
        }
        
        if (username.isEmpty()) {
            showStatus("Please enter a username", false);
            return false;
        }
        
        if (username.length() < 3) {
            showStatus("Username must be at least 3 characters long", false);
            return false;
        }
        
        if (email.isEmpty()) {
            showStatus("Please enter your email address", false);
            return false;
        }
        
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            showStatus("Please enter a valid email address", false);
            return false;
        }
        
        if (password.isEmpty()) {
            showStatus("Please enter a password", false);
            return false;
        }
        
        if (password.length() < 6) {
            showStatus("Password must be at least 6 characters long", false);
            return false;
        }
        
        if (!password.equals(confirmPassword)) {
            showStatus("Passwords do not match", false);
            return false;
        }
        
        return true;
    }
    
    /**
     * Clear input fields
     */
    private void clearFields() {
        fullNameField.clear();
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
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
