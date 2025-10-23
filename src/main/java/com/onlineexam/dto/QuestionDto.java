package com.onlineexam.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class QuestionDto {
    
    private Long id;
    
    @NotBlank(message = "Question text is required")
    private String questionText;
    
    @NotNull(message = "Options are required")
    @Size(min = 2, message = "At least 2 options are required")
    private List<String> options;
    
    @NotNull(message = "Correct answer is required")
    private Integer correctAnswer;
    
    private Long examId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


