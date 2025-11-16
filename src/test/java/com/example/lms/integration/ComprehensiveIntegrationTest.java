package com.example.lms.integration;

import com.example.lms.entity.*;
import com.example.lms.service.*;
import com.example.lms.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ComprehensiveIntegrationTest {

    @Autowired private UserService userService;
    @Autowired private CourseService courseService;
    @Autowired private EnrollmentService enrollmentService;
    @Autowired private AssignmentService assignmentService;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private CourseModuleRepository courseModuleRepository;
    @Autowired private LessonRepository lessonRepository;
    @Autowired private AssignmentRepository assignmentRepository;
    @Autowired private UserRepository userRepository;

    @Test
    void testCompleteStudentJourney() {
        // 1. Создаем преподавателя
        User teacher = userService.createUser("John Doe", "john@example.com", Role.TEACHER);
        assertNotNull(teacher.getId());

        // 2. Создаем студента
        User student = userService.createUser("Jane Smith", "jane@example.com", Role.STUDENT);
        assertNotNull(student.getId());

        // 3. Создаем категорию
        Category category = new Category("Programming");
        categoryRepository.save(category);

        // 4. Создаем курс
        Course course = new Course();
        course.setTitle("Java Programming");
        course.setDescription("Learn Java from scratch");
        course.setTeacher(teacher);
        course.setCategory(category);
        course.setStartDate(LocalDate.now());

        Course savedCourse = courseService.saveCourse(course);
        assertNotNull(savedCourse.getId());

        // 5. Создаем модуль
        CourseModule module = new CourseModule("Basics", 1);
        module.setCourse(savedCourse);
        courseModuleRepository.save(module);

        // 6. Создаем урок
        Lesson lesson = new Lesson("Introduction to Java", "Java basics content");
        lesson.setModule(module);
        lessonRepository.save(lesson);

        // 7. Создаем задание
        Assignment assignment = new Assignment();
        assignment.setTitle("First Java Program");
        assignment.setDescription("Write Hello World program");
        assignment.setDueDate(LocalDate.now().plusDays(7));
        assignment.setMaxScore(100);
        assignment.setLesson(lesson);
        assignmentRepository.save(assignment);

        // 8. Записываем студента на курс
        Enrollment enrollment = enrollmentService.enrollStudent(student.getId(), savedCourse.getId());
        assertNotNull(enrollment.getId());
        assertEquals("ACTIVE", enrollment.getStatus());

        // 9. Студент отправляет решение
        Submission submission = assignmentService.submitAssignment(
                assignment.getId(), student.getId(), "public class Hello { public static void main(String[] args) { System.out.println(\"Hello World\"); } }");
        assertNotNull(submission.getId());
        assertNotNull(submission.getSubmittedAt());

        // 10. Преподаватель оценивает работу
        Submission gradedSubmission = assignmentService.gradeSubmission(submission.getId(), 95, "Good job!");
        assertEquals(95, gradedSubmission.getScore());
        assertEquals("Good job!", gradedSubmission.getFeedback());

        // 11. Проверяем записи студента
        List<Enrollment> studentEnrollments = enrollmentService.getStudentEnrollments(student.getId());
        assertEquals(1, studentEnrollments.size());

        // 12. Проверяем студентов курса
        List<Enrollment> courseEnrollments = enrollmentService.getCourseEnrollments(savedCourse.getId());
        assertEquals(1, courseEnrollments.size());
    }

    @Test
    void testLazyLoadingBehavior() {
        // Создаем курс с модулями
        User teacher = userService.createUser("Prof. Lazy", "lazy@example.com", Role.TEACHER);
        Category category = new Category("Test Category");
        categoryRepository.save(category);

        Course course = new Course();
        course.setTitle("Lazy Loading Test");
        course.setTeacher(teacher);
        course.setCategory(category);
        Course savedCourse = courseService.saveCourse(course);

        // Этот тест должен работать в транзакции
        assertNotNull(savedCourse.getModules());
        assertEquals(0, savedCourse.getModules().size());
    }

    @Test
    void testDuplicateEnrollmentPrevention() {
        User student = userService.createUser("Duplicate Student", "duplicate@example.com", Role.STUDENT);
        User teacher = userService.createUser("Duplicate Teacher", "teacher@example.com", Role.TEACHER);

        Category category = new Category("Test");
        categoryRepository.save(category);

        Course course = new Course();
        course.setTitle("Test Course");
        course.setTeacher(teacher);
        course.setCategory(category);
        Course savedCourse = courseService.saveCourse(course);

        // Первая запись - должна работать
        Enrollment enrollment1 = enrollmentService.enrollStudent(student.getId(), savedCourse.getId());
        assertNotNull(enrollment1);

        // Вторая запись - должна выбросить исключение
        assertThrows(RuntimeException.class, () -> {
            enrollmentService.enrollStudent(student.getId(), savedCourse.getId());
        });
    }

    @Test
    void testCascadeOperations() {
        User teacher = userService.createUser("Cascade Teacher", "cascade@example.com", Role.TEACHER);
        Category category = new Category("Cascade Category");
        categoryRepository.save(category);

        Course course = new Course();
        course.setTitle("Cascade Test Course");
        course.setTeacher(teacher);
        course.setCategory(category);

        // Добавляем модули прямо в курс
        CourseModule module1 = new CourseModule("Module 1", 1);
        module1.setCourse(course);
        course.getModules().add(module1);

        CourseModule module2 = new CourseModule("Module 2", 2);
        module2.setCourse(course);
        course.getModules().add(module2);

        Course savedCourse = courseService.saveCourse(course);

        // Проверяем, что модули сохранились каскадно
        assertFalse(savedCourse.getModules().isEmpty());
        assertEquals(2, savedCourse.getModules().size());
    }
}