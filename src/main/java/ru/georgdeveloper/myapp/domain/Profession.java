package ru.georgdeveloper.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Сущность "Профессия".
 * Содержит информацию о профессиях и их связи с оборудованием, обучением и сотрудниками.
 */
@Entity
@Table(name = "profession")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Profession implements Serializable {

    private static final long serialVersionUID = 1L; // Идентификатор для сериализации

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id; // Уникальный идентификатор профессии

    @NotNull
    @Column(name = "profession_name", nullable = false)
    private String professionName; // Название профессии

    // Связи с другими сущностями:

    // Один-ко-многим: Средства индивидуальной защиты
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profession")
    @JsonIgnoreProperties(value = { "profession" }, allowSetters = true)
    private Set<ProtectiveEquipment> protectiveEquipments = new HashSet<>();

    // Один-ко-многим: Дополнительное обучение
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profession")
    @JsonIgnoreProperties(value = { "profession" }, allowSetters = true)
    private Set<AdditionalTraining> additionalTrainings = new HashSet<>();

    // Один-ко-многим: Инструкции по безопасности
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "profession")
    @JsonIgnoreProperties(value = { "profession", "position" }, allowSetters = true)
    private Set<SafetyInstruction> safetyInstructions = new HashSet<>();

    // Многие-ко-многим: Сотрудники
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "professions")
    @JsonIgnoreProperties(value = { "trainings", "tasks", "position", "professions", "team" }, allowSetters = true)
    private Set<Employee> employees = new HashSet<>();

    // Методы доступа и управления связями:

    public Long getId() {
        return this.id;
    }

    public Profession id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfessionName() {
        return this.professionName;
    }

    public Profession professionName(String professionName) {
        this.setProfessionName(professionName);
        return this;
    }

    public void setProfessionName(String professionName) {
        this.professionName = professionName;
    }

    // Методы для управления связью с ProtectiveEquipment
    public Set<ProtectiveEquipment> getProtectiveEquipments() {
        return this.protectiveEquipments;
    }

    public void setProtectiveEquipments(Set<ProtectiveEquipment> protectiveEquipments) {
        // Поддержание целостности связей
        if (this.protectiveEquipments != null) {
            this.protectiveEquipments.forEach(i -> i.setProfession(null));
        }
        if (protectiveEquipments != null) {
            protectiveEquipments.forEach(i -> i.setProfession(this));
        }
        this.protectiveEquipments = protectiveEquipments;
    }

    public Profession protectiveEquipments(Set<ProtectiveEquipment> protectiveEquipments) {
        this.setProtectiveEquipments(protectiveEquipments);
        return this;
    }

    public Profession addProtectiveEquipment(ProtectiveEquipment protectiveEquipment) {
        this.protectiveEquipments.add(protectiveEquipment);
        protectiveEquipment.setProfession(this);
        return this;
    }

    public Profession removeProtectiveEquipment(ProtectiveEquipment protectiveEquipment) {
        this.protectiveEquipments.remove(protectiveEquipment);
        protectiveEquipment.setProfession(null);
        return this;
    }

    public Set<AdditionalTraining> getAdditionalTrainings() {
        return this.additionalTrainings;
    }

    public void setAdditionalTrainings(Set<AdditionalTraining> additionalTrainings) {
        if (this.additionalTrainings != null) {
            this.additionalTrainings.forEach(i -> i.setProfession(null));
        }
        if (additionalTrainings != null) {
            additionalTrainings.forEach(i -> i.setProfession(this));
        }
        this.additionalTrainings = additionalTrainings;
    }

    public Profession additionalTrainings(Set<AdditionalTraining> additionalTrainings) {
        this.setAdditionalTrainings(additionalTrainings);
        return this;
    }

    public Profession addAdditionalTraining(AdditionalTraining additionalTraining) {
        this.additionalTrainings.add(additionalTraining);
        additionalTraining.setProfession(this);
        return this;
    }

    public Profession removeAdditionalTraining(AdditionalTraining additionalTraining) {
        this.additionalTrainings.remove(additionalTraining);
        additionalTraining.setProfession(null);
        return this;
    }

    public Set<SafetyInstruction> getSafetyInstructions() {
        return this.safetyInstructions;
    }

    public void setSafetyInstructions(Set<SafetyInstruction> safetyInstructions) {
        if (this.safetyInstructions != null) {
            this.safetyInstructions.forEach(i -> i.setProfession(null));
        }
        if (safetyInstructions != null) {
            safetyInstructions.forEach(i -> i.setProfession(this));
        }
        this.safetyInstructions = safetyInstructions;
    }

    public Profession safetyInstructions(Set<SafetyInstruction> safetyInstructions) {
        this.setSafetyInstructions(safetyInstructions);
        return this;
    }

    public Profession addSafetyInstruction(SafetyInstruction safetyInstruction) {
        this.safetyInstructions.add(safetyInstruction);
        safetyInstruction.setProfession(this);
        return this;
    }

    public Profession removeSafetyInstruction(SafetyInstruction safetyInstruction) {
        this.safetyInstructions.remove(safetyInstruction);
        safetyInstruction.setProfession(null);
        return this;
    }

    // Методы для управления связью многие-ко-многим с Employee
    public Set<Employee> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<Employee> employees) {
        if (this.employees != null) {
            this.employees.forEach(i -> i.removeProfession(this));
        }
        if (employees != null) {
            employees.forEach(i -> i.addProfession(this));
        }
        this.employees = employees;
    }

    public Profession employees(Set<Employee> employees) {
        this.setEmployees(employees);
        return this;
    }

    public Profession addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.getProfessions().add(this);
        return this;
    }

    public Profession removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.getProfessions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Profession)) {
            return false;
        }
        return getId() != null && getId().equals(((Profession) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Профессия{" +
            "id=" + getId() +
            ", наименование='" + getProfessionName() + "'" +
            "}";
    }
}
