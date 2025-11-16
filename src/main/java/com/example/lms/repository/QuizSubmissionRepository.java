package com.example.lms.repository;

import com.example.lms.entity.QuizSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuizSubmissionRepository extends JpaRepository<QuizSubmission, Long> {
    List<QuizSubmission> findByStudentId(Long studentId);
    List<QuizSubmission> findByQuizId(Long quizId);
    List<QuizSubmission> findByStudentIdAndQuizId(Long studentId, Long quizId);
}