package com.example.lms.repository;

import com.example.lms.entity.CourseReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CourseReviewRepository extends JpaRepository<CourseReview, Long> {
    List<CourseReview> findByCourseId(Long courseId);
    List<CourseReview> findByStudentId(Long studentId);
    Optional<CourseReview> findByCourseIdAndStudentId(Long courseId, Long studentId);
}