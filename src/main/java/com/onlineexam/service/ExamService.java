package com.onlineexam.service;

import com.onlineexam.dto.ExamDto;
import com.onlineexam.entity.Category;
import com.onlineexam.entity.Exam;
import com.onlineexam.entity.User;
import com.onlineexam.repository.CategoryRepository;
import com.onlineexam.repository.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ExamService {
    
    private final ExamRepository examRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    
    public ExamDto createExam(ExamDto examDto) {
        Category category = categoryRepository.findById(examDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + examDto.getCategoryId()));
        
        User currentUser = userService.getCurrentUser();
        
        Exam exam = new Exam();
        exam.setTitle(examDto.getTitle());
        exam.setDescription(examDto.getDescription());
        exam.setCategory(category);
        exam.setCreatedBy(currentUser);
        
        Exam savedExam = examRepository.save(exam);
        return modelMapper.map(savedExam, ExamDto.class);
    }
    
    public ExamDto updateExam(Long examId, ExamDto examDto) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found with id: " + examId));
        
        User currentUser = userService.getCurrentUser();
        
        // Only the creator or admin can update the exam
        if (!exam.getCreatedBy().getId().equals(currentUser.getId()) && 
            !currentUser.getRole().equals(User.Role.ADMIN)) {
            throw new RuntimeException("You don't have permission to update this exam");
        }
        
        Category category = categoryRepository.findById(examDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + examDto.getCategoryId()));
        
        exam.setTitle(examDto.getTitle());
        exam.setDescription(examDto.getDescription());
        exam.setCategory(category);
        
        Exam updatedExam = examRepository.save(exam);
        return modelMapper.map(updatedExam, ExamDto.class);
    }
    
    public ExamDto getExamById(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found with id: " + examId));
        return modelMapper.map(exam, ExamDto.class);
    }
    
    public Page<ExamDto> getExamsByTeacher(Pageable pageable) {
        User currentUser = userService.getCurrentUser();
        return examRepository.findByCreatedBy(currentUser, pageable)
                .map(exam -> modelMapper.map(exam, ExamDto.class));
    }
    
    public Page<ExamDto> getAllExams(Pageable pageable) {
        return examRepository.findAll(pageable)
                .map(exam -> modelMapper.map(exam, ExamDto.class));
    }
    
    public Page<ExamDto> getExamsByCategory(Long categoryId, Pageable pageable) {
        return examRepository.findByCategoryId(categoryId, pageable)
                .map(exam -> modelMapper.map(exam, ExamDto.class));
    }
    
    public void deleteExam(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found with id: " + examId));
        
        User currentUser = userService.getCurrentUser();
        
        // Only the creator or admin can delete the exam
        if (!exam.getCreatedBy().getId().equals(currentUser.getId()) && 
            !currentUser.getRole().equals(User.Role.ADMIN)) {
            throw new RuntimeException("You don't have permission to delete this exam");
        }
        
        examRepository.deleteById(examId);
    }
}


