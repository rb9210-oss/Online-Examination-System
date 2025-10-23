package com.onlineexam.service;

import com.onlineexam.dto.ExamSubmissionDto;
import com.onlineexam.dto.ResultDto;
import com.onlineexam.entity.Exam;
import com.onlineexam.entity.Question;
import com.onlineexam.entity.Result;
import com.onlineexam.entity.User;
import com.onlineexam.repository.ExamRepository;
import com.onlineexam.repository.QuestionRepository;
import com.onlineexam.repository.ResultRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ExamSubmissionService {
    
    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final ResultRepository resultRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    
    public ResultDto submitExam(ExamSubmissionDto submissionDto) {
        Exam exam = examRepository.findById(submissionDto.getExamId())
                .orElseThrow(() -> new RuntimeException("Exam not found with id: " + submissionDto.getExamId()));
        
        User currentUser = userService.getCurrentUser();
        
        // Check if student already submitted this exam
        if (resultRepository.findByExamIdAndStudentId(exam.getId(), currentUser.getId()).isPresent()) {
            throw new RuntimeException("You have already submitted this exam");
        }
        
        List<Question> questions = questionRepository.findByExamId(exam.getId());
        
        if (questions.isEmpty()) {
            throw new RuntimeException("No questions found for this exam");
        }
        
        // Calculate score
        int correctAnswers = 0;
        int totalQuestions = questions.size();
        
        for (ExamSubmissionDto.AnswerDto answer : submissionDto.getAnswers()) {
            Question question = questions.stream()
                    .filter(q -> q.getId().equals(answer.getQuestionId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Question not found with id: " + answer.getQuestionId()));
            
            if (question.getCorrectAnswer().equals(answer.getSelectedAnswer())) {
                correctAnswers++;
            }
        }
        
        // Calculate percentage score
        int score = (int) Math.round((double) correctAnswers / totalQuestions * 100);
        
        // Create result
        Result result = new Result();
        result.setExam(exam);
        result.setStudent(currentUser);
        result.setScore(score);
        result.setTotalQuestions(totalQuestions);
        result.setCorrectAnswers(correctAnswers);
        
        Result savedResult = resultRepository.save(result);
        
        ResultDto resultDto = modelMapper.map(savedResult, ResultDto.class);
        resultDto.setExamTitle(exam.getTitle());
        resultDto.setStudentName(currentUser.getName());
        resultDto.setGrade(calculateGrade(score));
        
        return resultDto;
    }
    
    private String calculateGrade(int score) {
        if (score >= 90) return "A";
        if (score >= 80) return "B";
        if (score >= 70) return "C";
        if (score >= 60) return "D";
        return "F";
    }
}


