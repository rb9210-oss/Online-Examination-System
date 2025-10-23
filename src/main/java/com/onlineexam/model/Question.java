package com.onlineexam.model;

/**
 * Question model class representing an exam question
 * Contains question text, four options, and correct answer
 */
public class Question {
    private int id;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctOption;
    private String category;
    private int difficulty; // 1-5 scale

    /**
     * Default constructor
     */
    public Question() {}

    /**
     * Constructor with all fields
     * @param id question ID
     * @param questionText the question text
     * @param optionA option A
     * @param optionB option B
     * @param optionC option C
     * @param optionD option D
     * @param correctOption correct option (A, B, C, or D)
     * @param category question category
     * @param difficulty difficulty level (1-5)
     */
    public Question(int id, String questionText, String optionA, String optionB, 
                   String optionC, String optionD, String correctOption, 
                   String category, int difficulty) {
        this.id = id;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption;
        this.category = category;
        this.difficulty = difficulty;
    }

    /**
     * Constructor for new question (without ID)
     * @param questionText the question text
     * @param optionA option A
     * @param optionB option B
     * @param optionC option C
     * @param optionD option D
     * @param correctOption correct option (A, B, C, or D)
     * @param category question category
     * @param difficulty difficulty level (1-5)
     */
    public Question(String questionText, String optionA, String optionB, 
                   String optionC, String optionD, String correctOption, 
                   String category, int difficulty) {
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption;
        this.category = category;
        this.difficulty = difficulty;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Get option text by option letter
     * @param option the option letter (A, B, C, or D)
     * @return the option text
     */
    public String getOptionByLetter(String option) {
        switch (option.toUpperCase()) {
            case "A": return optionA;
            case "B": return optionB;
            case "C": return optionC;
            case "D": return optionD;
            default: return null;
        }
    }

    /**
     * Check if the given option is correct
     * @param selectedOption the selected option
     * @return true if correct, false otherwise
     */
    public boolean isCorrectAnswer(String selectedOption) {
        return correctOption != null && correctOption.equalsIgnoreCase(selectedOption);
    }

    /**
     * Get difficulty level as string
     * @return difficulty level description
     */
    public String getDifficultyString() {
        switch (difficulty) {
            case 1: return "Very Easy";
            case 2: return "Easy";
            case 3: return "Medium";
            case 4: return "Hard";
            case 5: return "Very Hard";
            default: return "Unknown";
        }
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", questionText='" + questionText + '\'' +
                ", correctOption='" + correctOption + '\'' +
                ", category='" + category + '\'' +
                ", difficulty=" + difficulty +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Question question = (Question) obj;
        return id == question.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
