package com.onlineexam;

import com.onlineexam.model.Result;
import com.onlineexam.model.User;
import com.onlineexam.util.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main application class for the Online Examination System
 * Handles scene management and application lifecycle
 */
public class Main extends Application {
    
    private static Stage primaryStage;
    private static User currentUser;
    private static Result currentResult;
    
    // Scene references
    private static Scene loginScene;
    private static Scene registerScene;
    private static Scene studentDashboardScene;
    private static Scene adminDashboardScene;
    private static Scene examScene;
    private static Scene resultScene;
    private static Scene questionManagementScene;
    
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        
        // Set application title and icon
        primaryStage.setTitle("Online Examination System");
        try {
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
        } catch (Exception e) {
            // Icon not found, continue without it
        }
        
        // Check database connection
        if (!DatabaseConnection.isDataSourceAvailable()) {
            showAlert("Database Error", 
                     "Cannot connect to database. Please check your database configuration and ensure MySQL is running.", 
                     Alert.AlertType.ERROR);
            System.exit(1);
        }
        
        // Load and show login scene
        showLoginView();
        
        // Set window properties
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.centerOnScreen();
        primaryStage.show();
        
        // Handle window close event
        primaryStage.setOnCloseRequest(event -> {
            DatabaseConnection.closeDataSource();
        });
    }
    
    /**
     * Show login view
     */
    public static void showLoginView() {
        try {
            if (loginScene == null) {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/LoginView.fxml"));
                loginScene = new Scene(loader.load(), 800, 600);
            }
            primaryStage.setScene(loginScene);
            primaryStage.setTitle("Online Examination System - Login");
        } catch (IOException e) {
            showAlert("Error", "Failed to load login view: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    /**
     * Show registration view
     */
    public static void showRegisterView() {
        try {
            if (registerScene == null) {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/RegisterView.fxml"));
                registerScene = new Scene(loader.load(), 800, 600);
            }
            primaryStage.setScene(registerScene);
            primaryStage.setTitle("Online Examination System - Registration");
        } catch (IOException e) {
            showAlert("Error", "Failed to load registration view: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    /**
     * Show student dashboard
     */
    public static void showStudentDashboard() {
        try {
            if (studentDashboardScene == null) {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/StudentDashboardView.fxml"));
                studentDashboardScene = new Scene(loader.load(), 1000, 700);
            }
            primaryStage.setScene(studentDashboardScene);
            primaryStage.setTitle("Online Examination System - Student Dashboard");
        } catch (IOException e) {
            showAlert("Error", "Failed to load student dashboard: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    /**
     * Show admin dashboard
     */
    public static void showAdminDashboard() {
        try {
            if (adminDashboardScene == null) {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/AdminDashboardView.fxml"));
                adminDashboardScene = new Scene(loader.load(), 1000, 700);
            }
            primaryStage.setScene(adminDashboardScene);
            primaryStage.setTitle("Online Examination System - Admin Dashboard");
        } catch (IOException e) {
            showAlert("Error", "Failed to load admin dashboard: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    /**
     * Show exam view
     */
    public static void showExamView() {
        try {
            if (examScene == null) {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/ExamView.fxml"));
                examScene = new Scene(loader.load(), 1000, 700);
            }
            primaryStage.setScene(examScene);
            primaryStage.setTitle("Online Examination System - Exam");
        } catch (IOException e) {
            showAlert("Error", "Failed to load exam view: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    /**
     * Show result view
     */
    public static void showResultView() {
        try {
            if (resultScene == null) {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/ResultView.fxml"));
                resultScene = new Scene(loader.load(), 800, 600);
            }
            primaryStage.setScene(resultScene);
            primaryStage.setTitle("Online Examination System - Results");
        } catch (IOException e) {
            showAlert("Error", "Failed to load result view: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    /**
     * Show question management view
     */
    public static void showQuestionManagementView() {
        try {
            if (questionManagementScene == null) {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/QuestionManagementView.fxml"));
                questionManagementScene = new Scene(loader.load(), 1200, 800);
            }
            primaryStage.setScene(questionManagementScene);
            primaryStage.setTitle("Online Examination System - Question Management");
        } catch (IOException e) {
            showAlert("Error", "Failed to load question management view: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    /**
     * Show alert dialog
     * @param title the alert title
     * @param message the alert message
     * @param alertType the alert type
     */
    public static void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Get current user
     * @return the current user
     */
    public static User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Set current user
     * @param user the user to set as current
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
    }
    
    /**
     * Get current result
     * @return the current result
     */
    public static Result getCurrentResult() {
        return currentResult;
    }
    
    /**
     * Set current result
     * @param result the result to set as current
     */
    public static void setCurrentResult(Result result) {
        currentResult = result;
    }
    
    /**
     * Get primary stage
     * @return the primary stage
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    
    /**
     * Main method
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
