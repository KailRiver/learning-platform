package com.example.lms.integration;

import com.example.lms.entity.Course;
import com.example.lms.service.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.LazyInitializationException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LazyLoadingTest {

    @Autowired
    private CourseService courseService;

    @Test
    void testLazyInitializationException() {
        // Создаем и сохраняем курс
        Course course = new Course();
        course.setTitle("Test Course");
        course.setDescription("Test Description");

        Course savedCourse = courseService.saveCourse(course);

        // Открываем новую транзакцию только для получения курса
        Course detachedCourse = getCourseDetached(savedCourse.getId());

        // Пытаемся получить модули вне транзакции - должно выбросить исключение
        assertThrows(LazyInitializationException.class, () -> {
            int moduleCount = detachedCourse.getModules().size();
        });
    }

    @Transactional
    protected Course getCourseDetached(Long courseId) {
        // Получаем курс в рамках транзакции, но возвращаем отсоединенный объект
        return courseService.getCourseWithDetails(courseId);
    }

    @Test
    void testLazyLoadingWithTransactional() {
        Course course = new Course();
        course.setTitle("Another Test Course");

        Course savedCourse = courseService.saveCourse(course);

        assertNotNull(savedCourse.getModules());
        assertEquals(0, savedCourse.getModules().size());
    }
}