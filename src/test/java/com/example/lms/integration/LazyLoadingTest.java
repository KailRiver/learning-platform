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
    void testLazyInitializationExceptionThrown() {
        // Создаем курс
        Course course = new Course();
        course.setTitle("Lazy Test Course");
        Course savedCourse = courseService.saveCourse(course);

        // Получаем курс без транзакции - используем существующий метод
        Course detachedCourse = courseService.getCourseWithDetails(savedCourse.getId());

        // Должно выбросить LazyInitializationException при обращении к ленивой коллекции
        assertThrows(LazyInitializationException.class, () -> {
            // Пытаемся обратиться к ленивой коллекции вне транзакции
            detachedCourse.getModules().size();
        });
    }

    @Test
    @Transactional
    void testLazyLoadingWorksInTransaction() {
        // Создаем курс
        Course course = new Course();
        course.setTitle("Lazy Test Course 2");
        Course savedCourse = courseService.saveCourse(course);

        // В транзакции можно безопасно обращаться к ленивым коллекциям
        Course loadedCourse = courseService.getCourseWithDetails(savedCourse.getId());
        assertNotNull(loadedCourse.getModules());
        // Не должно выбрасывать исключение
        assertEquals(0, loadedCourse.getModules().size());
    }

    @Test
    void testDirectLazyAccessThrowsException() {
        // Альтернативный тест для демонстрации LazyInitializationException
        Course course = new Course();
        course.setTitle("Direct Lazy Test");
        Course savedCourse = courseService.saveCourse(course);

        // Получаем курс обычным способом (без JOIN FETCH)
        Course simpleCourse = courseService.getAllCourses().stream()
                .filter(c -> c.getId().equals(savedCourse.getId()))
                .findFirst()
                .orElseThrow();

        // Должно выбросить исключение при доступе к ленивой коллекции
        assertThrows(LazyInitializationException.class, () -> {
            simpleCourse.getModules().isEmpty();
        });
    }
}