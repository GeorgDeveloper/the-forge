package ru.georgdeveloper.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Сущность "Должность".
 * Содержит информацию о должностях в организации и их связях с другими сущностями.
 */
@Entity
@Table(name = "position")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Position implements Serializable {

    private static final long serialVersionUID = 1L; // Идентификатор для сериализации

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id; // Уникальный идентификатор должности

    @NotNull
    @Column(name = "position_name", nullable = false)
    private String positionName; // Название должности

    // Связь один-к-одному с JobDescription (должностная инструкция)
    @JsonIgnoreProperties(value = { "position" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true) // Уникальное соединение
    private JobDescription jobDescription;

    // Связь один-ко-многим с SafetyInstruction (инструкции по безопасности)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "position")
    @JsonIgnoreProperties(value = { "profession", "position" }, allowSetters = true)
    private Set<SafetyInstruction> safetyInstructions = new HashSet<>();

    // Связь один-ко-многим с Employee (сотрудники)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "position")
    @JsonIgnoreProperties(value = { "trainings", "tasks", "position", "professions", "team" }, allowSetters = true)
    private Set<Employee> employees = new HashSet<>();

    // Методы доступа (getters/setters) с fluent-интерфейсом
    public Long getId() {
        return this.id;
    }

    public Position id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPositionName() {
        return this.positionName;
    }

    public Position positionName(String positionName) {
        this.setPositionName(positionName);
        return this;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    // Управление связью с JobDescription
    public JobDescription getJobDescription() {
        return this.jobDescription;
    }

    public void setJobDescription(JobDescription jobDescription) {
        this.jobDescription = jobDescription;
    }

    public Position jobDescription(JobDescription jobDescription) {
        this.setJobDescription(jobDescription);
        return this;
    }

    // Управление связью с SafetyInstruction
    public Set<SafetyInstruction> getSafetyInstructions() {
        return this.safetyInstructions;
    }

    public void setSafetyInstructions(Set<SafetyInstruction> safetyInstructions) {
        // Поддержание целостности связей
        if (this.safetyInstructions != null) {
            this.safetyInstructions.forEach(i -> i.setPosition(null));
        }
        if (safetyInstructions != null) {
            safetyInstructions.forEach(i -> i.setPosition(this));
        }
        this.safetyInstructions = safetyInstructions;
    }

    public Position addSafetyInstruction(SafetyInstruction safetyInstruction) {
        this.safetyInstructions.add(safetyInstruction);
        safetyInstruction.setPosition(this);
        return this;
    }

    public Position removeSafetyInstruction(SafetyInstruction safetyInstruction) {
        this.safetyInstructions.remove(safetyInstruction);
        safetyInstruction.setPosition(null);
        return this;
    }

    public Set<Employee> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<Employee> employees) {
        if (this.employees != null) {
            this.employees.forEach(i -> i.setPosition(null));
        }
        if (employees != null) {
            employees.forEach(i -> i.setPosition(this));
        }
        this.employees = employees;
    }

    public Position employees(Set<Employee> employees) {
        this.setEmployees(employees);
        return this;
    }

    public Position addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.setPosition(this);
        return this;
    }

    public Position removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.setPosition(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Position)) {
            return false;
        }
        return getId() != null && getId().equals(((Position) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Должность{" +
            "id=" + getId() +
            ", наименование='" + getPositionName() + "'" +
            "}";
    }
}
