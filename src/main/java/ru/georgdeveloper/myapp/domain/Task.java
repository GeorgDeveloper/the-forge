package ru.georgdeveloper.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import ru.georgdeveloper.myapp.domain.enumeration.TaskPriority;
import ru.georgdeveloper.myapp.domain.enumeration.TaskStatus;

/**
 * Сущность "Задача".
 * Представляет рабочую задачу, назначенную сотруднику, с указанием статуса, приоритета и сроков выполнения.
 */
@Entity
@Table(name = "task")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L; // Идентификатор версии для сериализации

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id; // Уникальный идентификатор задачи

    @NotNull
    @Column(name = "task_name", nullable = false)
    private String taskName; // Название задачи

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate; // Дата создания задачи

    @NotNull
    @Column(name = "planned_completion_date", nullable = false)
    private LocalDate plannedCompletionDate; // Планируемая дата завершения

    @NotNull
    @Enumerated(EnumType.STRING) // Хранение enum как строки в БД
    @Column(name = "status", nullable = false)
    private TaskStatus status; // Текущий статус задачи

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private TaskPriority priority; // Приоритет задачи

    @NotNull
    @Column(name = "body", nullable = false)
    private String body; // Подробное описание задачи

    @Column(name = "actual_completion_date")
    private LocalDate actualCompletionDate; // Фактическая дата выполнения задачи

    // Связь многие-к-одному с Employee (исполнитель задачи)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "trainings", "tasks", "position", "professions", "team" },
        allowSetters = true // Игнорирование циклических ссылок при сериализации
    )
    private Employee employee;

    // Методы доступа с fluent-интерфейсом
    public Long getId() {
        return this.id;
    }

    public Task id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public Task taskName(String taskName) {
        this.setTaskName(taskName);
        return this;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public Task creationDate(LocalDate creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDate getPlannedCompletionDate() {
        return this.plannedCompletionDate;
    }

    public Task plannedCompletionDate(LocalDate plannedCompletionDate) {
        this.setPlannedCompletionDate(plannedCompletionDate);
        return this;
    }

    public void setPlannedCompletionDate(LocalDate plannedCompletionDate) {
        this.plannedCompletionDate = plannedCompletionDate;
    }

    public TaskStatus getStatus() {
        return this.status;
    }

    public Task status(TaskStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskPriority getPriority() {
        return this.priority;
    }

    public Task priority(TaskPriority priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public String getBody() {
        return this.body;
    }

    public Task body(String body) {
        this.setBody(body);
        return this;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDate getActualCompletionDate() {
        return this.actualCompletionDate;
    }

    public Task actualCompletionDate(LocalDate actualCompletionDate) {
        this.setActualCompletionDate(actualCompletionDate);
        return this;
    }

    public void setActualCompletionDate(LocalDate actualCompletionDate) {
        this.actualCompletionDate = actualCompletionDate;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Task employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // equals и hashCode для корректной работы с коллекциями
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        return getId() != null && getId().equals(((Task) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode(); // Рекомендуемый подход для JPA
    }

    @Override
    public String toString() {
        return (
            "Задача{" +
            "id=" +
            getId() +
            ", наименование='" +
            getTaskName() +
            "'" +
            ", дата создания='" +
            getCreationDate() +
            "'" +
            ", запланированная дата выполнения='" +
            getPlannedCompletionDate() +
            "'" +
            ", фактическая дата выполнения='" +
            getActualCompletionDate() +
            "'" +
            ", статус='" +
            getStatus() +
            "'" +
            ", приоритет='" +
            getPriority() +
            "'" +
            ", содержание='" +
            getBody() +
            "'" +
            "}"
        );
    }
}
