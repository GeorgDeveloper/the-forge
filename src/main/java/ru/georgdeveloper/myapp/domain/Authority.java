package ru.georgdeveloper.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import org.springframework.data.domain.Persistable;

/**
 * Сущность "Роль" (Authority).
 * Представляет роли/права пользователей в системе.
 * Реализует интерфейс Persistable для кастомного управления состоянием персистентности.
 */
@Entity
@Table(name = "jhi_authority")
@JsonIgnoreProperties(value = { "new", "id" }) // Игнорирует поля при JSON сериализации
@SuppressWarnings("common-java:DuplicatedBlocks") // Игнорирует предупреждения о дублировании блоков
public class Authority implements Serializable, Persistable<String> {

    private static final long serialVersionUID = 1L; // Идентификатор для сериализации

    @NotNull // Валидация - поле не может быть null
    @Size(max = 50) // Ограничение длины строки
    @Id // Поле является первичным ключом
    @Column(name = "name", length = 50, nullable = false) // Настройки колонки в БД
    private String name; // Название роли (используется как ID)

    // Флаг персистентности (не сохраняется в БД)
    @org.springframework.data.annotation.Transient // Spring Transient
    @Transient // JPA Transient
    private boolean isPersisted;

    // Методы доступа (getters/setters) с fluent-интерфейсом
    public String getName() {
        return this.name;
    }

    public Authority name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Обновляет состояние персистентности после загрузки или сохранения.
     * Аннотации @PostLoad и @PostPersist гарантируют вызов после соответствующих событий JPA.
     */
    @PostLoad
    @PostPersist
    public void updateEntityState() {
        this.setIsPersisted();
    }

    /**
     * Реализация метода getId() из Persistable.
     * Возвращает имя роли в качестве ID.
     */
    @Override
    public String getId() {
        return this.name;
    }

    /**
     * Реализация метода isNew() из Persistable.
     * Определяет, является ли сущность новой (не сохраненной в БД).
     */
    @org.springframework.data.annotation.Transient
    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    /**
     * Устанавливает флаг персистентности.
     */
    public Authority setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // Переопределенные методы equals и hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Authority)) {
            return false;
        }
        return getName() != null && getName().equals(((Authority) o).getName());
    }

    @Override
    public int hashCode() {
        // Используется Objects.hashCode для null-безопасности
        return Objects.hashCode(getName());
    }

    // Переопределенный метод toString()
    @Override
    public String toString() {
        return "Authority{" + "name=" + getName() + "}";
    }
}
