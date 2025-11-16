package com.example.lms.service;

import com.example.lms.entity.Assignment;
import com.example.lms.entity.Submission;
import com.example.lms.entity.User;
import com.example.lms.repository.AssignmentRepository;
import com.example.lms.repository.SubmissionRepository;
import com.example.lms.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;

    public AssignmentService(AssignmentRepository assignmentRepository,
                             SubmissionRepository submissionRepository,
                             UserRepository userRepository) {
        this.assignmentRepository = assignmentRepository;
        this.submissionRepository = submissionRepository;
        this.userRepository = userRepository;
    }

    public Assignment createAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    @Transactional(readOnly = true)
    public List<Assignment> getAssignmentsByLesson(Long lessonId) {

        return assignmentRepository.findByLessonIdWithDetails(lessonId);
    }

    public Submission submitAssignment(Long assignmentId, Long studentId, String content) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Проверяем, не отправил ли студент уже решение
        Optional<Submission> existingSubmission = submissionRepository
                .findByStudentIdAndAssignmentId(studentId, assignmentId);

        if (existingSubmission.isPresent()) {
            throw new RuntimeException("Student has already submitted this assignment");
        }

        Submission submission = new Submission(assignment, student, content);
        return submissionRepository.save(submission);
    }

    @Transactional(readOnly = true)
    public List<Submission> getSubmissionsByAssignment(Long assignmentId) {

        return submissionRepository.findByAssignmentIdWithDetails(assignmentId);
    }

    @Transactional(readOnly = true)
    public List<Submission> getSubmissionsByStudent(Long studentId) {

        return submissionRepository.findByStudentIdWithDetails(studentId);
    }

    public Submission gradeSubmission(Long submissionId, Integer score, String feedback) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        submission.setScore(score);
        submission.setFeedback(feedback);

        return submissionRepository.save(submission);
    }
}