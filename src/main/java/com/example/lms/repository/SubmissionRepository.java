package com.example.lms.repository;

import com.example.lms.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<Submission> findByStudentId(Long studentId);
    List<Submission> findByAssignmentId(Long assignmentId);
    Optional<Submission> findByStudentIdAndAssignmentId(Long studentId, Long assignmentId);

    @Query("SELECT s FROM Submission s JOIN FETCH s.student JOIN FETCH s.assignment WHERE s.student.id = :studentId")
    List<Submission> findByStudentIdWithDetails(Long studentId);

    @Query("SELECT s FROM Submission s JOIN FETCH s.student JOIN FETCH s.assignment WHERE s.assignment.id = :assignmentId")
    List<Submission> findByAssignmentIdWithDetails(Long assignmentId);
}