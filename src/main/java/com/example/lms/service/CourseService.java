package com.example.lms.service;

import com.example.lms.dto.CourseDTO;
import com.example.lms.entity.Course;
import com.example.lms.entity.CourseModule;
import com.example.lms.repository.CourseRepository;
import com.example.lms.repository.CourseModuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseModuleRepository courseModuleRepository;
    private final DTOMapper dtoMapper;

    public CourseService(CourseRepository courseRepository,
                         CourseModuleRepository courseModuleRepository,
                         DTOMapper dtoMapper) {
        this.courseRepository = courseRepository;
        this.courseModuleRepository = courseModuleRepository;
        this.dtoMapper = dtoMapper;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<CourseDTO> getAllCoursesDTO() {
        return courseRepository.findAll().stream()
                .map(dtoMapper::toCourseDTO)
                .collect(Collectors.toList());
    }

    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public List<CourseModule> getCourseModules(Long courseId) {
        return courseModuleRepository.findByCourseId(courseId);
    }

    // Метод для получения конкретного курса с явной загрузкой связей
    @Transactional(readOnly = true)
    public Course getCourseWithDetails(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }
}