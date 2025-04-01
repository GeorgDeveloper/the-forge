package ru.georgdeveloper.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.BatchSize;
import ru.georgdeveloper.myapp.config.Constants;

/**
 * Сущность "Пользователь".
 * Представляет учетную запись пользователя системы с основными данными,
 * учетными данными и правами доступа.
 */
@Entity
@Table(name = "jhi_user") // Специальное имя таблицы для совместимости с JHipster
public class User extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L; // Идентификатор версии для сериализации

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id; // Уникальный идентификатор пользователя

    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX) // Валидация по регулярному выражению
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login; // Уникальный логин пользователя

    @JsonIgnore // Исключаем пароль из JSON
    @NotNull
    @Size(min = 60, max = 60) // Хеш пароля фиксированной длины
    @Column(name = "password_hash", length = 60, nullable = false)
    private String password; // Захешированный пароль

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName; // Имя пользователя

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName; // Фамилия пользователя

    @Email // Валидация email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    private String email; // Электронная почта

    @NotNull
    @Column(nullable = false)
    private boolean activated = false; // Флаг активации учетной записи

    @Size(min = 2, max = 10)
    @Column(name = "lang_key", length = 10)
    private String langKey; // Языковая настройка

    @Size(max = 256)
    @Column(name = "image_url", length = 256)
    private String imageUrl; // URL аватара пользователя

    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    private String activationKey; // Ключ активации учетной записи

    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    @JsonIgnore
    private String resetKey; // Ключ сброса пароля

    @Column(name = "reset_date")
    private Instant resetDate = null; // Дата сброса пароля

    // Связь многие-ко-многим с ролями (Authority)
    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "jhi_user_authority",
        joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") },
        inverseJoinColumns = { @JoinColumn(name = "authority_name", referencedColumnName = "name") }
    )
    @BatchSize(size = 20) // Оптимизация загрузки для пакетной обработки
    private Set<Authority> authorities = new HashSet<>();

    // Связь с Teams через права доступа
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<UserTeamAccess> teamAccesses = new HashSet<>();

    // Методы доступа и модификации полей

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    /**
     * Устанавливает логин, приводя его к нижнему регистру.
     * Это обеспечивает регистронезависимость при аутентификации.
     */
    public void setLogin(String login) {
        this.login = StringUtils.lowerCase(login, Locale.ENGLISH);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public Instant getResetDate() {
        return resetDate;
    }

    public void setResetDate(Instant resetDate) {
        this.resetDate = resetDate;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public Set<UserTeamAccess> getTeamAccesses() {
        return teamAccesses;
    }

    public void setTeamAccesses(Set<UserTeamAccess> teamAccesses) {
        this.teamAccesses = teamAccesses;
    }

    /**
     * Проверяет, имеет ли пользователь указанную роль/право.
     */
    public boolean hasAuthority(String authority) {
        return authorities.stream().anyMatch(auth -> auth.getName().equals(authority));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        return id != null && id.equals(((User) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "User{" +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", activated='" + activated + '\'' +
            ", langKey='" + langKey + '\'' +
            ", activationKey='" + activationKey + '\'' +
            "}";
    }
}
