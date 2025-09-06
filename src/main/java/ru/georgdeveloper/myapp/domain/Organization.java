package ru.georgdeveloper.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import ru.georgdeveloper.myapp.domain.enumeration.EmployeesCountRange;

/**
 * Сущность "Организация".
 */
@Entity
@Table(name = "organization")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    // Блок 1: Основная информация
    @NotNull
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotNull
    @Column(name = "short_name", nullable = false)
    private String shortName;

    // Сферы деятельности: хранить как текст со списком значений, разделенных запятыми
    @Column(name = "activity_areas")
    private String activityAreas;

    @NotNull
    @Column(name = "tagline", nullable = false, length = 1024)
    private String tagline;

    // Блок 2: Юридические и контактные реквизиты
    @Column(name = "legal_address")
    private String legalAddress;

    @Column(name = "actual_address")
    private String actualAddress;

    @Column(name = "inn")
    private String inn;

    @Column(name = "kpp")
    private String kpp;

    @Column(name = "ogrn")
    private String ogrn;

    @Column(name = "okpo")
    private String okpo;

    // Банковские реквизиты
    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_bik")
    private String bankBik;

    @Column(name = "bank_corr_account")
    private String bankCorrAccount;

    @Column(name = "bank_settlement_account")
    private String bankSettlementAccount;

    // Блок 3: Контакты
    @Column(name = "phone_main")
    private String phoneMain;

    @Column(name = "phone_sales")
    private String phoneSales;

    @Column(name = "phone_support")
    private String phoneSupport;

    @Column(name = "email_main")
    private String emailMain;

    @Column(name = "email_partners")
    private String emailPartners;

    @Column(name = "email_support")
    private String emailSupport;

    @Column(name = "website")
    private String website;

    // Блок 4: Дополнительная информация
    @Column(name = "founded_year")
    private Integer foundedYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "employees_count_range")
    private EmployeesCountRange employeesCountRange;

    // Ключевые лица, продукты/услуги, клиенты/партнеры — простые текстовые поля (JSON/CSV по необходимости)
    @Column(name = "key_persons", length = 4000)
    private String keyPersons;

    @Column(name = "products", length = 4000)
    private String products;

    @Column(name = "partners", length = 4000)
    private String partners;

    // Связь с Team: одна организация — много команд
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "organization")
    @JsonIgnoreProperties(value = { "employees", "userAccesses", "users", "organization" }, allowSetters = true)
    private Set<Team> teams = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getActivityAreas() {
        return activityAreas;
    }

    public void setActivityAreas(String activityAreas) {
        this.activityAreas = activityAreas;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getLegalAddress() {
        return legalAddress;
    }

    public void setLegalAddress(String legalAddress) {
        this.legalAddress = legalAddress;
    }

    public String getActualAddress() {
        return actualAddress;
    }

    public void setActualAddress(String actualAddress) {
        this.actualAddress = actualAddress;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getKpp() {
        return kpp;
    }

    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    public String getOgrn() {
        return ogrn;
    }

    public void setOgrn(String ogrn) {
        this.ogrn = ogrn;
    }

    public String getOkpo() {
        return okpo;
    }

    public void setOkpo(String okpo) {
        this.okpo = okpo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankBik() {
        return bankBik;
    }

    public void setBankBik(String bankBik) {
        this.bankBik = bankBik;
    }

    public String getBankCorrAccount() {
        return bankCorrAccount;
    }

    public void setBankCorrAccount(String bankCorrAccount) {
        this.bankCorrAccount = bankCorrAccount;
    }

    public String getBankSettlementAccount() {
        return bankSettlementAccount;
    }

    public void setBankSettlementAccount(String bankSettlementAccount) {
        this.bankSettlementAccount = bankSettlementAccount;
    }

    public String getPhoneMain() {
        return phoneMain;
    }

    public void setPhoneMain(String phoneMain) {
        this.phoneMain = phoneMain;
    }

    public String getPhoneSales() {
        return phoneSales;
    }

    public void setPhoneSales(String phoneSales) {
        this.phoneSales = phoneSales;
    }

    public String getPhoneSupport() {
        return phoneSupport;
    }

    public void setPhoneSupport(String phoneSupport) {
        this.phoneSupport = phoneSupport;
    }

    public String getEmailMain() {
        return emailMain;
    }

    public void setEmailMain(String emailMain) {
        this.emailMain = emailMain;
    }

    public String getEmailPartners() {
        return emailPartners;
    }

    public void setEmailPartners(String emailPartners) {
        this.emailPartners = emailPartners;
    }

    public String getEmailSupport() {
        return emailSupport;
    }

    public void setEmailSupport(String emailSupport) {
        this.emailSupport = emailSupport;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getFoundedYear() {
        return foundedYear;
    }

    public void setFoundedYear(Integer foundedYear) {
        this.foundedYear = foundedYear;
    }

    public EmployeesCountRange getEmployeesCountRange() {
        return employeesCountRange;
    }

    public void setEmployeesCountRange(EmployeesCountRange employeesCountRange) {
        this.employeesCountRange = employeesCountRange;
    }

    public String getKeyPersons() {
        return keyPersons;
    }

    public void setKeyPersons(String keyPersons) {
        this.keyPersons = keyPersons;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getPartners() {
        return partners;
    }

    public void setPartners(String partners) {
        this.partners = partners;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }
}
