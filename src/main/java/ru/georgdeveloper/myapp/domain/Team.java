package ru.georgdeveloper.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Сущность "Команда/Отдел".
 * Представляет группу сотрудников, работающих вместе над общими задачами.
 */
@Entity
@Table(name = "team")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Team implements Serializable {

    private static final long serialVersionUID = 1L; // Идентификатор для сериализации

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id; // Уникальный идентификатор команды

    @NotNull
    @Column(name = "team_name", nullable = false)
    private String teamName; // Название команды/отдела

    // Связь один-ко-многим с Employee (сотрудники в команде)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "team")
    @JsonIgnoreProperties(
        value = { "trainings", "tasks", "position", "professions", "team" },
        allowSetters = true // Игнорирование циклических ссылок при сериализации
    )
    private Set<Employee> employees = new HashSet<>();

    // Связь с User через права доступа
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<UserTeamAccess> userAccesses = new HashSet<>();

    // Методы доступа с fluent-интерфейсом
    public Long getId() {
        return this.id;
    }

    public Team id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamName() {
        return this.teamName;
    }

    public Team teamName(String teamName) {
        this.setTeamName(teamName);
        return this;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Set<UserTeamAccess> getUserAccesses() {
        return userAccesses;
    }

    public void setUserAccesses(Set<UserTeamAccess> userAccesses) {
        this.userAccesses = userAccesses;
    }

    /**
     * Управление связью с сотрудниками команды.
     * Поддерживает целостность двусторонней связи.
     */
    public Set<Employee> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<Employee> employees) {
        // Очищаем предыдущие связи
        if (this.employees != null) {
            this.employees.forEach(emp -> emp.setTeam(null));
        }
        // Устанавливаем новые связи
        if (employees != null) {
            employees.forEach(emp -> emp.setTeam(this));
        }
        this.employees = employees;
    }

    public Team employees(Set<Employee> employees) {
        this.setEmployees(employees);
        return this;
    }

    /**
     * Добавляет сотрудника в команду с обновлением двусторонней связи.
     */
    public Team addEmployee(Employee employee) {
        this.employees.add(employee);
        employee.setTeam(this);
        return this;
    }

    /**
     * Удаляет сотрудника из команды с обновлением двусторонней связи.
     */
    public Team removeEmployee(Employee employee) {
        this.employees.remove(employee);
        employee.setTeam(null);
        return this;
    }

    // equals и hashCode для корректной работы с коллекциями
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team)) return false;
        return getId() != null && getId().equals(((Team) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode(); // Рекомендуемый подход для JPA сущностей
    }

    @Override
    public String toString() {
        return "Команда{" + "id=" + getId() + ", наименование='" + getTeamName() + "'" + "}";
    }
}
