package com.onlineexam.service;

import com.onlineexam.dto.ResultDto;
import com.onlineexam.entity.Exam;
import com.onlineexam.entity.Result;
import com.onlineexam.entity.User;
import com.onlineexam.repository.ExamRepository;
import com.onlineexam.repository.ResultRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResultService {
    
    private final ResultRepository resultRepository;
    private final ExamRepository examRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    
    public ResultDto getResultById(Long resultId) {
        Result result = resultRepository.findById(resultId)
                .orElseThrow(() -> new RuntimeException("Result not found with id: " + resultId));
        
        ResultDto resultDto = modelMapper.map(result, ResultDto.class);
        resultDto.setExamTitle(result.getExam().getTitle());
        resultDto.setStudentName(result.getStudent().getName());
        resultDto.setGrade(calculateGrade(result.getScore()));
        
        return resultDto;
    }
    
    public List<ResultDto> getResultsByStudent() {
        User currentUser = userService.getCurrentUser();
        List<Result> results = resultRepository.findByStudent(currentUser);
        
        return results.stream()
                .map(result -> {
                    ResultDto resultDto = modelMapper.map(result, ResultDto.class);
                    resultDto.setExamTitle(result.getExam().getTitle());
                    resultDto.setStudentName(result.getStudent().getName());
                    resultDto.setGrade(calculateGrade(result.getScore()));
                    return resultDto;
                })
                .collect(Collectors.toList());
    }
    
    public Page<ResultDto> getResultsByStudent(Pageable pageable) {
        User currentUser = userService.getCurrentUser();
        return resultRepository.findByStudentId(currentUser.getId(), pageable)
                .map(result -> {
                    ResultDto resultDto = modelMapper.map(result, ResultDto.class);
                    resultDto.setExamTitle(result.getExam().getTitle());
                    resultDto.setStudentName(result.getStudent().getName());
                    resultDto.setGrade(calculateGrade(result.getScore()));
                    return resultDto;
                });
    }
    
    public List<ResultDto> getResultsByExam(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found with id: " + examId));
        
        User currentUser = userService.getCurrentUser();
        
        // Only the exam creator or admin can view results
        if (!exam.getCreatedBy().getId().equals(currentUser.getId()) && 
            !currentUser.getRole().equals(User.Role.ADMIN)) {
            throw new RuntimeException("You don't have permission to view results for this exam");
        }
        
        List<Result> results = resultRepository.findByExam(exam);
        
        return results.stream()
                .map(result -> {
                    ResultDto resultDto = modelMapper.map(result, ResultDto.class);
                    resultDto.setExamTitle(result.getExam().getTitle());
                    resultDto.setStudentName(result.getStudent().getName());
                    resultDto.setGrade(calculateGrade(result.getScore()));
                    return resultDto;
                })
                .collect(Collectors.toList());
    }
    
    public Page<ResultDto> getAllResults(Pageable pageable) {
        return resultRepository.findAll(pageable)
                .map(result -> {
                    ResultDto resultDto = modelMapper.map(result, ResultDto.class);
                    resultDto.setExamTitle(result.getExam().getTitle());
                    resultDto.setStudentName(result.getStudent().getName());
                    resultDto.setGrade(calculateGrade(result.getScore()));
                    return resultDto;
                });
    }
    
    private String calculateGrade(int score) {
        if (score >= 90) return "A";
        if (score >= 80) return "B";
        if (score >= 70) return "C";
        if (score >= 60) return "D";
        return "F";
    }
}


