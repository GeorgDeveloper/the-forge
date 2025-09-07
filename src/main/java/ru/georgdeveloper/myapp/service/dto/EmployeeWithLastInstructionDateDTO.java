package ru.georgdeveloper.myapp.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import ru.georgdeveloper.myapp.domain.Employee;
import ru.georgdeveloper.myapp.domain.Position;
import ru.georgdeveloper.myapp.domain.Team;

/**
 * DTO для сотрудника с датой последнего инструктажа.
 * Используется для API ответов, где нужно включить вычисляемое поле.
 */
public class EmployeeWithLastInstructionDateDTO {

    private Long id;
    private String firstName;
    private String lastName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private String employeeNumber;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("lastInstructionDate")
    private LocalDate lastInstructionDate;

    // Связанные сущности
    @JsonIgnoreProperties(value = { "jobDescription", "safetyInstructions", "employees" }, allowSetters = true)
    private Position position;

    @JsonIgnoreProperties(value = { "employees" }, allowSetters = true)
    private Team team;

    // Конструктор для создания DTO из Entity
    public EmployeeWithLastInstructionDateDTO(Employee employee) {
        this.id = employee.getId();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.birthDate = employee.getBirthDate();
        this.employeeNumber = employee.getEmployeeNumber();
        this.hireDate = employee.getHireDate();
        this.lastInstructionDate = employee.getLastInstructionDate();
        this.position = employee.getPosition();
        this.team = employee.getTeam();
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public LocalDate getLastInstructionDate() {
        return lastInstructionDate;
    }

    public void setLastInstructionDate(LocalDate lastInstructionDate) {
        this.lastInstructionDate = lastInstructionDate;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
