package com.example.lms.service;

import com.example.lms.dto.CourseDTO;
import com.example.lms.dto.UserDTO;
import com.example.lms.dto.EnrollmentDTO;
import com.example.lms.entity.Course;
import com.example.lms.entity.User;
import com.example.lms.entity.Enrollment;
import org.springframework.stereotype.Component;

@Component
public class DTOMapper {

    public CourseDTO toCourseDTO(Course course) {
        if (course == null) {
            return null;
        }

        return new CourseDTO(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getCategory() != null ? course.getCategory().getName() : null,
                course.getTeacher() != null ? course.getTeacher().getName() : null,
                course.getStartDate(),
                course.getEndDate(),
                course.getIsPublished(),
                course.getCreatedAt()
        );
    }

    public UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }

        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    public EnrollmentDTO toEnrollmentDTO(Enrollment enrollment) {
        if (enrollment == null) {
            return null;
        }

        return new EnrollmentDTO(
                enrollment.getId(),
                enrollment.getStudent() != null ? enrollment.getStudent().getName() : null,
                enrollment.getCourse() != null ? enrollment.getCourse().getTitle() : null,
                enrollment.getEnrollDate(),
                enrollment.getStatus()
        );
    }
}