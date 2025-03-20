package ru.georgdeveloper.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A SafetyInstruction.
 */
@Entity
@Table(name = "safety_instruction")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SafetyInstruction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "instruction_name", nullable = false)
    private String instructionName;

    @NotNull
    @Column(name = "introduction_date", nullable = false)
    private LocalDate introductionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "protectiveEquipments", "additionalTrainings", "safetyInstructions", "employees" }, allowSetters = true)
    private Profession profession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "jobDescription", "safetyInstructions", "employees" }, allowSetters = true)
    private Position position;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SafetyInstruction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstructionName() {
        return this.instructionName;
    }

    public SafetyInstruction instructionName(String instructionName) {
        this.setInstructionName(instructionName);
        return this;
    }

    public void setInstructionName(String instructionName) {
        this.instructionName = instructionName;
    }

    public LocalDate getIntroductionDate() {
        return this.introductionDate;
    }

    public SafetyInstruction introductionDate(LocalDate introductionDate) {
        this.setIntroductionDate(introductionDate);
        return this;
    }

    public void setIntroductionDate(LocalDate introductionDate) {
        this.introductionDate = introductionDate;
    }

    public Profession getProfession() {
        return this.profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public SafetyInstruction profession(Profession profession) {
        this.setProfession(profession);
        return this;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public SafetyInstruction position(Position position) {
        this.setPosition(position);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SafetyInstruction)) {
            return false;
        }
        return getId() != null && getId().equals(((SafetyInstruction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SafetyInstruction{" +
            "id=" + getId() +
            ", instructionName='" + getInstructionName() + "'" +
            ", introductionDate='" + getIntroductionDate() + "'" +
            "}";
    }
}
