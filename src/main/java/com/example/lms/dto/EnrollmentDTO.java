package com.example.lms.dto;

import java.time.LocalDate;

public class EnrollmentDTO {
    private Long id;
    private String studentName;
    private String courseTitle;
    private LocalDate enrollDate;
    private String status;

    public EnrollmentDTO() {}

    public EnrollmentDTO(Long id, String studentName, String courseTitle,
                         LocalDate enrollDate, String status) {
        this.id = id;
        this.studentName = studentName;
        this.courseTitle = courseTitle;
        this.enrollDate = enrollDate;
        this.status = status;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getCourseTitle() { return courseTitle; }
    public void setCourseTitle(String courseTitle) { this.courseTitle = courseTitle; }
    public LocalDate getEnrollDate() { return enrollDate; }
    public void setEnrollDate(LocalDate enrollDate) { this.enrollDate = enrollDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}