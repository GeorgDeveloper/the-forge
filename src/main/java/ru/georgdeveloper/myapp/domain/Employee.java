package ru.georgdeveloper.myapp.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Сущность "Сотрудник".
 * Содержит основную информацию о сотруднике и его связях с другими сущностями.
 */
@Entity
@Table(name = "employee")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L; // Идентификатор для сериализации

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id; // Уникальный идентификатор

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName; // Имя сотрудника

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName; // Фамилия сотрудника

    @NotNull
    @Column(name = "birth_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate; // Дата рождения

    @NotNull
    @Column(name = "employee_number", nullable = false, unique = true)
    private String employeeNumber; // Уникальный табельный номер

    @NotNull
    @Column(name = "hire_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate; // Дата приема на работу

    // Вычисляемое поле - дата последнего инструктажа
    @Transient // Поле не сохраняется в БД, вычисляется динамически
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("lastInstructionDate")
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private LocalDate lastInstructionDate;

    // Связь один-ко-многим с Training (обучения сотрудника)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<Training> trainings = new HashSet<>();

    // Связь один-ко-многим с Task (задачи сотрудника)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<Task> tasks = new HashSet<>();

    // Связь многие-к-одному с Position (должность)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "jobDescription", "safetyInstructions", "employees" }, allowSetters = true)
    private Position position;

    // Связь многие-ко-многим с Profession (профессии)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_employee__profession", // Название таблицы связи
        joinColumns = @JoinColumn(name = "employee_id"), // Колонка для текущей сущности
        inverseJoinColumns = @JoinColumn(name = "profession_id") // Колонка для связанной сущности
    )
    @JsonIgnoreProperties(value = { "employees" }, allowSetters = true)
    private Set<Profession> professions = new HashSet<>();

    // Связь многие-к-одному с Team (команда/отдел)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "employees" }, allowSetters = true)
    private Team team;

    // Методы доступа (getters/setters) с fluent-интерфейсом
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

    public LocalDate getLastInstructionDate() {
        return this.lastInstructionDate;
    }

    public Employee lastInstructionDate(LocalDate lastInstructionDate) {
        this.setLastInstructionDate(lastInstructionDate);
        return this;
    }

    public void setLastInstructionDate(LocalDate lastInstructionDate) {
        this.lastInstructionDate = lastInstructionDate;
    }

    public Set<Training> getTrainings() {
        return this.trainings;
    }

    // Специальные методы для управления связями
    public void setTrainings(Set<Training> trainings) {
        if (this.trainings != null) {
            this.trainings.forEach(i -> i.setEmployee(null)); // Очищаем обратные ссылки
        }
        if (trainings != null) {
            trainings.forEach(i -> i.setEmployee(this)); // Устанавливаем обратные ссылки
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
        profession.addEmployee(this);
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
        return "Сотрудник{" +
            "id=" + getId() +
            ", Имя='" + getFirstName() + "'" +
            ", Фамилия='" + getLastName() + "'" +
            ", дата рождения='" + getBirthDate() + "'" +
            ", таб номер='" + getEmployeeNumber() + "'" +
            ", дата приема на работу='" + getHireDate() + "'" +
            ", дата последнего инструктажа='" + getLastInstructionDate() + "'" +
            "}";
    }
}
