package ru.georgdeveloper.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Сущность "Инструкция по технике безопасности (охране труда)".
 * Содержит информацию о правилах безопасности, их дате введения
 * и связях с профессиями и должностями.
 */
@Entity // Указывает, что это JPA сущность
@Table(name = "safety_instruction") // Связывает с таблицей в БД
@SuppressWarnings("common-java:DuplicatedBlocks") // Игнорирует предупреждения о дублировании блоков
public class SafetyInstruction implements Serializable {

    private static final long serialVersionUID = 1L; // Идентификатор для сериализации

    @Id // Поле является первичным ключом
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator") // Стратегия генерации ID
    @SequenceGenerator(name = "sequenceGenerator") // Используемый генератор последовательностей
    @Column(name = "id") // Связь с колонкой в таблице
    private Long id; // Уникальный идентификатор инструкции

    @NotNull // Валидация - поле не может быть null
    @Column(name = "instruction_name", nullable = false) // Настройки колонки в БД
    private String instructionName; // Наименование инструкции

    @NotNull
    @Column(name = "introduction_date", nullable = false)
    private LocalDate introductionDate; // Дата введения инструкции

    @Column(name = "pdf_file_name")
    private String pdfFileName; // Имя PDF-файла инструкции

    @Column(name = "pdf_file_content_type")
    private String pdfFileContentType; // MIME-тип PDF-файла

    @Lob
    @Column(name = "pdf_file")
    private byte[] pdfFile; // Содержимое PDF-файла

    // Связь многие-к-одному с Profession (профессия)
    @ManyToOne(fetch = FetchType.LAZY) // Ленивая загрузка
    @JsonIgnoreProperties(
        value = { "protectiveEquipments", "additionalTrainings", "safetyInstructions", "employees" },
        allowSetters = true // Игнорирует указанные поля при JSON сериализации
    )
    private Profession profession; // Профессия, для которой предназначена инструкция

    // Связь многие-к-одному с Position (должность)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "jobDescription", "safetyInstructions", "employees" }, allowSetters = true)
    private Position position; // Должность, для которой предназначена инструкция

    // Методы доступа (getters/setters) с fluent-интерфейсом
    public Long getId() {
        return this.id;
    }

    public SafetyInstruction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstructionName() {
        return this.instructionName;
    }

    public SafetyInstruction instructionName(String instructionName) {
        this.setInstructionName(instructionName);
        return this;
    }

    public void setInstructionName(String instructionName) {
        this.instructionName = instructionName;
    }

    public LocalDate getIntroductionDate() {
        return this.introductionDate;
    }

    public SafetyInstruction introductionDate(LocalDate introductionDate) {
        this.setIntroductionDate(introductionDate);
        return this;
    }

    public void setIntroductionDate(LocalDate introductionDate) {
        this.introductionDate = introductionDate;
    }

    public String getPdfFileName() {
        return this.pdfFileName;
    }

    public SafetyInstruction pdfFileName(String pdfFileName) {
        this.setPdfFileName(pdfFileName);
        return this;
    }

    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
    }

    public String getPdfFileContentType() {
        return this.pdfFileContentType;
    }

    public SafetyInstruction pdfFileContentType(String pdfFileContentType) {
        this.setPdfFileContentType(pdfFileContentType);
        return this;
    }

    public void setPdfFileContentType(String pdfFileContentType) {
        this.pdfFileContentType = pdfFileContentType;
    }

    public byte[] getPdfFile() {
        return this.pdfFile;
    }

    public SafetyInstruction pdfFile(byte[] pdfFile) {
        this.setPdfFile(pdfFile);
        return this;
    }

    public void setPdfFile(byte[] pdfFile) {
        this.pdfFile = pdfFile;
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

    public SafetyInstruction profession(Profession profession) {
        this.setProfession(profession);
        return this;
    }

    /**
     * Управление связью с Position.
     */
    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public SafetyInstruction position(Position position) {
        this.setPosition(position);
        return this;
    }

    // equals, hashCode и toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SafetyInstruction)) return false;
        return getId() != null && getId().equals(((SafetyInstruction) o).getId());
    }

    @Override
    public int hashCode() {
        // Рекомендуемая реализация для JPA сущностей
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "Инструкция по ОТ{" +
            "id=" +
            getId() +
            ", наименование='" +
            getInstructionName() +
            "'" +
            ", дата введения инструкции='" +
            getIntroductionDate() +
            "'" +
            ", pdf файл='" +
            getPdfFileName() +
            "'" +
            "}"
        );
    }
}
