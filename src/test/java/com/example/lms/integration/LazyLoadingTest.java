package com.example.lms.integration;

import com.example.lms.entity.Course;
import com.example.lms.service.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.hibernate.LazyInitializationException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LazyLoadingTest {

    @Autowired
    private CourseService courseService;

    @Test
    void testLazyInitializationException() {
        // Создаем и сохраняем курс с модулями
        Course course = new Course();
        course.setTitle("Test Course");
        course.setDescription("Test Description");

        Course savedCourse = courseService.saveCourse(course);

        // Пытаемся получить модули вне транзакции
        assertThrows(LazyInitializationException.class, () -> {
            // Этот вызов выбросит исключение, так как сессия закрыта
            int moduleCount = savedCourse.getModules().size();
        });
    }

    @Test
    void testLazyLoadingWithTransactional() {
        // Этот тест должен работать, так как выполняется в транзакции
        Course course = new Course();
        course.setTitle("Another Test Course");

        Course savedCourse = courseService.saveCourse(course);

        // В рамках транзакции можем обращаться к ленивым коллекциям
        assertNotNull(savedCourse.getModules());
        assertEquals(0, savedCourse.getModules().size());
    }
}