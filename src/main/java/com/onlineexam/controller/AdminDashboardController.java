package com.onlineexam.controller;

import com.onlineexam.Main;
import com.onlineexam.dao.QuestionDAO;
import com.onlineexam.dao.ResultDAO;
import com.onlineexam.dao.UserDAO;
import com.onlineexam.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Admin Dashboard view
 * Handles admin dashboard functionality and displays system statistics
 */
public class AdminDashboardController implements Initializable {
    
    @FXML private Label welcomeLabel;
    @FXML private Button logoutButton;
    @FXML private Button manageQuestionsButton;
    @FXML private Button viewResultsButton;
    @FXML private Button manageUsersButton;
    @FXML private Label totalQuestionsLabel;
    @FXML private Label totalStudentsLabel;
    @FXML private Label totalExamsLabel;
    @FXML private Label averageScoreLabel;
    
    private QuestionDAO questionDAO;
    private ResultDAO resultDAO;
    private UserDAO userDAO;
    private User currentUser;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        questionDAO = new QuestionDAO();
        resultDAO = new ResultDAO();
        userDAO = new UserDAO();
        currentUser = Main.getCurrentUser();
        
        loadUserData();
        loadStatistics();
    }
    
    /**
     * Load user data and update welcome label
     */
    private void loadUserData() {
        if (currentUser != null) {
            welcomeLabel.setText("Welcome, " + currentUser.getFullName() + "!");
        }
    }
    
    /**
     * Load and display system statistics
     */
    private void loadStatistics() {
        try {
            // Load statistics
            int totalQuestions = questionDAO.getTotalQuestionsCount();
            int totalStudents = userDAO.getAllStudents().size();
            
            double[] resultStats = resultDAO.getResultStatistics();
            int totalExams = (int) resultStats[0];
            double averageScore = resultStats[3];
            
            // Update labels
            totalQuestionsLabel.setText(String.valueOf(totalQuestions));
            totalStudentsLabel.setText(String.valueOf(totalStudents));
            totalExamsLabel.setText(String.valueOf(totalExams));
            
            if (totalExams > 0) {
                averageScoreLabel.setText(String.format("%.1f%%", averageScore));
            } else {
                averageScoreLabel.setText("N/A");
            }
            
        } catch (Exception e) {
            System.err.println("Error loading statistics: " + e.getMessage());
            e.printStackTrace();
            
            // Set default values on error
            totalQuestionsLabel.setText("0");
            totalStudentsLabel.setText("0");
            totalExamsLabel.setText("0");
            averageScoreLabel.setText("N/A");
        }
    }
    
    /**
     * Handle manage questions button click
     */
    @FXML
    private void handleManageQuestions() {
        Main.showQuestionManagementView();
    }
    
    /**
     * Handle view results button click
     */
    @FXML
    private void handleViewResults() {
        // For now, show a simple dialog with all results
        // In a more advanced version, this could open a dedicated results management view
        try {
            double[] stats = resultDAO.getResultStatistics();
            int totalExams = (int) stats[0];
            int passedExams = (int) stats[1];
            int failedExams = (int) stats[2];
            double averageScore = stats[3];
            
            StringBuilder resultText = new StringBuilder("System Statistics:\n\n");
            resultText.append("Total Exams Taken: ").append(totalExams).append("\n");
            resultText.append("Passed Exams: ").append(passedExams).append("\n");
            resultText.append("Failed Exams: ").append(failedExams).append("\n");
            resultText.append("Average Score: ").append(String.format("%.1f%%", averageScore)).append("\n");
            
            if (totalExams > 0) {
                double passRate = (double) passedExams / totalExams * 100;
                resultText.append("Pass Rate: ").append(String.format("%.1f%%", passRate)).append("\n");
            }
            
            Main.showAlert("System Statistics", resultText.toString(), 
                         javafx.scene.control.Alert.AlertType.INFORMATION);
            
        } catch (Exception e) {
            Main.showAlert("Error", "Failed to load statistics: " + e.getMessage(), 
                         javafx.scene.control.Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    /**
     * Handle manage users button click
     */
    @FXML
    private void handleManageUsers() {
        // For now, show a simple dialog with user information
        // In a more advanced version, this could open a dedicated user management view
        try {
            var students = userDAO.getAllStudents();
            
            if (students.isEmpty()) {
                Main.showAlert("No Students", "No students are registered in the system.", 
                             javafx.scene.control.Alert.AlertType.INFORMATION);
            } else {
                StringBuilder userText = new StringBuilder("Registered Students:\n\n");
                
                for (User student : students) {
                    userText.append("Name: ").append(student.getFullName()).append("\n");
                    userText.append("Username: ").append(student.getUsername()).append("\n");
                    userText.append("Email: ").append(student.getEmail()).append("\n\n");
                }
                
                Main.showAlert("Student List", userText.toString(), 
                             javafx.scene.control.Alert.AlertType.INFORMATION);
            }
            
        } catch (Exception e) {
            Main.showAlert("Error", "Failed to load user information: " + e.getMessage(), 
                         javafx.scene.control.Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    /**
     * Handle logout button click
     */
    @FXML
    private void handleLogout() {
        Main.setCurrentUser(null);
        Main.showLoginView();
    }
}
