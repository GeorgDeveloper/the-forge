package ru.georgdeveloper.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Сущность "Дополнительное обучение".
 * Хранит информацию о дополнительных тренингах/обучениях сотрудников.
 */
@Entity
@Table(name = "additional_training") // Связывает с таблицей в БД
@SuppressWarnings("common-java:DuplicatedBlocks") // Игнорирует предупреждения о дублировании блоков
public class AdditionalTraining implements Serializable {

    private static final long serialVersionUID = 1L; // Идентификатор для сериализации

    @Id // Поле является первичным ключом
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator") // Стратегия генерации ID
    @SequenceGenerator(name = "sequenceGenerator") // Используемый генератор последовательностей
    @Column(name = "id") // Связь с колонкой в таблице
    private Long id;

    @NotNull // Валидация - поле не может быть null
    @Column(name = "training_name", nullable = false)
    private String trainingName; // Название тренинга

    @NotNull
    @Column(name = "training_date", nullable = false)
    private LocalDate trainingDate; // Дата прохождения тренинга

    @NotNull
    @Column(name = "validity_period", nullable = false)
    private Integer validityPeriod; // Срок действия тренинга (в месяцах)

    @Column(name = "next_training_date")
    private LocalDate nextTrainingDate; // Дата следующего тренинга (вычисляемое поле)

    // Связь многие-к-одному с сущностью Profession
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "protectiveEquipments", "additionalTrainings", "safetyInstructions", "employees" },
        allowSetters = true
        // "инструкции по технике безопасности", "сотрудники") при JSON сериализации // Игнорирует указанные поля ("средства защиты", "дополнительные тренинги",
    )
    private Profession profession; // Профессия, для которой требуется тренинг

    // Методы доступа (getters/setters) с fluent-интерфейсом
    public Long getId() {
        return this.id;
    }

    public AdditionalTraining id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrainingName() {
        return this.trainingName;
    }

    public AdditionalTraining trainingName(String trainingName) {
        this.setTrainingName(trainingName);
        return this;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public LocalDate getTrainingDate() {
        return this.trainingDate;
    }

    public AdditionalTraining trainingDate(LocalDate trainingDate) {
        this.setTrainingDate(trainingDate);
        return this;
    }

    public void setTrainingDate(LocalDate trainingDate) {
        this.trainingDate = trainingDate;
    }

    public Integer getValidityPeriod() {
        return this.validityPeriod;
    }

    public AdditionalTraining validityPeriod(Integer validityPeriod) {
        this.setValidityPeriod(validityPeriod);
        return this;
    }

    public void setValidityPeriod(Integer validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public LocalDate getNextTrainingDate() {
        return this.nextTrainingDate;
    }

    public AdditionalTraining nextTrainingDate(LocalDate nextTrainingDate) {
        this.setNextTrainingDate(nextTrainingDate);
        return this;
    }

    public void setNextTrainingDate(LocalDate nextTrainingDate) {
        this.nextTrainingDate = nextTrainingDate;
    }

    public Profession getProfession() {
        return this.profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public AdditionalTraining profession(Profession profession) {
        this.setProfession(profession);
        return this;
    }

    // Переопределенные методы equals и hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdditionalTraining)) {
            return false;
        }
        return getId() != null && getId().equals(((AdditionalTraining) o).getId());
    }

    @Override
    public int hashCode() {
        // Рекомендуемая реализация для JPA сущностей
        return getClass().hashCode();
    }

    // Переопределенный метод toString()
    @Override
    public String toString() {
        return (
            "Дополнительное обучение{" +
            "id=" +
            getId() +
            ", Наименование='" +
            getTrainingName() +
            "'" +
            ", дата обучения='" +
            getTrainingDate() +
            "'" +
            ", срок действия=" +
            getValidityPeriod() +
            ", дата следующего обучения='" +
            getNextTrainingDate() +
            "'" +
            "}"
        );
    }
}
