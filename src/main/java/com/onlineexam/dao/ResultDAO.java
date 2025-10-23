package com.onlineexam.dao;

import com.onlineexam.model.Result;
import com.onlineexam.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Result operations
 * Handles all database operations related to exam results
 */
public class ResultDAO {
    
    /**
     * Save exam result
     * @param result the result object to save
     * @return true if save successful, false otherwise
     */
    public boolean saveResult(Result result) {
        String sql = "INSERT INTO results (exam_id, student_id, username, total_questions, correct_answers, score, submitted_at, time_taken, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, result.getExamId());
            stmt.setInt(2, result.getUserId());
            stmt.setString(3, result.getUsername());
            stmt.setInt(4, result.getTotalQuestions());
            stmt.setInt(5, result.getCorrectAnswers());
            stmt.setInt(6, result.getScore());
            stmt.setTimestamp(7, Timestamp.valueOf(result.getExamDate()));
            stmt.setInt(8, result.getTimeTaken());
            stmt.setString(9, result.getStatus());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    result.setId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error saving result: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
    
    /**
     * Get result by ID
     * @param resultId the result ID
     * @return Result object if found, null otherwise
     */
    public Result getResultById(int resultId) {
        String sql = "SELECT * FROM results WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, resultId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Result(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getInt("total_questions"),
                    rs.getInt("correct_answers"),
                    rs.getInt("score"),
                    rs.getTimestamp("exam_date").toLocalDateTime(),
                    rs.getInt("time_taken"),
                    rs.getString("status")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting result by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get all results for a specific user
     * @param userId the user ID
     * @return List of results for the user
     */
    public List<Result> getResultsByUserId(int userId) {
        List<Result> results = new ArrayList<>();
        String sql = "SELECT * FROM results WHERE student_id = ? ORDER BY submitted_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                results.add(new Result(
                    rs.getInt("id"),
                    rs.getInt("student_id"),
                    rs.getString("username"),
                    rs.getInt("total_questions"),
                    rs.getInt("correct_answers"),
                    rs.getInt("score"),
                    rs.getTimestamp("submitted_at").toLocalDateTime(),
                    rs.getInt("time_taken"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting results by user ID: " + e.getMessage());
            e.printStackTrace();
        }

        return results;
    }
    
    /**
     * Get all results
     * @return List of all results
     */
    public List<Result> getAllResults() {
        List<Result> results = new ArrayList<>();
        String sql = "SELECT * FROM results ORDER BY exam_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                results.add(new Result(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getInt("total_questions"),
                    rs.getInt("correct_answers"),
                    rs.getInt("score"),
                    rs.getTimestamp("exam_date").toLocalDateTime(),
                    rs.getInt("time_taken"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all results: " + e.getMessage());
            e.printStackTrace();
        }
        
        return results;
    }
    
    /**
     * Get results by status
     * @param status the result status (PASSED, FAILED, INCOMPLETE)
     * @return List of results with the specified status
     */
    public List<Result> getResultsByStatus(String status) {
        List<Result> results = new ArrayList<>();
        String sql = "SELECT * FROM results WHERE status = ? ORDER BY exam_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                results.add(new Result(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getInt("total_questions"),
                    rs.getInt("correct_answers"),
                    rs.getInt("score"),
                    rs.getTimestamp("exam_date").toLocalDateTime(),
                    rs.getInt("time_taken"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting results by status: " + e.getMessage());
            e.printStackTrace();
        }
        
        return results;
    }
    
    /**
     * Get best result for a user
     * @param userId the user ID
     * @return Best result for the user, null if no results found
     */
    public Result getBestResultByUserId(int userId) {
        String sql = "SELECT * FROM results WHERE user_id = ? ORDER BY score DESC, exam_date DESC LIMIT 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Result(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getInt("total_questions"),
                    rs.getInt("correct_answers"),
                    rs.getInt("score"),
                    rs.getTimestamp("exam_date").toLocalDateTime(),
                    rs.getInt("time_taken"),
                    rs.getString("status")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting best result by user ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get latest result for a user
     * @param userId the user ID
     * @return Latest result for the user, null if no results found
     */
    public Result getLatestResultByUserId(int userId) {
        String sql = "SELECT * FROM results WHERE user_id = ? ORDER BY exam_date DESC LIMIT 1";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Result(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getInt("total_questions"),
                    rs.getInt("correct_answers"),
                    rs.getInt("score"),
                    rs.getTimestamp("exam_date").toLocalDateTime(),
                    rs.getInt("time_taken"),
                    rs.getString("status")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error getting latest result by user ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get statistics for all results
     * @return Array containing [totalExams, passedExams, failedExams, averageScore]
     */
    public double[] getResultStatistics() {
        String sql = "SELECT COUNT(*) as total, " +
                    "SUM(CASE WHEN status = 'PASSED' THEN 1 ELSE 0 END) as passed, " +
                    "SUM(CASE WHEN status = 'FAILED' THEN 1 ELSE 0 END) as failed, " +
                    "AVG(score) as avgScore " +
                    "FROM results";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return new double[]{
                    rs.getInt("total"),
                    rs.getInt("passed"),
                    rs.getInt("failed"),
                    rs.getDouble("avgScore")
                };
            }
        } catch (SQLException e) {
            System.err.println("Error getting result statistics: " + e.getMessage());
            e.printStackTrace();
        }
        
        return new double[]{0, 0, 0, 0};
    }
    
    /**
     * Get top performers
     * @param limit number of top performers to return
     * @return List of top performing results
     */
    public List<Result> getTopPerformers(int limit) {
        List<Result> results = new ArrayList<>();
        String sql = "SELECT * FROM results ORDER BY score DESC, exam_date DESC LIMIT ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                results.add(new Result(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getInt("total_questions"),
                    rs.getInt("correct_answers"),
                    rs.getInt("score"),
                    rs.getTimestamp("exam_date").toLocalDateTime(),
                    rs.getInt("time_taken"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting top performers: " + e.getMessage());
            e.printStackTrace();
        }
        
        return results;
    }
    
    /**
     * Delete result
     * @param resultId the result ID to delete
     * @return true if deletion successful, false otherwise
     */
    public boolean deleteResult(int resultId) {
        String sql = "DELETE FROM results WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, resultId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting result: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
}
