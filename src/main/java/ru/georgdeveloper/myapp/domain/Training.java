package ru.georgdeveloper.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Training.
 */
@Entity
@Table(name = "training")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Training implements Serializable {

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
    @Column(name = "last_training_date", nullable = false)
    private LocalDate lastTrainingDate;

    @NotNull
    @Column(name = "validity_period", nullable = false)
    private Integer validityPeriod;

    @Column(name = "next_training_date")
    private LocalDate nextTrainingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "trainings", "tasks", "position", "professions", "team" }, allowSetters = true)
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Training)) {
            return false;
        }
        return getId() != null && getId().equals(((Training) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Training{" +
            "id=" + getId() +
            ", trainingName='" + getTrainingName() + "'" +
            ", lastTrainingDate='" + getLastTrainingDate() + "'" +
            ", validityPeriod=" + getValidityPeriod() +
            ", nextTrainingDate='" + getNextTrainingDate() + "'" +
            "}";
    }
}
