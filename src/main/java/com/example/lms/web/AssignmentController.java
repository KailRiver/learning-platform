package com.example.lms.web;

import com.example.lms.entity.Assignment;
import com.example.lms.entity.Submission;
import com.example.lms.service.AssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    public ResponseEntity<Assignment> createAssignment(@RequestBody Assignment assignment) {
        Assignment created = assignmentService.createAssignment(assignment);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<List<Assignment>> getAssignmentsByLesson(@PathVariable Long lessonId) {
        List<Assignment> assignments = assignmentService.getAssignmentsByLesson(lessonId);
        return ResponseEntity.ok(assignments);
    }

    @PostMapping("/{assignmentId}/submit")
    public ResponseEntity<Submission> submitAssignment(
            @PathVariable Long assignmentId,
            @RequestParam Long studentId,
            @RequestParam String content) {
        try {
            Submission submission = assignmentService.submitAssignment(assignmentId, studentId, content);
            return ResponseEntity.ok(submission);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{assignmentId}/submissions")
    public ResponseEntity<List<Submission>> getAssignmentSubmissions(@PathVariable Long assignmentId) {
        List<Submission> submissions = assignmentService.getSubmissionsByAssignment(assignmentId);
        return ResponseEntity.ok(submissions);
    }

    @PostMapping("/submissions/{submissionId}/grade")
    public ResponseEntity<Submission> gradeSubmission(
            @PathVariable Long submissionId,
            @RequestParam Integer score,
            @RequestParam String feedback) {
        try {
            Submission submission = assignmentService.gradeSubmission(submissionId, score, feedback);
            return ResponseEntity.ok(submission);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}