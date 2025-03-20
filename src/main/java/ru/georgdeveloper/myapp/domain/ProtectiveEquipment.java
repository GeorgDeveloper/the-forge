package ru.georgdeveloper.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A ProtectiveEquipment.
 */
@Entity
@Table(name = "protective_equipment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProtectiveEquipment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "equipment_name", nullable = false)
    private String equipmentName;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "issuance_frequency", nullable = false)
    private Integer issuanceFrequency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "protectiveEquipments", "additionalTrainings", "safetyInstructions", "employees" }, allowSetters = true)
    private Profession profession;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProtectiveEquipment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEquipmentName() {
        return this.equipmentName;
    }

    public ProtectiveEquipment equipmentName(String equipmentName) {
        this.setEquipmentName(equipmentName);
        return this;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public ProtectiveEquipment quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getIssuanceFrequency() {
        return this.issuanceFrequency;
    }

    public ProtectiveEquipment issuanceFrequency(Integer issuanceFrequency) {
        this.setIssuanceFrequency(issuanceFrequency);
        return this;
    }

    public void setIssuanceFrequency(Integer issuanceFrequency) {
        this.issuanceFrequency = issuanceFrequency;
    }

    public Profession getProfession() {
        return this.profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public ProtectiveEquipment profession(Profession profession) {
        this.setProfession(profession);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProtectiveEquipment)) {
            return false;
        }
        return getId() != null && getId().equals(((ProtectiveEquipment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProtectiveEquipment{" +
            "id=" + getId() +
            ", equipmentName='" + getEquipmentName() + "'" +
            ", quantity=" + getQuantity() +
            ", issuanceFrequency=" + getIssuanceFrequency() +
            "}";
    }
}
