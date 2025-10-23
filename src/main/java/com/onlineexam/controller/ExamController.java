package com.onlineexam.controller;

import com.onlineexam.Main;
import com.onlineexam.dao.QuestionDAO;
import com.onlineexam.dao.ResultDAO;
import com.onlineexam.model.Question;
import com.onlineexam.model.Result;
import com.onlineexam.model.User;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Controller for the Exam view
 * Handles exam functionality including timer, navigation, and submission
 */
public class ExamController implements Initializable {
    
    @FXML private Label timerLabel;
    @FXML private Label questionCounterLabel;
    @FXML private Label questionText;
    @FXML private RadioButton optionA;
    @FXML private RadioButton optionB;
    @FXML private RadioButton optionC;
    @FXML private RadioButton optionD;
    @FXML private ToggleGroup answerGroup;
    @FXML private Button previousButton;
    @FXML private Button nextButton;
    @FXML private Button submitButton;
    @FXML private ProgressBar progressBar;
    @FXML private Label progressLabel;
    
    private QuestionDAO questionDAO;
    private ResultDAO resultDAO;
    private User currentUser;
    
    private List<Question> questions;
    private Map<Integer, String> userAnswers;
    private int currentQuestionIndex;
    private int examDurationMinutes = 30; // 30 minutes exam
    private int timeRemainingSeconds;
    private Timeline timer;
    private LocalDateTime examStartTime;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        questionDAO = new QuestionDAO();
        resultDAO = new ResultDAO();
        currentUser = Main.getCurrentUser();
        
        userAnswers = new HashMap<>();
        examStartTime = LocalDateTime.now();
        
        setupExam();
        startTimer();
    }
    
    /**
     * Setup the exam by loading questions
     */
    private void setupExam() {
        try {
            // Load 10 random questions for the exam
            questions = questionDAO.getRandomQuestions(10);
            
            if (questions.isEmpty()) {
                Main.showAlert("No Questions", "No questions available for the exam. Please contact administrator.", 
                             Alert.AlertType.ERROR);
                Main.showStudentDashboard();
                return;
            }
            
            currentQuestionIndex = 0;
            timeRemainingSeconds = examDurationMinutes * 60;
            
            updateQuestionDisplay();
            updateNavigationButtons();
            updateProgress();
            
        } catch (Exception e) {
            Main.showAlert("Error", "Failed to load exam questions: " + e.getMessage(), 
                         Alert.AlertType.ERROR);
            e.printStackTrace();
            Main.showStudentDashboard();
        }
    }
    
    /**
     * Start the exam timer
     */
    private void startTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimer()));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }
    
    /**
     * Update the timer display
     */
    private void updateTimer() {
        timeRemainingSeconds--;
        
        int minutes = timeRemainingSeconds / 60;
        int seconds = timeRemainingSeconds % 60;
        
        timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
        
        // Change color when time is running low
        if (timeRemainingSeconds <= 300) { // 5 minutes
            timerLabel.setStyle("-fx-background-color: #e74c3c; -fx-background-radius: 5; -fx-padding: 5 10; -fx-text-fill: white;");
        }
        
        if (timeRemainingSeconds <= 0) {
            timer.stop();
            autoSubmitExam();
        }
    }
    
    /**
     * Update the question display
     */
    private void updateQuestionDisplay() {
        if (currentQuestionIndex >= 0 && currentQuestionIndex < questions.size()) {
            Question question = questions.get(currentQuestionIndex);
            
            questionText.setText(question.getQuestionText());
            optionA.setText("A. " + question.getOptionA());
            optionB.setText("B. " + question.getOptionB());
            optionC.setText("C. " + question.getOptionC());
            optionD.setText("D. " + question.getOptionD());
            
            // Clear selection
            answerGroup.selectToggle(null);
            
            // Restore previous answer if exists
            String previousAnswer = userAnswers.get(question.getId());
            if (previousAnswer != null) {
                switch (previousAnswer) {
                    case "A": optionA.setSelected(true); break;
                    case "B": optionB.setSelected(true); break;
                    case "C": optionC.setSelected(true); break;
                    case "D": optionD.setSelected(true); break;
                }
            }
            
            questionCounterLabel.setText(String.format("Question %d of %d", 
                currentQuestionIndex + 1, questions.size()));
        }
    }
    
    /**
     * Update navigation buttons state
     */
    private void updateNavigationButtons() {
        previousButton.setDisable(currentQuestionIndex == 0);
        nextButton.setDisable(currentQuestionIndex == questions.size() - 1);
    }
    
    /**
     * Update progress bar and label
     */
    private void updateProgress() {
        double progress = (double) (currentQuestionIndex + 1) / questions.size();
        progressBar.setProgress(progress);
        progressLabel.setText(String.format("%d/%d", currentQuestionIndex + 1, questions.size()));
    }
    
    /**
     * Handle previous button click
     */
    @FXML
    private void handlePrevious() {
        saveCurrentAnswer();
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            updateQuestionDisplay();
            updateNavigationButtons();
            updateProgress();
        }
    }
    
    /**
     * Handle next button click
     */
    @FXML
    private void handleNext() {
        saveCurrentAnswer();
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            updateQuestionDisplay();
            updateNavigationButtons();
            updateProgress();
        }
    }
    
    /**
     * Handle submit button click
     */
    @FXML
    private void handleSubmit() {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Submit Exam");
        confirmAlert.setHeaderText("Are you sure you want to submit the exam?");
        confirmAlert.setContentText("This action cannot be undone.");
        
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            saveCurrentAnswer();
            submitExam();
        }
    }
    
    /**
     * Save the current answer
     */
    private void saveCurrentAnswer() {
        if (currentQuestionIndex >= 0 && currentQuestionIndex < questions.size()) {
            Question question = questions.get(currentQuestionIndex);
            RadioButton selectedOption = (RadioButton) answerGroup.getSelectedToggle();
            
            if (selectedOption != null) {
                String answer = selectedOption.getText().substring(0, 1); // Get A, B, C, or D
                userAnswers.put(question.getId(), answer);
            }
        }
    }
    
    /**
     * Auto-submit exam when time runs out
     */
    private void autoSubmitExam() {
        Alert timeUpAlert = new Alert(Alert.AlertType.WARNING);
        timeUpAlert.setTitle("Time's Up!");
        timeUpAlert.setHeaderText("Exam time has expired");
        timeUpAlert.setContentText("Your exam will be automatically submitted.");
        timeUpAlert.showAndWait();
        
        submitExam();
    }
    
    /**
     * Submit the exam and calculate results
     */
    private void submitExam() {
        try {
            int correctAnswers = 0;
            int totalQuestions = questions.size();
            
            // Calculate score
            for (Question question : questions) {
                String userAnswer = userAnswers.get(question.getId());
                if (userAnswer != null && question.isCorrectAnswer(userAnswer)) {
                    correctAnswers++;
                }
            }
            
            // Calculate time taken
            LocalDateTime examEndTime = LocalDateTime.now();
            int timeTakenMinutes = (int) java.time.Duration.between(examStartTime, examEndTime).toMinutes();
            
            // Create result
            Result result = new Result(
                1, // examId - hardcoded for now, should be dynamic
                currentUser.getId(),
                currentUser.getUsername(),
                totalQuestions,
                correctAnswers,
                examEndTime,
                timeTakenMinutes
            );
            
            // Save result to database
            if (resultDAO.saveResult(result)) {
                // Stop timer
                if (timer != null) {
                    timer.stop();
                }
                
                // Show result
                Main.setCurrentResult(result);
                Main.showResultView();
            } else {
                Main.showAlert("Error", "Failed to save exam result", Alert.AlertType.ERROR);
                Main.showStudentDashboard();
            }
            
        } catch (Exception e) {
            Main.showAlert("Error", "Failed to submit exam: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
            Main.showStudentDashboard();
        }
    }
}
