package com.example.lms.repository;

import com.example.lms.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByModuleId(Long moduleId);
    Optional<Quiz> findByModuleIdAndTitle(Long moduleId, String title);
}