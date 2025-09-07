package ru.georgdeveloper.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Сущность "Процедуры и Документации".
 * Содержит информацию о процедурах и документах, их дате введения
 * и связях с профессиями и должностями.
 */
@Entity
@Table(name = "procedure_document")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProcedureDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "document_name", nullable = false)
    private String documentName;

    @NotNull
    @Column(name = "introduction_date", nullable = false)
    private LocalDate introductionDate;

    @Column(name = "description")
    private String description;

    @Column(name = "pdf_file_name")
    private String pdfFileName;

    @Column(name = "pdf_file_content_type")
    private String pdfFileContentType;

    @Lob
    @Column(name = "pdf_file")
    private byte[] pdfFile;

    // Связь многие-к-одному с Profession (профессия)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "protectiveEquipments", "additionalTrainings", "safetyInstructions", "employees", "procedureDocuments" },
        allowSetters = true
    )
    private Profession profession;

    // Связь многие-к-одному с Position (должность)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "jobDescription", "safetyInstructions", "employees", "procedureDocuments" }, allowSetters = true)
    private Position position;

    // Методы доступа (getters/setters) с fluent-интерфейсом
    public Long getId() {
        return this.id;
    }

    public ProcedureDocument id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentName() {
        return this.documentName;
    }

    public ProcedureDocument documentName(String documentName) {
        this.setDocumentName(documentName);
        return this;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public LocalDate getIntroductionDate() {
        return this.introductionDate;
    }

    public ProcedureDocument introductionDate(LocalDate introductionDate) {
        this.setIntroductionDate(introductionDate);
        return this;
    }

    public void setIntroductionDate(LocalDate introductionDate) {
        this.introductionDate = introductionDate;
    }

    public String getDescription() {
        return this.description;
    }

    public ProcedureDocument description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPdfFileName() {
        return this.pdfFileName;
    }

    public ProcedureDocument pdfFileName(String pdfFileName) {
        this.setPdfFileName(pdfFileName);
        return this;
    }

    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
    }

    public String getPdfFileContentType() {
        return this.pdfFileContentType;
    }

    public ProcedureDocument pdfFileContentType(String pdfFileContentType) {
        this.setPdfFileContentType(pdfFileContentType);
        return this;
    }

    public void setPdfFileContentType(String pdfFileContentType) {
        this.pdfFileContentType = pdfFileContentType;
    }

    public byte[] getPdfFile() {
        return this.pdfFile;
    }

    public ProcedureDocument pdfFile(byte[] pdfFile) {
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

    public ProcedureDocument profession(Profession profession) {
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

    public ProcedureDocument position(Position position) {
        this.setPosition(position);
        return this;
    }

    // equals, hashCode и toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProcedureDocument)) return false;
        return getId() != null && getId().equals(((ProcedureDocument) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return (
            "Процедуры и Документации{" +
            "id=" +
            getId() +
            ", наименование='" +
            getDocumentName() +
            "'" +
            ", дата введения='" +
            getIntroductionDate() +
            "'" +
            ", описание='" +
            getDescription() +
            "'" +
            ", pdf файл='" +
            getPdfFileName() +
            "'" +
            "}"
        );
    }
}
