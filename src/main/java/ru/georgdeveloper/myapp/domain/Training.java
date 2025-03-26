package ru.georgdeveloper.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Сущность "Инструктаж".
 * Содержит информацию о пройденных инструктажах, их периодичности
 * и планируемых датах следующего проведения инструктажа.
 */
@Entity
@Table(name = "training")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Training implements Serializable {

    private static final long serialVersionUID = 1L; // Идентификатор для сериализации

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id; // Уникальный идентификатор записи об инструктаже

    @NotNull
    @Column(name = "training_name", nullable = false)
    private String trainingName; // Наименование инструктажа

    @NotNull
    @Column(name = "last_training_date", nullable = false)
    private LocalDate lastTrainingDate; // Дата последнего прохождения инструктажа

    @NotNull
    @Column(name = "validity_period", nullable = false)
    private Integer validityPeriod; // Срок действия инструктажа (в месяцах)

    @Column(name = "next_training_date")
    private LocalDate nextTrainingDate; // Планируемая дата следующего инструктажа

    // Связь многие-к-одному с Employee (сотрудник)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "trainings", "tasks", "position", "professions", "team" },
        allowSetters = true // Игнорирование циклических ссылок при сериализации
    )
    private Employee employee; // Сотрудник, прошедший инструктаж

    // Методы доступа с fluent-интерфейсом
    public Long getId() {
        return this.id;
    }

    public Training id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrainingName() {
        return this.trainingName;
    }

    public Training trainingName(String trainingName) {
        this.setTrainingName(trainingName);
        return this;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public LocalDate getLastTrainingDate() {
        return this.lastTrainingDate;
    }

    public Training lastTrainingDate(LocalDate lastTrainingDate) {
        this.setLastTrainingDate(lastTrainingDate);
        return this;
    }

    public void setLastTrainingDate(LocalDate lastTrainingDate) {
        this.lastTrainingDate = lastTrainingDate;
        // Можно автоматически рассчитывать nextTrainingDate при обновлении
        if (this.validityPeriod != null) {
            this.nextTrainingDate = lastTrainingDate.plusMonths(this.validityPeriod);
        }
    }

    public Integer getValidityPeriod() {
        return this.validityPeriod;
    }

    public Training validityPeriod(Integer validityPeriod) {
        this.setValidityPeriod(validityPeriod);
        return this;
    }

    public void setValidityPeriod(Integer validityPeriod) {
        this.validityPeriod = validityPeriod;
        // Автоматический пересчет даты следующего обучения
        if (this.lastTrainingDate != null) {
            this.nextTrainingDate = this.lastTrainingDate.plusMonths(validityPeriod);
        }
    }

    public LocalDate getNextTrainingDate() {
        return this.nextTrainingDate;
    }

    public Training nextTrainingDate(LocalDate nextTrainingDate) {
        this.setNextTrainingDate(nextTrainingDate);
        return this;
    }

    public void setNextTrainingDate(LocalDate nextTrainingDate) {
        this.nextTrainingDate = nextTrainingDate;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Training employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    // equals и hashCode для корректной работы с коллекциями
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Training)) return false;
        return getId() != null && getId().equals(((Training) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode(); // Рекомендуемый подход для JPA сущностей
    }

    @Override
    public String toString() {
        return (
            "Инструктаж{" +
            "id=" +
            getId() +
            ", наименование='" +
            getTrainingName() +
            "'" +
            ", дата последнего инструктажа='" +
            getLastTrainingDate() +
            "'" +
            ", период действия=" +
            getValidityPeriod() +
            ", дата следующего инструктажа='" +
            getNextTrainingDate() +
            "'" +
            "}"
        );
    }
}
