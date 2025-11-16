package com.example.lms.config;

import com.example.lms.entity.*;
import com.example.lms.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;

    public TestDataLoader(UserRepository userRepository,
                          CourseRepository courseRepository,
                          CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Создаем тестовые данные только если база пустая
        if (userRepository.count() == 0) {
            User teacher = new User("Professor X", "profx@school.com", Role.TEACHER);
            User student = new User("Student One", "student1@school.com", Role.STUDENT);

            userRepository.save(teacher);
            userRepository.save(student);

            Category programming = new Category("Programming");
            categoryRepository.save(programming);

            Course javaCourse = new Course("Java Basics", "Learn Java programming");
            javaCourse.setTeacher(teacher);
            javaCourse.setCategory(programming);
            courseRepository.save(javaCourse);

            System.out.println("Test data loaded successfully!");
        }
    }
}