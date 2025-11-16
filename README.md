# Learning Management System (LMS)

Учебная платформа для онлайн-курсов по ORM и Hibernate. Проект демонстрирует использование Spring Boot, JPA/Hibernate и PostgreSQL для создания комплексной системы управления образованием.

##  Быстрый старт

### Предварительные требования
- **Java 17** или выше
- **Maven 3.9+**
- **PostgreSQL 14+** (или Docker)

### Установка и запуск

1. **Клонирование репозитория**
```bash
git clone https://github.com/KailRiver/learning-platform.git
cd learning-platform
```

2. **Настройка базы данных**
```sql
-- Создайте базу данных в PostgreSQL
CREATE DATABASE learning_platform;
```

3. **Запуск приложения**
```bash
mvn spring-boot:run
```

Приложение будет доступно по адресу: `http://localhost:8080`

##  Конфигурация

### Настройки базы данных (application.properties)
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/learning_platform
spring.datasource.username=postgres
spring.datasource.password=12345

spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.open-in-view=false
```

### Переменные окружения (опционально)
```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/learning_platform
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=12345
```

##  Архитектура проекта

```
src/
├── main/java/com/example/lms/
│   ├── entity/          # 16 JPA сущностей
│   ├── repository/      # 11 Spring Data JPA репозиториев
│   ├── service/         # Бизнес-логика (5 сервисов)
│   ├── web/             # REST контроллеры (4 контроллера)
│   ├── dto/             # Data Transfer Objects
│   ├── config/          # Конфигурация приложения
│   └── exception/       # Обработка исключений
├── test/java/com/example/lms/integration/
│   ├── ComprehensiveIntegrationTest.java
│   ├── CourseIntegrationTest.java
│   └── LazyLoadingTest.java
└── resources/
    ├── application.properties
    └── application.yml
```

##  Модель данных

### Основные сущности (16)

| Сущность | Описание | Связи |
|----------|----------|--------|
| **User** | Пользователи системы | 5x @OneToMany, 1x @OneToOne |
| **Course** | Учебные курсы | 3x @OneToMany, 2x @ManyToOne, 1x @ManyToMany |
| **CourseModule** | Модули курсов | 2x @OneToMany, 1x @ManyToOne, 1x @OneToOne |
| **Lesson** | Уроки | 1x @OneToMany, 1x @ManyToOne |
| **Assignment** | Задания | 1x @OneToMany, 1x @ManyToOne |
| **Submission** | Решения заданий | 2x @ManyToOne |
| **Quiz** | Тесты | 2x @OneToMany, 1x @OneToOne |
| **Question** | Вопросы тестов | 1x @OneToMany, 1x @ManyToOne |
| **AnswerOption** | Варианты ответов | 1x @ManyToOne |
| **QuizSubmission** | Результаты тестов | 2x @ManyToOne |
| **Enrollment** | Записи на курсы | 2x @ManyToOne |
| **Category** | Категории курсов | 1x @OneToMany |
| **CourseReview** | Отзывы о курсах | 2x @ManyToOne |
| **Profile** | Профили пользователей | 1x @OneToOne |
| **Tag** | Теги курсов | 1x @ManyToMany |
| **Role** | Роли пользователей (enum) | - |

### Типы связей
- **@OneToOne**: 3 связи
- **@OneToMany**: 8 связей
- **@ManyToOne**: 11 связей
- **@ManyToMany**: 2 связи

Все связи настроены с **ленивой загрузкой** (`FetchType.LAZY`)

##  REST API

### Пользователи

```http
GET /users/role/{role}          # Получить пользователей по роли
POST /users                     # Создать пользователя
```

**Пример:**
```bash
# Создать преподавателя
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"name": "Dr. Smith", "email": "smith@university.edu", "role": "TEACHER"}'

# Получить всех студентов
curl http://localhost:8080/users/role/STUDENT
```

### Курсы

```http
GET /courses                    # Получить все курсы (DTO)
GET /courses/{id}              # Получить курс по ID
POST /courses                  # Создать курс
```

### Записи на курсы

```http
POST /enrollments?studentId={id}&courseId={id}    # Записать студента
GET /enrollments/student/{studentId}              # Курсы студента  
GET /enrollments/course/{courseId}               # Студенты курса
DELETE /enrollments?studentId={id}&courseId={id} # Отписать студента
```

### Задания и решения

```http
POST /assignments                               # Создать задание
GET /assignments/lesson/{lessonId}             # Задания урока
POST /assignments/{id}/submit?studentId={id}&content={text}  # Отправить решение
GET /assignments/{id}/submissions              # Решения задания
POST /assignments/submissions/{id}/grade?score={score}&feedback={text} # Оценить работу
```

##  Тестирование

### Запуск тестов
```bash
# Все тесты
mvn test

# Конкретный тест
mvn test -Dtest=LazyLoadingTest
```

### Интеграционные тесты

1. **ComprehensiveIntegrationTest** - полный сценарий обучения
2. **CourseIntegrationTest** - тесты работы с курсами
3. **LazyLoadingTest** - демонстрация проблем ленивой загрузки

### Демонстрация LazyInitializationException

Тесты специально демонстрируют типичные проблемы ORM:
```java
// Этот тест проверяет возникновение LazyInitializationException
@Test
void testLazyInitializationExceptionThrown() {
    Course course = courseService.getCourseWithDetails(1L);
    // Вызовет исключение при обращении к ленивой коллекции вне транзакции
    assertThrows(LazyInitializationException.class, () -> {
        course.getModules().size();
    });
}
```

##  Технические особенности

### Ленивая загрузка
- Все коллекции используют `FetchType.LAZY`
- Демонстрация типичных проблем ORM
- Решения через `@Transactional` и JOIN FETCH

### Валидация данных
```java
@NotBlank(message = "Name is mandatory")
@Size(min = 2, max = 100)
private String name;

@Email(message = "Email should be valid")
@Column(unique = true)
private String email;
```

### Обработка исключений
- `ResourceNotFoundException` - 404 Not Found
- `DuplicateResourceException` - 400 Bad Request
- Глобальный обработчик возвращает структурированные JSON-ответы

### Транзакции
Все сервисные методы помечены `@Transactional` для обеспечения целостности данных.
