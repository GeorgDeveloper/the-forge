package ru.georgdeveloper.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Абстрактный базовый класс для сущностей с аудитом изменений.
 * Содержит поля для отслеживания:
 * - Кто создал запись
 * - Когда создана запись
 * - Кто последним изменил запись
 * - Когда последний раз изменена запись
 *
 * @param <T> тип идентификатора сущности
 */
@MappedSuperclass // Указывает, что это родительский класс для JPA сущностей
@EntityListeners(AuditingEntityListener.class) // Включает автоматическое заполнение аудит-полей
@JsonIgnoreProperties(
    value = { "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate" },
    allowGetters = true // Разрешаем получение значений через getter'ы
)
public abstract class AbstractAuditingEntity<T> implements Serializable {

    private static final long serialVersionUID = 1L; // Идентификатор для сериализации

    /**
     * Абстрактный метод для получения ID сущности
     */
    public abstract T getId();

    // Поле: кто создал запись
    @CreatedBy // Автоматически заполняется пользователем, создавшим запись
    @Column(name = "created_by", nullable = false, length = 50, updatable = false)
    private String createdBy;

    // Поле: когда создана запись
    @CreatedDate // Автоматически заполняется датой создания
    @Column(name = "created_date", updatable = false)
    private Instant createdDate = Instant.now(); // Значение по умолчанию - текущее время

    // Поле: кто последний изменил запись
    @LastModifiedBy // Автоматически заполняется пользователем, изменившим запись
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    // Поле: когда последний раз изменена запись
    @LastModifiedDate // Автоматически заполняется датой изменения
    @Column(name = "last_modified_date")
    private Instant lastModifiedDate = Instant.now(); // Значение по умолчанию - текущее время

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
