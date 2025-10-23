package com.onlineexam.controller;

import com.onlineexam.Main;
import com.onlineexam.dao.ResultDAO;
import com.onlineexam.model.Result;
import com.onlineexam.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Student Dashboard view
 * Handles student dashboard functionality and navigation
 */
public class StudentDashboardController implements Initializable {
    
    @FXML private Label welcomeLabel;
    @FXML private Button logoutButton;
    @FXML private Button takeExamButton;
    @FXML private Button viewResultsButton;
    @FXML private TableView<Result> resultsTable;
    @FXML private TableColumn<Result, String> examDateColumn;
    @FXML private TableColumn<Result, String> scoreColumn;
    @FXML private TableColumn<Result, String> statusColumn;
    @FXML private TableColumn<Result, String> timeTakenColumn;
    
    private ResultDAO resultDAO;
    private User currentUser;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resultDAO = new ResultDAO();
        currentUser = Main.getCurrentUser();
        
        setupTable();
        loadUserData();
        loadRecentResults();
    }
    
    /**
     * Setup the results table
     */
    private void setupTable() {
        examDateColumn.setCellValueFactory(cellData -> {
            Result result = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(
                result.getExamDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            );
        });
        
        scoreColumn.setCellValueFactory(cellData -> {
            Result result = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(result.getScorePercentage());
        });
        
        statusColumn.setCellValueFactory(cellData -> {
            Result result = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(result.getStatus());
        });
        
        timeTakenColumn.setCellValueFactory(cellData -> {
            Result result = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(result.getTimeTakenString());
        });
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
     * Load recent results for the current user
     */
    private void loadRecentResults() {
        if (currentUser != null) {
            try {
                List<Result> results = resultDAO.getResultsByUserId(currentUser.getId());
                
                // Show only the 5 most recent results
                List<Result> recentResults = results.stream()
                    .limit(5)
                    .collect(java.util.stream.Collectors.toList());
                
                ObservableList<Result> observableResults = FXCollections.observableArrayList(recentResults);
                resultsTable.setItems(observableResults);
            } catch (Exception e) {
                System.err.println("Error loading recent results: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Handle take exam button click
     */
    @FXML
    private void handleTakeExam() {
        Main.showExamView();
    }
    
    /**
     * Handle view results button click
     */
    @FXML
    private void handleViewResults() {
        // For now, we'll show a simple dialog with all results
        // In a more advanced version, this could open a dedicated results view
        if (currentUser != null) {
            try {
                List<Result> results = resultDAO.getResultsByUserId(currentUser.getId());
                
                if (results.isEmpty()) {
                    Main.showAlert("No Results", "You haven't taken any exams yet.", 
                                 javafx.scene.control.Alert.AlertType.INFORMATION);
                } else {
                    StringBuilder resultText = new StringBuilder("Your Exam Results:\n\n");
                    
                    for (Result result : results) {
                        resultText.append("Date: ").append(result.getExamDateString()).append("\n");
                        resultText.append("Score: ").append(result.getScorePercentage()).append("\n");
                        resultText.append("Status: ").append(result.getStatus()).append("\n");
                        resultText.append("Time Taken: ").append(result.getTimeTakenString()).append("\n");
                        resultText.append("Grade: ").append(result.getGrade()).append("\n\n");
                    }
                    
                    Main.showAlert("Exam Results", resultText.toString(), 
                                 javafx.scene.control.Alert.AlertType.INFORMATION);
                }
            } catch (Exception e) {
                Main.showAlert("Error", "Failed to load results: " + e.getMessage(), 
                             javafx.scene.control.Alert.AlertType.ERROR);
                e.printStackTrace();
            }
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
