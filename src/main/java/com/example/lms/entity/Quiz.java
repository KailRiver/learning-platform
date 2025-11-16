package com.example.lms.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quizzes")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToOne
    @JoinColumn(name = "module_id")
    private CourseModule module;

    @OneToMany(mappedBy = "quiz", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "quiz", fetch = FetchType.LAZY)
    private List<QuizSubmission> quizSubmissions = new ArrayList<>();

    // Конструкторы, геттеры и сеттеры
    public Quiz() {}

    public Quiz(String title) {
        this.title = title;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public CourseModule getModule() { return module; }
    public void setModule(CourseModule module) { this.module = module; }
    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }
    public List<QuizSubmission> getQuizSubmissions() { return quizSubmissions; }
    public void setQuizSubmissions(List<QuizSubmission> quizSubmissions) { this.quizSubmissions = quizSubmissions; }
}