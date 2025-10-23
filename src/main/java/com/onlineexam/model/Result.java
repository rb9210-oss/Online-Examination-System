package com.onlineexam.model;

import java.time.LocalDateTime;

/**
 * Result model class representing an exam result
 * Contains user ID, score, and exam date
 */
public class Result {
    private int id;
    private int examId;
    private int userId;
    private String username; // For display purposes
    private int totalQuestions;
    private int correctAnswers;
    private int score;
    private LocalDateTime examDate;
    private int timeTaken; // in minutes
    private String status; // "PASSED", "FAILED", "INCOMPLETE"

    /**
     * Default constructor
     */
    public Result() {}

    /**
     * Constructor with all fields
     * @param id result ID
     * @param userId user ID
     * @param username username for display
     * @param totalQuestions total number of questions
     * @param correctAnswers number of correct answers
     * @param score calculated score
     * @param examDate date and time of exam
     * @param timeTaken time taken in minutes
     * @param status exam status
     */
    public Result(int id, int userId, String username, int totalQuestions, 
                 int correctAnswers, int score, LocalDateTime examDate, 
                 int timeTaken, String status) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.score = score;
        this.examDate = examDate;
        this.timeTaken = timeTaken;
        this.status = status;
    }

    /**
     * Constructor for new result (without ID)
     * @param examId exam ID
     * @param userId user ID
     * @param username username for display
     * @param totalQuestions total number of questions
     * @param correctAnswers number of correct answers
     * @param examDate date and time of exam
     * @param timeTaken time taken in minutes
     */
    public Result(int examId, int userId, String username, int totalQuestions,
                 int correctAnswers, LocalDateTime examDate, int timeTaken) {
        this.examId = examId;
        this.userId = userId;
        this.username = username;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.examDate = examDate;
        this.timeTaken = timeTaken;
        calculateScore();
        determineStatus();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LocalDateTime getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDateTime examDate) {
        this.examDate = examDate;
    }

    public int getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(int timeTaken) {
        this.timeTaken = timeTaken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Calculate score percentage
     */
    public void calculateScore() {
        if (totalQuestions > 0) {
            this.score = (correctAnswers * 100) / totalQuestions;
        } else {
            this.score = 0;
        }
    }

    /**
     * Determine exam status based on score
     */
    public void determineStatus() {
        if (score >= 60) {
            this.status = "PASSED";
        } else if (score >= 0) {
            this.status = "FAILED";
        } else {
            this.status = "INCOMPLETE";
        }
    }

    /**
     * Get percentage score as string
     * @return formatted percentage string
     */
    public String getScorePercentage() {
        return score + "%";
    }

    /**
     * Get time taken as formatted string
     * @return formatted time string
     */
    public String getTimeTakenString() {
        int hours = timeTaken / 60;
        int minutes = timeTaken % 60;
        
        if (hours > 0) {
            return String.format("%d hours %d minutes", hours, minutes);
        } else {
            return String.format("%d minutes", minutes);
        }
    }

    /**
     * Get exam date as formatted string
     * @return formatted date string
     */
    public String getExamDateString() {
        return examDate.toString().replace("T", " ");
    }

    /**
     * Check if exam was passed
     * @return true if passed, false otherwise
     */
    public boolean isPassed() {
        return "PASSED".equals(status);
    }

    /**
     * Get grade based on score
     * @return grade letter
     */
    public String getGrade() {
        if (score >= 90) return "A";
        if (score >= 80) return "B";
        if (score >= 70) return "C";
        if (score >= 60) return "D";
        return "F";
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", userId=" + userId +
                ", username='" + username + '\'' +
                ", totalQuestions=" + totalQuestions +
                ", correctAnswers=" + correctAnswers +
                ", score=" + score +
                ", examDate=" + examDate +
                ", timeTaken=" + timeTaken +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Result result = (Result) obj;
        return id == result.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
