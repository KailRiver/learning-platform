package com.example.lms.web;

import com.example.lms.dto.EnrollmentDTO;
import com.example.lms.entity.Enrollment;
import com.example.lms.service.EnrollmentService;
import com.example.lms.service.DTOMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final DTOMapper dtoMapper;

    public EnrollmentController(EnrollmentService enrollmentService, DTOMapper dtoMapper) {
        this.enrollmentService = enrollmentService;
        this.dtoMapper = dtoMapper;
    }

    @PostMapping
    public ResponseEntity<?> enrollStudent(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {
        try {
            Enrollment enrollment = enrollmentService.enrollStudent(studentId, courseId);
            return ResponseEntity.ok(enrollment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getStudentEnrollments(@PathVariable Long studentId) {
        try {
            List<EnrollmentDTO> enrollments = enrollmentService.getStudentEnrollments(studentId).stream()
                    .map(dtoMapper::toEnrollmentDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getCourseEnrollments(@PathVariable Long courseId) {
        try {
            List<EnrollmentDTO> enrollments = enrollmentService.getCourseEnrollments(courseId).stream()
                    .map(dtoMapper::toEnrollmentDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> unenrollStudent(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {
        try {
            enrollmentService.unenrollStudent(studentId, courseId);
            return ResponseEntity.ok().body("Student unenrolled successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}