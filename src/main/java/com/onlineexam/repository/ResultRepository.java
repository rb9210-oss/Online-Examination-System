package com.onlineexam.repository;

import com.onlineexam.entity.Exam;
import com.onlineexam.entity.Result;
import com.onlineexam.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    
    List<Result> findByStudent(User student);
    
    List<Result> findByExam(Exam exam);
    
    Page<Result> findByStudentId(Long studentId, Pageable pageable);
    
    Page<Result> findByExamId(Long examId, Pageable pageable);
    
    Optional<Result> findByExamIdAndStudentId(Long examId, Long studentId);
    
    @Query("SELECT r FROM Result r WHERE r.student.id = :studentId AND r.exam.id = :examId")
    Optional<Result> findExistingResult(@Param("studentId") Long studentId, @Param("examId") Long examId);
    
    @Query("SELECT AVG(r.score) FROM Result r WHERE r.exam.id = :examId")
    Double getAverageScoreByExamId(@Param("examId") Long examId);
}


