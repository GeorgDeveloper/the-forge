package ru.georgdeveloper.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * Сущность "Средства индивидуальной защиты".
 * Содержит информацию о средствах индивидуальной защиты, его количестве и частоте выдачи,
 * а также связь с профессией, для которой оно предназначено.
 */
@Entity // Указывает, что это JPA сущность
@Table(name = "protective_equipment") // Связывает с таблицей в БД
@SuppressWarnings("common-java:DuplicatedBlocks") // Игнорирует предупреждения о дублировании блоков
public class ProtectiveEquipment implements Serializable {

    private static final long serialVersionUID = 1L; // Идентификатор для сериализации

    @Id // Поле является первичным ключом
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator") // Стратегия генерации ID
    @SequenceGenerator(name = "sequenceGenerator") // Используемый генератор последовательностей
    @Column(name = "id") // Связь с колонкой в таблице
    private Long id; // Уникальный идентификатор

    @NotNull // Валидация - поле не может быть null
    @Column(name = "equipment_name", nullable = false) // Настройки колонки в БД
    private String equipmentName; // Наименование средства защиты

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity; // Количество доступных единиц

    @NotNull
    @Column(name = "issuance_frequency", nullable = false)
    private Integer issuanceFrequency; // Частота выдачи (в месяцах)

    // Связь многие-к-одному с Profession (профессия)
    @ManyToOne(fetch = FetchType.LAZY) // Ленивая загрузка
    @JsonIgnoreProperties(
        value = { "protectiveEquipments", "additionalTrainings", "safetyInstructions", "employees" },
        allowSetters = true // Игнорирует указанные поля при JSON сериализации
    )
    private Profession profession; // Профессия, для которой предназначено средство защиты

    // Методы доступа (getters/setters) с fluent-интерфейсом
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

    /**
     * Управление связью с Profession.
     */
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

    // equals, hashCode и toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProtectiveEquipment)) return false;
        return getId() != null && getId().equals(((ProtectiveEquipment) o).getId());
    }

    @Override
    public int hashCode() {
        // Рекомендуемая реализация для JPA сущностей
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "Норма выдачи СИЗ{" +
            "id=" +
            getId() +
            ", наименование='" +
            getEquipmentName() +
            "'" +
            ", количество=" +
            getQuantity() +
            ", частота выдачи=" +
            getIssuanceFrequency() +
            "}"
        );
    }
}
