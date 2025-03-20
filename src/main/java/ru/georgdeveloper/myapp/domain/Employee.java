package ru.georgdeveloper.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @NotNull
    @Column(name = "employee_number", nullable = false, unique = true)
    private String employeeNumber;

    @NotNull
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<Training> trainings = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<Task> tasks = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "jobDescription", "safetyInstructions", "employees" }, allowSetters = true)
    private Position position;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_employee__profession",
        joinColumns = @JoinColumn(name = "employee_id"),
        inverseJoinColumns = @JoinColumn(name = "profession_id")
    )
    @JsonIgnoreProperties(value = { "protectiveEquipments", "additionalTrainings", "safetyInstructions", "employees" }, allowSetters = true)
    private Set<Profession> professions = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "employees" }, allowSetters = true)
    private Team team;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Employee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Employee firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Employee lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public Employee birthDate(LocalDate birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmployeeNumber() {
        return this.employeeNumber;
    }

    public Employee employeeNumber(String employeeNumber) {
        this.setEmployeeNumber(employeeNumber);
        return this;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public LocalDate getHireDate() {
        return this.hireDate;
    }

    public Employee hireDate(LocalDate hireDate) {
        this.setHireDate(hireDate);
        return this;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public Set<Training> getTrainings() {
        return this.trainings;
    }

    public void setTrainings(Set<Training> trainings) {
        if (this.trainings != null) {
            this.trainings.forEach(i -> i.setEmployee(null));
        }
        if (trainings != null) {
            trainings.forEach(i -> i.setEmployee(this));
        }
        this.trainings = trainings;
    }

    public Employee trainings(Set<Training> trainings) {
        this.setTrainings(trainings);
        return this;
    }

    public Employee addTraining(Training training) {
        this.trainings.add(training);
        training.setEmployee(this);
        return this;
    }

    public Employee removeTraining(Training training) {
        this.trainings.remove(training);
        training.setEmployee(null);
        return this;
    }

    public Set<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(Set<Task> tasks) {
        if (this.tasks != null) {
            this.tasks.forEach(i -> i.setEmployee(null));
        }
        if (tasks != null) {
            tasks.forEach(i -> i.setEmployee(this));
        }
        this.tasks = tasks;
    }

    public Employee tasks(Set<Task> tasks) {
        this.setTasks(tasks);
        return this;
    }

    public Employee addTask(Task task) {
        this.tasks.add(task);
        task.setEmployee(this);
        return this;
    }

    public Employee removeTask(Task task) {
        this.tasks.remove(task);
        task.setEmployee(null);
        return this;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Employee position(Position position) {
        this.setPosition(position);
        return this;
    }

    public Set<Profession> getProfessions() {
        return this.professions;
    }

    public void setProfessions(Set<Profession> professions) {
        this.professions = professions;
    }

    public Employee professions(Set<Profession> professions) {
        this.setProfessions(professions);
        return this;
    }

    public Employee addProfession(Profession profession) {
        this.professions.add(profession);
        return this;
    }

    public Employee removeProfession(Profession profession) {
        this.professions.remove(profession);
        return this;
    }

    public Team getTeam() {
        return this.team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Employee team(Team team) {
        this.setTeam(team);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return getId() != null && getId().equals(((Employee) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Employee{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", employeeNumber='" + getEmployeeNumber() + "'" +
            ", hireDate='" + getHireDate() + "'" +
            "}";
    }
}
