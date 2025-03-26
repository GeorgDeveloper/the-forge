package ru.georgdeveloper.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Сущность "Должностная инструкция".
 * Содержит описание должностных обязанностей и связана с конкретной должностью (Position).
 */
@Entity
@Table(name = "job_description")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JobDescription implements Serializable {

    private static final long serialVersionUID = 1L; // Идентификатор версии для сериализации

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id; // Уникальный идентификатор

    @NotNull
    @Column(name = "description_name", nullable = false)
    private String descriptionName; // Наименование должностной инструкции

    @NotNull
    @Column(name = "approval_date", nullable = false)
    private LocalDate approvalDate; // Дата утверждения инструкции

    // Однонаправленная связь один-к-одному с сущностью Position
    @JsonIgnoreProperties(value = { "jobDescription", "safetyInstructions", "employees" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "jobDescription")
    private Position position; // Должность, для которой предназначена инструкция

    // Стандартные getter'ы и setter'ы с fluent-интерфейсом
    public Long getId() {
        return this.id;
    }

    public JobDescription id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescriptionName() {
        return this.descriptionName;
    }

    public JobDescription descriptionName(String descriptionName) {
        this.setDescriptionName(descriptionName);
        return this;
    }

    public void setDescriptionName(String descriptionName) {
        this.descriptionName = descriptionName;
    }

    public LocalDate getApprovalDate() {
        return this.approvalDate;
    }

    public JobDescription approvalDate(LocalDate approvalDate) {
        this.setApprovalDate(approvalDate);
        return this;
    }

    public void setApprovalDate(LocalDate approvalDate) {
        this.approvalDate = approvalDate;
    }

    /**
     * Управление связью с Position с поддержанием целостности
     */
    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        // Очищаем предыдущую связь
        if (this.position != null) {
            this.position.setJobDescription(null);
        }
        // Устанавливаем новую связь
        if (position != null) {
            position.setJobDescription(this);
        }
        this.position = position;
    }

    public JobDescription position(Position position) {
        this.setPosition(position);
        return this;
    }

    // equals, hashCode и toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobDescription)) return false;
        return getId() != null && getId().equals(((JobDescription) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode(); // Рекомендуемый подход для JPA сущностей
    }

    @Override
    public String toString() {
        return (
            "Должностная инструкция{" +
            "id=" +
            getId() +
            ", наименование='" +
            getDescriptionName() +
            "'" +
            ", дата создания='" +
            getApprovalDate() +
            "'" +
            "}"
        );
    }
}
