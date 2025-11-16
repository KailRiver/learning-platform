package com.example.lms.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    private String categoryName;
    private String teacherName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isPublished;
    private LocalDateTime createdAt;

    // Конструкторы
    public CourseDTO() {}

    public CourseDTO(Long id, String title, String description, String categoryName,
                     String teacherName, LocalDate startDate, LocalDate endDate,
                     Boolean isPublished, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.categoryName = categoryName;
        this.teacherName = teacherName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isPublished = isPublished;
        this.createdAt = createdAt;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public Boolean getIsPublished() { return isPublished; }
    public void setIsPublished(Boolean isPublished) { this.isPublished = isPublished; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}