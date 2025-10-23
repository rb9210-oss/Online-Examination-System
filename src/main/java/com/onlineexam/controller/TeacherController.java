package com.onlineexam.controller;

import com.onlineexam.dto.ApiResponse;
import com.onlineexam.dto.ExamDto;
import com.onlineexam.dto.QuestionDto;
import com.onlineexam.dto.ResultDto;
import com.onlineexam.service.ExamService;
import com.onlineexam.service.QuestionService;
import com.onlineexam.service.ResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teachers")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('TEACHER', 'ADMIN')")
@Tag(name = "Teacher Management", description = "Teacher management APIs")
public class TeacherController {
    
    private final ExamService examService;
    private final QuestionService questionService;
    private final ResultService resultService;
    
    // Exam Management
    @PostMapping("/exams")
    @Operation(summary = "Create exam", description = "Create a new exam")
    public ResponseEntity<ApiResponse<ExamDto>> createExam(@Valid @RequestBody ExamDto examDto) {
        try {
            ExamDto exam = examService.createExam(examDto);
            return ResponseEntity.ok(ApiResponse.success("Exam created successfully", exam));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to create exam: " + e.getMessage()));
        }
    }
    
    @GetMapping("/exams")
    @Operation(summary = "Get teacher's exams", description = "Retrieve all exams created by the teacher")
    public ResponseEntity<ApiResponse<Page<ExamDto>>> getTeacherExams(Pageable pageable) {
        try {
            Page<ExamDto> exams = examService.getExamsByTeacher(pageable);
            return ResponseEntity.ok(ApiResponse.success(exams));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to get exams: " + e.getMessage()));
        }
    }
    
    @GetMapping("/exams/{examId}")
    @Operation(summary = "Get exam details", description = "Retrieve exam details by ID")
    public ResponseEntity<ApiResponse<ExamDto>> getExamById(@PathVariable Long examId) {
        try {
            ExamDto exam = examService.getExamById(examId);
            return ResponseEntity.ok(ApiResponse.success(exam));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to get exam: " + e.getMessage()));
        }
    }
    
    @PutMapping("/exams/{examId}")
    @Operation(summary = "Update exam", description = "Update an exam")
    public ResponseEntity<ApiResponse<ExamDto>> updateExam(@PathVariable Long examId, 
                                                          @Valid @RequestBody ExamDto examDto) {
        try {
            ExamDto exam = examService.updateExam(examId, examDto);
            return ResponseEntity.ok(ApiResponse.success("Exam updated successfully", exam));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update exam: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/exams/{examId}")
    @Operation(summary = "Delete exam", description = "Delete an exam")
    public ResponseEntity<ApiResponse<Void>> deleteExam(@PathVariable Long examId) {
        try {
            examService.deleteExam(examId);
            return ResponseEntity.ok(ApiResponse.success("Exam deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to delete exam: " + e.getMessage()));
        }
    }
    
    // Question Management
    @PostMapping("/exams/{examId}/questions")
    @Operation(summary = "Add question to exam", description = "Add a question to an exam")
    public ResponseEntity<ApiResponse<QuestionDto>> addQuestion(@PathVariable Long examId, 
                                                               @Valid @RequestBody QuestionDto questionDto) {
        try {
            QuestionDto question = questionService.createQuestion(examId, questionDto);
            return ResponseEntity.ok(ApiResponse.success("Question added successfully", question));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to add question: " + e.getMessage()));
        }
    }
    
    @PutMapping("/questions/{questionId}")
    @Operation(summary = "Update question", description = "Update a question")
    public ResponseEntity<ApiResponse<QuestionDto>> updateQuestion(@PathVariable Long questionId, 
                                                                  @Valid @RequestBody QuestionDto questionDto) {
        try {
            QuestionDto question = questionService.updateQuestion(questionId, questionDto);
            return ResponseEntity.ok(ApiResponse.success("Question updated successfully", question));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to update question: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/questions/{questionId}")
    @Operation(summary = "Delete question", description = "Delete a question")
    public ResponseEntity<ApiResponse<Void>> deleteQuestion(@PathVariable Long questionId) {
        try {
            questionService.deleteQuestion(questionId);
            return ResponseEntity.ok(ApiResponse.success("Question deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to delete question: " + e.getMessage()));
        }
    }
    
    // Result Viewing
    @GetMapping("/exams/{examId}/results")
    @Operation(summary = "Get exam results", description = "View results of an exam")
    public ResponseEntity<ApiResponse<List<ResultDto>>> getExamResults(@PathVariable Long examId) {
        try {
            List<ResultDto> results = resultService.getResultsByExam(examId);
            return ResponseEntity.ok(ApiResponse.success(results));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to get exam results: " + e.getMessage()));
        }
    }
}


