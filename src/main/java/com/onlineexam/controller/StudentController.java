package com.onlineexam.controller;

import com.onlineexam.dto.ApiResponse;
import com.onlineexam.dto.ExamDto;
import com.onlineexam.dto.ExamSubmissionDto;
import com.onlineexam.dto.ResultDto;
import com.onlineexam.service.ExamService;
import com.onlineexam.service.ExamSubmissionService;
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
@RequestMapping("/students")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
@Tag(name = "Student Management", description = "Student management APIs")
public class StudentController {
    
    private final ExamService examService;
    private final ExamSubmissionService examSubmissionService;
    private final ResultService resultService;
    
    // Exam Access
    @GetMapping("/exams")
    @Operation(summary = "Get available exams", description = "List all available exams for students")
    public ResponseEntity<ApiResponse<Page<ExamDto>>> getAvailableExams(Pageable pageable) {
        try {
            Page<ExamDto> exams = examService.getAllExams(pageable);
            return ResponseEntity.ok(ApiResponse.success(exams));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to get exams: " + e.getMessage()));
        }
    }
    
    @GetMapping("/exams/{examId}")
    @Operation(summary = "Get exam details", description = "Get details of a specific exam")
    public ResponseEntity<ApiResponse<ExamDto>> getExamDetails(@PathVariable Long examId) {
        try {
            ExamDto exam = examService.getExamById(examId);
            return ResponseEntity.ok(ApiResponse.success(exam));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to get exam details: " + e.getMessage()));
        }
    }
    
    // Taking Exams
    @PostMapping("/exams/{examId}/submit")
    @Operation(summary = "Submit exam", description = "Submit answers for an exam")
    public ResponseEntity<ApiResponse<ResultDto>> submitExam(@PathVariable Long examId, 
                                                           @Valid @RequestBody ExamSubmissionDto submissionDto) {
        try {
            submissionDto.setExamId(examId);
            ResultDto result = examSubmissionService.submitExam(submissionDto);
            return ResponseEntity.ok(ApiResponse.success("Exam submitted successfully", result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to submit exam: " + e.getMessage()));
        }
    }
    
    // Results
    @GetMapping("/results")
    @Operation(summary = "Get student results", description = "List all results for the student")
    public ResponseEntity<ApiResponse<List<ResultDto>>> getStudentResults() {
        try {
            List<ResultDto> results = resultService.getResultsByStudent();
            return ResponseEntity.ok(ApiResponse.success(results));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to get results: " + e.getMessage()));
        }
    }
    
    @GetMapping("/results/paginated")
    @Operation(summary = "Get student results (paginated)", description = "List all results for the student with pagination")
    public ResponseEntity<ApiResponse<Page<ResultDto>>> getStudentResultsPaginated(Pageable pageable) {
        try {
            Page<ResultDto> results = resultService.getResultsByStudent(pageable);
            return ResponseEntity.ok(ApiResponse.success(results));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to get results: " + e.getMessage()));
        }
    }
    
    @GetMapping("/results/{resultId}")
    @Operation(summary = "Get specific result", description = "View details of a specific result")
    public ResponseEntity<ApiResponse<ResultDto>> getResultById(@PathVariable Long resultId) {
        try {
            ResultDto result = resultService.getResultById(resultId);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to get result: " + e.getMessage()));
        }
    }
}


