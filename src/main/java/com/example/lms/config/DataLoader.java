package com.example.lms.config;

import com.example.lms.entity.*;
import com.example.lms.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final CourseModuleRepository courseModuleRepository;
    private final LessonRepository lessonRepository;
    private final AssignmentRepository assignmentRepository;
    private final EnrollmentRepository enrollmentRepository;

    public DataLoader(UserRepository userRepository,
                      CourseRepository courseRepository,
                      CategoryRepository categoryRepository,
                      CourseModuleRepository courseModuleRepository,
                      LessonRepository lessonRepository,
                      AssignmentRepository assignmentRepository,
                      EnrollmentRepository enrollmentRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
        this.courseModuleRepository = courseModuleRepository;
        this.lessonRepository = lessonRepository;
        this.assignmentRepository = assignmentRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Создаем тестовые данные только если база пустая
        if (userRepository.count() == 0) {
            // Создаем пользователей
            User teacher = new User("Professor X", "profx@school.com", Role.TEACHER);
            User student1 = new User("Student One", "student1@school.com", Role.STUDENT);
            User student2 = new User("Student Two", "student2@school.com", Role.STUDENT);

            userRepository.save(teacher);
            userRepository.save(student1);
            userRepository.save(student2);

            // Создаем категории
            Category programming = new Category("Programming");
            Category design = new Category("Design");
            categoryRepository.save(programming);
            categoryRepository.save(design);

            // Создаем курс
            Course javaCourse = new Course();
            javaCourse.setTitle("Java Advanced");
            javaCourse.setDescription("Advanced Java programming concepts");
            javaCourse.setTeacher(teacher);
            javaCourse.setCategory(programming);
            javaCourse.setStartDate(LocalDate.now());
            courseRepository.save(javaCourse);

            // Создаем модули
            CourseModule module1 = new CourseModule("OOP Principles", 1);
            module1.setCourse(javaCourse);
            courseModuleRepository.save(module1);

            CourseModule module2 = new CourseModule("Collections Framework", 2);
            module2.setCourse(javaCourse);
            courseModuleRepository.save(module2);

            // Создаем уроки
            Lesson lesson1 = new Lesson("Inheritance and Polymorphism", "OOP concepts explained");
            lesson1.setModule(module1);
            lessonRepository.save(lesson1);

            Lesson lesson2 = new Lesson("Lists and Maps", "Collections framework overview");
            lesson2.setModule(module2);
            lessonRepository.save(lesson2);

            // Создаем задания
            Assignment assignment1 = new Assignment();
            assignment1.setTitle("OOP Design");
            assignment1.setDescription("Design a class hierarchy");
            assignment1.setDueDate(LocalDate.now().plusDays(14));
            assignment1.setMaxScore(100);
            assignment1.setLesson(lesson1);
            assignmentRepository.save(assignment1);

            // Записываем студентов на курс
            Enrollment enrollment1 = new Enrollment();
            enrollment1.setStudent(student1);
            enrollment1.setCourse(javaCourse);
            enrollment1.setEnrollDate(LocalDate.now());
            enrollment1.setStatus("ACTIVE");

            Enrollment enrollment2 = new Enrollment();
            enrollment2.setStudent(student2);
            enrollment2.setCourse(javaCourse);
            enrollment2.setEnrollDate(LocalDate.now());
            enrollment2.setStatus("ACTIVE");

            enrollmentRepository.save(enrollment1);
            enrollmentRepository.save(enrollment2);

            System.out.println("Test data loaded successfully!");
        }
    }
}