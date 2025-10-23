package com.onlineexam.service;

import com.onlineexam.dto.QuestionDto;
import com.onlineexam.entity.Exam;
import com.onlineexam.entity.Question;
import com.onlineexam.entity.User;
import com.onlineexam.repository.ExamRepository;
import com.onlineexam.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionService {
    
    private final QuestionRepository questionRepository;
    private final ExamRepository examRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    
    public QuestionDto createQuestion(Long examId, QuestionDto questionDto) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found with id: " + examId));
        
        User currentUser = userService.getCurrentUser();
        
        // Only the exam creator or admin can add questions
        if (!exam.getCreatedBy().getId().equals(currentUser.getId()) && 
            !currentUser.getRole().equals(User.Role.ADMIN)) {
            throw new RuntimeException("You don't have permission to add questions to this exam");
        }
        
        Question question = new Question();
        question.setQuestionText(questionDto.getQuestionText());
        question.setOptions(questionDto.getOptions());
        question.setCorrectAnswer(questionDto.getCorrectAnswer());
        question.setExam(exam);
        
        Question savedQuestion = questionRepository.save(question);
        return modelMapper.map(savedQuestion, QuestionDto.class);
    }
    
    public QuestionDto updateQuestion(Long questionId, QuestionDto questionDto) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));
        
        User currentUser = userService.getCurrentUser();
        
        // Only the exam creator or admin can update questions
        if (!question.getExam().getCreatedBy().getId().equals(currentUser.getId()) && 
            !currentUser.getRole().equals(User.Role.ADMIN)) {
            throw new RuntimeException("You don't have permission to update this question");
        }
        
        question.setQuestionText(questionDto.getQuestionText());
        question.setOptions(questionDto.getOptions());
        question.setCorrectAnswer(questionDto.getCorrectAnswer());
        
        Question updatedQuestion = questionRepository.save(question);
        return modelMapper.map(updatedQuestion, QuestionDto.class);
    }
    
    public QuestionDto getQuestionById(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));
        return modelMapper.map(question, QuestionDto.class);
    }
    
    public List<QuestionDto> getQuestionsByExam(Long examId) {
        List<Question> questions = questionRepository.findByExamId(examId);
        return questions.stream()
                .map(question -> modelMapper.map(question, QuestionDto.class))
                .collect(Collectors.toList());
    }
    
    public void deleteQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));
        
        User currentUser = userService.getCurrentUser();
        
        // Only the exam creator or admin can delete questions
        if (!question.getExam().getCreatedBy().getId().equals(currentUser.getId()) && 
            !currentUser.getRole().equals(User.Role.ADMIN)) {
            throw new RuntimeException("You don't have permission to delete this question");
        }
        
        questionRepository.deleteById(questionId);
    }
}


