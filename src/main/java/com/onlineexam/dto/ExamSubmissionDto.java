package com.onlineexam.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ExamSubmissionDto {
    
    @NotNull(message = "Exam ID is required")
    private Long examId;
    
    @Valid
    @NotNull(message = "Answers are required")
    private List<AnswerDto> answers;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerDto {
        
        @NotNull(message = "Question ID is required")
        private Long questionId;
        
        private Integer selectedAnswer; // Index of selected option (0-based)
    }
}


