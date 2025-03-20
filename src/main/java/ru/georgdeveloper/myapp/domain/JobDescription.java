package ru.georgdeveloper.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A JobDescription.
 */
@Entity
@Table(name = "job_description")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JobDescription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "description_name", nullable = false)
    private String descriptionName;

    @NotNull
    @Column(name = "approval_date", nullable = false)
    private LocalDate approvalDate;

    @JsonIgnoreProperties(value = { "jobDescription", "safetyInstructions", "employees" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "jobDescription")
    private Position position;

    // jhipster-needle-entity-add-field - JHipster will add fields here

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

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        if (this.position != null) {
            this.position.setJobDescription(null);
        }
        if (position != null) {
            position.setJobDescription(this);
        }
        this.position = position;
    }

    public JobDescription position(Position position) {
        this.setPosition(position);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobDescription)) {
            return false;
        }
        return getId() != null && getId().equals(((JobDescription) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobDescription{" +
            "id=" + getId() +
            ", descriptionName='" + getDescriptionName() + "'" +
            ", approvalDate='" + getApprovalDate() + "'" +
            "}";
    }
}
