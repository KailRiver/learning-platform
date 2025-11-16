package com.example.lms.repository;

import com.example.lms.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByLessonId(Long lessonId);

    @Query("SELECT a FROM Assignment a JOIN FETCH a.lesson WHERE a.lesson.id = :lessonId")
    List<Assignment> findByLessonIdWithDetails(Long lessonId);
}