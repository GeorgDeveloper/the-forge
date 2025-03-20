package ru.georgdeveloper.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A AdditionalTraining.
 */
@Entity
@Table(name = "additional_training")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdditionalTraining implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "training_name", nullable = false)
    private String trainingName;

    @NotNull
    @Column(name = "training_date", nullable = false)
    private LocalDate trainingDate;

    @NotNull
    @Column(name = "validity_period", nullable = false)
    private Integer validityPeriod;

    @Column(name = "next_training_date")
    private LocalDate nextTrainingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "protectiveEquipments", "additionalTrainings", "safetyInstructions", "employees" }, allowSetters = true)
    private Profession profession;

    // jhipster-needle-entity-add-field - JHipster will add fields here

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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

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
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdditionalTraining{" +
            "id=" + getId() +
            ", trainingName='" + getTrainingName() + "'" +
            ", trainingDate='" + getTrainingDate() + "'" +
            ", validityPeriod=" + getValidityPeriod() +
            ", nextTrainingDate='" + getNextTrainingDate() + "'" +
            "}";
    }
}
