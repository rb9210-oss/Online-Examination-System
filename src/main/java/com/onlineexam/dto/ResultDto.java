package com.onlineexam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultDto {
    
    private Long id;
    private Long examId;
    private String examTitle;
    private Long studentId;
    private String studentName;
    private Integer score;
    private Integer totalQuestions;
    private Integer correctAnswers;
    private LocalDateTime submittedAt;
    private String grade;
}


