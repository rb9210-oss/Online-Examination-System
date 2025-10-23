package com.onlineexam.controller;

import com.onlineexam.Main;
import com.onlineexam.model.Result;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Result view
 * Displays exam results and provides navigation options
 */
public class ResultController implements Initializable {
    
    @FXML private Label scoreText;
    @FXML private Label gradeText;
    @FXML private Label statusText;
    @FXML private Label totalQuestionsText;
    @FXML private Label correctAnswersText;
    @FXML private Label timeTakenText;
    @FXML private Label examDateText;
    @FXML private Label feedbackText;
    @FXML private Button retakeExamButton;
    @FXML private Button backToDashboardButton;
    
    private Result currentResult;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentResult = Main.getCurrentResult();
        
        if (currentResult != null) {
            displayResults();
        } else {
            // If no result available, go back to dashboard
            Main.showStudentDashboard();
        }
    }
    
    /**
     * Display the exam results
     */
    private void displayResults() {
        // Display score
        scoreText.setText(currentResult.getScorePercentage());
        
        // Display grade
        gradeText.setText("Grade: " + currentResult.getGrade());
        
        // Display status with appropriate color
        statusText.setText(currentResult.getStatus());
        if (currentResult.isPassed()) {
            statusText.setStyle("-fx-fill: #27ae60;");
        } else {
            statusText.setStyle("-fx-fill: #e74c3c;");
        }
        
        // Display statistics
        totalQuestionsText.setText(String.valueOf(currentResult.getTotalQuestions()));
        correctAnswersText.setText(String.valueOf(currentResult.getCorrectAnswers()));
        timeTakenText.setText(currentResult.getTimeTakenString());
        examDateText.setText("Exam Date: " + currentResult.getExamDateString());
        
        // Display feedback
        String feedback = generateFeedback(currentResult);
        feedbackText.setText(feedback);
    }
    
    /**
     * Generate feedback based on the result
     * @param result the exam result
     * @return feedback message
     */
    private String generateFeedback(Result result) {
        StringBuilder feedback = new StringBuilder();
        
        if (result.isPassed()) {
            feedback.append("Congratulations! You have passed the exam. ");
            
            if (result.getScore() >= 90) {
                feedback.append("Excellent performance! You have demonstrated mastery of the subject.");
            } else if (result.getScore() >= 80) {
                feedback.append("Great job! You have a strong understanding of the material.");
            } else if (result.getScore() >= 70) {
                feedback.append("Good work! You have a solid grasp of the concepts.");
            } else {
                feedback.append("Well done! You have met the passing requirements.");
            }
        } else {
            feedback.append("Unfortunately, you did not pass this exam. ");
            
            if (result.getScore() >= 50) {
                feedback.append("You're close to passing. Review the material and try again.");
            } else {
                feedback.append("Consider reviewing the study materials more thoroughly before retaking the exam.");
            }
        }
        
        feedback.append("\n\n");
        feedback.append("You answered ").append(result.getCorrectAnswers())
                .append(" out of ").append(result.getTotalQuestions())
                .append(" questions correctly.");
        
        if (result.getTimeTaken() < 15) {
            feedback.append(" You completed the exam quickly, which shows good preparation.");
        } else if (result.getTimeTaken() > 25) {
            feedback.append(" You took your time with the exam, which shows careful consideration.");
        }
        
        return feedback.toString();
    }
    
    /**
     * Handle retake exam button click
     */
    @FXML
    private void handleRetakeExam() {
        Alert confirmAlert = new Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Retake Exam");
        confirmAlert.setHeaderText("Are you sure you want to retake the exam?");
        confirmAlert.setContentText("This will start a new exam with different questions.");
        
        java.util.Optional<javafx.scene.control.ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            Main.showExamView();
        }
    }
    
    /**
     * Handle back to dashboard button click
     */
    @FXML
    private void handleBackToDashboard() {
        Main.showStudentDashboard();
    }
}
