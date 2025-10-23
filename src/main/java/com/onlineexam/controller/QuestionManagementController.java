package com.onlineexam.controller;

import com.onlineexam.Main;
import com.onlineexam.dao.QuestionDAO;
import com.onlineexam.model.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Question Management view
 * Handles CRUD operations for exam questions
 */
public class QuestionManagementController implements Initializable {
    
    @FXML private TableView<Question> questionsTable;
    @FXML private TableColumn<Question, Integer> idColumn;
    @FXML private TableColumn<Question, String> questionColumn;
    @FXML private TableColumn<Question, String> categoryColumn;
    @FXML private TableColumn<Question, String> difficultyColumn;
    @FXML private TableColumn<Question, String> actionsColumn;
    
    @FXML private TextArea questionTextArea;
    @FXML private TextField categoryField;
    @FXML private ComboBox<Integer> difficultyComboBox;
    @FXML private TextField optionAField;
    @FXML private TextField optionBField;
    @FXML private TextField optionCField;
    @FXML private TextField optionDField;
    @FXML private ComboBox<String> correctAnswerComboBox;
    
    @FXML private Button addQuestionButton;
    @FXML private Button saveButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button clearButton;
    @FXML private Button backButton;
    @FXML private Label statusLabel;
    
    private QuestionDAO questionDAO;
    private ObservableList<Question> questionsList;
    private Question selectedQuestion;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        questionDAO = new QuestionDAO();
        questionsList = FXCollections.observableArrayList();
        
        setupTable();
        setupComboBoxes();
        loadQuestions();
        setupTableSelection();
    }
    
    /**
     * Setup the questions table
     */
    private void setupTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("questionText"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        difficultyColumn.setCellValueFactory(cellData -> {
            Question question = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(question.getDifficultyString());
        });
        
        // Setup actions column
        actionsColumn.setCellFactory(column -> new TableCell<Question, String>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            
            {
                editButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-background-radius: 3;");
                deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 3;");
                
                editButton.setOnAction(e -> {
                    Question question = getTableView().getItems().get(getIndex());
                    selectQuestion(question);
                });
                
                deleteButton.setOnAction(e -> {
                    Question question = getTableView().getItems().get(getIndex());
                    deleteQuestion(question);
                });
            }
            
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new javafx.scene.layout.HBox(5, editButton, deleteButton));
                }
            }
        });
        
        questionsTable.setItems(questionsList);
    }
    
    /**
     * Setup combo boxes
     */
    private void setupComboBoxes() {
        // Difficulty levels
        difficultyComboBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));
        difficultyComboBox.setValue(3); // Default to medium difficulty
        
        // Correct answer options
        correctAnswerComboBox.setItems(FXCollections.observableArrayList("A", "B", "C", "D"));
        correctAnswerComboBox.setValue("A"); // Default to option A
    }
    
    /**
     * Setup table selection listener
     */
    private void setupTableSelection() {
        questionsTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    selectQuestion(newSelection);
                }
            });
    }
    
    /**
     * Load all questions from database
     */
    private void loadQuestions() {
        try {
            List<Question> questions = questionDAO.getAllQuestions();
            questionsList.clear();
            questionsList.addAll(questions);
        } catch (Exception e) {
            showStatus("Error loading questions: " + e.getMessage(), false);
            e.printStackTrace();
        }
    }
    
    /**
     * Select a question for editing
     */
    private void selectQuestion(Question question) {
        selectedQuestion = question;
        
        questionTextArea.setText(question.getQuestionText());
        categoryField.setText(question.getCategory());
        difficultyComboBox.setValue(question.getDifficulty());
        optionAField.setText(question.getOptionA());
        optionBField.setText(question.getOptionB());
        optionCField.setText(question.getOptionC());
        optionDField.setText(question.getOptionD());
        correctAnswerComboBox.setValue(question.getCorrectOption());
        
        updateButton.setDisable(false);
        deleteButton.setDisable(false);
        saveButton.setDisable(true);
    }
    
    /**
     * Handle add question button click
     */
    @FXML
    private void handleAddQuestion() {
        clearFields();
        selectedQuestion = null;
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
        saveButton.setDisable(false);
    }
    
    /**
     * Handle save button click
     */
    @FXML
    private void handleSave() {
        if (validateInput()) {
            Question newQuestion = createQuestionFromFields();
            
            if (questionDAO.addQuestion(newQuestion)) {
                showStatus("Question added successfully!", true);
                loadQuestions();
                clearFields();
            } else {
                showStatus("Failed to add question", false);
            }
        }
    }
    
    /**
     * Handle update button click
     */
    @FXML
    private void handleUpdate() {
        if (selectedQuestion != null && validateInput()) {
            updateQuestionFromFields(selectedQuestion);
            
            if (questionDAO.updateQuestion(selectedQuestion)) {
                showStatus("Question updated successfully!", true);
                loadQuestions();
                clearFields();
            } else {
                showStatus("Failed to update question", false);
            }
        }
    }
    
    /**
     * Handle delete button click
     */
    @FXML
    private void handleDelete() {
        if (selectedQuestion != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Delete Question");
            confirmAlert.setHeaderText("Are you sure you want to delete this question?");
            confirmAlert.setContentText("This action cannot be undone.");
            
            java.util.Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                deleteQuestion(selectedQuestion);
            }
        }
    }
    
    /**
     * Delete a question
     */
    private void deleteQuestion(Question question) {
        if (questionDAO.deleteQuestion(question.getId())) {
            showStatus("Question deleted successfully!", true);
            loadQuestions();
            clearFields();
        } else {
            showStatus("Failed to delete question", false);
        }
    }
    
    /**
     * Handle clear button click
     */
    @FXML
    private void handleClear() {
        clearFields();
    }
    
    /**
     * Handle back button click
     */
    @FXML
    private void handleBack() {
        Main.showAdminDashboard();
    }
    
    /**
     * Validate input fields
     */
    private boolean validateInput() {
        if (questionTextArea.getText().trim().isEmpty()) {
            showStatus("Please enter question text", false);
            return false;
        }
        
        if (categoryField.getText().trim().isEmpty()) {
            showStatus("Please enter category", false);
            return false;
        }
        
        if (optionAField.getText().trim().isEmpty() ||
            optionBField.getText().trim().isEmpty() ||
            optionCField.getText().trim().isEmpty() ||
            optionDField.getText().trim().isEmpty()) {
            showStatus("Please enter all four options", false);
            return false;
        }
        
        if (correctAnswerComboBox.getValue() == null) {
            showStatus("Please select correct answer", false);
            return false;
        }
        
        return true;
    }
    
    /**
     * Create question object from form fields
     */
    private Question createQuestionFromFields() {
        return new Question(
            questionTextArea.getText().trim(),
            optionAField.getText().trim(),
            optionBField.getText().trim(),
            optionCField.getText().trim(),
            optionDField.getText().trim(),
            correctAnswerComboBox.getValue(),
            categoryField.getText().trim(),
            difficultyComboBox.getValue()
        );
    }
    
    /**
     * Update question object from form fields
     */
    private void updateQuestionFromFields(Question question) {
        question.setQuestionText(questionTextArea.getText().trim());
        question.setCategory(categoryField.getText().trim());
        question.setDifficulty(difficultyComboBox.getValue());
        question.setOptionA(optionAField.getText().trim());
        question.setOptionB(optionBField.getText().trim());
        question.setOptionC(optionCField.getText().trim());
        question.setOptionD(optionDField.getText().trim());
        question.setCorrectOption(correctAnswerComboBox.getValue());
    }
    
    /**
     * Clear all input fields
     */
    private void clearFields() {
        questionTextArea.clear();
        categoryField.clear();
        difficultyComboBox.setValue(3);
        optionAField.clear();
        optionBField.clear();
        optionCField.clear();
        optionDField.clear();
        correctAnswerComboBox.setValue("A");
        
        selectedQuestion = null;
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
        saveButton.setDisable(false);
        statusLabel.setText("");
    }
    
    /**
     * Show status message
     */
    private void showStatus(String message, boolean isSuccess) {
        statusLabel.setText(message);
        statusLabel.setStyle(isSuccess ? "-fx-text-fill: #27ae60;" : "-fx-text-fill: #e74c3c;");
    }
}
