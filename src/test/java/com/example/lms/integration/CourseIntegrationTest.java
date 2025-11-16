package com.example.lms.integration;

import com.example.lms.entity.Course;
import com.example.lms.service.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CourseIntegrationTest {

    @Autowired
    private CourseService courseService;

    @Test
    void testSaveAndLoadCourse() {
        Course c = new Course();
        c.setTitle("Java Basics");
        c.setDescription("Learn Java from scratch");

        Course saved = courseService.saveCourse(c);

        assertNotNull(saved.getId());

        Course found = courseService.getAllCourses().stream()
                .filter(course -> course.getId().equals(saved.getId()))
                .findFirst()
                .orElse(null);

        assertNotNull(found);
        assertEquals(saved.getTitle(), found.getTitle());
    }
}