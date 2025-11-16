package com.example.lms.service;

import com.example.lms.entity.Course;
import com.example.lms.entity.Enrollment;
import com.example.lms.entity.User;
import com.example.lms.exception.DuplicateResourceException;
import com.example.lms.exception.ResourceNotFoundException;
import com.example.lms.repository.CourseRepository;
import com.example.lms.repository.EnrollmentRepository;
import com.example.lms.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             UserRepository userRepository,
                             CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    public Enrollment enrollStudent(Long studentId, Long courseId) {
        // Проверяем, не записан ли уже студент
        if (enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId).isPresent()) {
            throw new DuplicateResourceException("Student is already enrolled in this course");
        }

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollDate(LocalDate.now());
        enrollment.setStatus("ACTIVE");

        return enrollmentRepository.save(enrollment);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getStudentEnrollments(Long studentId) {
        return enrollmentRepository.findByStudentIdWithDetails(studentId);
    }

    @Transactional(readOnly = true)
    public List<Enrollment> getCourseEnrollments(Long courseId) {
        return enrollmentRepository.findByCourseIdWithDetails(courseId);
    }

    public void unenrollStudent(Long studentId, Long courseId) {
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

        enrollmentRepository.delete(enrollment);
    }
}