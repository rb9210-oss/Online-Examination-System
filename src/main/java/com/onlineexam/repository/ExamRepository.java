package com.onlineexam.repository;

import com.onlineexam.entity.Exam;
import com.onlineexam.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    
    Page<Exam> findByCreatedBy(User createdBy, Pageable pageable);
    
    Page<Exam> findByCategoryId(Long categoryId, Pageable pageable);
    
    Page<Exam> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    @Query("SELECT e FROM Exam e WHERE e.category.id = :categoryId")
    List<Exam> findByCategoryId(@Param("categoryId") Long categoryId);
    
    @Query("SELECT COUNT(q) FROM Question q WHERE q.exam.id = :examId")
    Long countQuestionsByExamId(@Param("examId") Long examId);
}


