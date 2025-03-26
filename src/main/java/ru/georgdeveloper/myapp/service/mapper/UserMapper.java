package ru.georgdeveloper.myapp.service.mapper;

import java.util.*;
import java.util.stream.Collectors;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;
import ru.georgdeveloper.myapp.domain.Authority;
import ru.georgdeveloper.myapp.domain.User;
import ru.georgdeveloper.myapp.service.dto.AdminUserDTO;
import ru.georgdeveloper.myapp.service.dto.UserDTO;

/**
 * Маппер для преобразования между сущностью {@link User} и DTO {@link UserDTO}.
 * В отличие от автоматически генерируемых MapStruct-мапперов, этот написан вручную.
 */
@Service // Регистрирует маппер как Spring-компонент
public class UserMapper {

    /**
     * Преобразует список пользователей в список UserDTO
     * @param users список сущностей User
     * @return список UserDTO
     */
    public List<UserDTO> usersToUserDTOs(List<User> users) {
        return users
            .stream()
            .filter(Objects::nonNull) // Фильтрация null-значений
            .map(this::userToUserDTO) // Преобразование каждого элемента
            .toList(); // Сбор в список
    }

    /**
     * Преобразует User в UserDTO
     * @param user сущность User
     * @return UserDTO
     */
    public UserDTO userToUserDTO(User user) {
        return new UserDTO(user); // Использует конструктор DTO
    }

    /**
     * Преобразует список пользователей в список AdminUserDTO
     * @param users список сущностей User
     * @return список AdminUserDTO
     */
    public List<AdminUserDTO> usersToAdminUserDTOs(List<User> users) {
        return users.stream().filter(Objects::nonNull).map(this::userToAdminUserDTO).toList();
    }

    /**
     * Преобразует User в AdminUserDTO (с расширенными полями для администрирования)
     * @param user сущность User
     * @return AdminUserDTO
     */
    public AdminUserDTO userToAdminUserDTO(User user) {
        return new AdminUserDTO(user);
    }

    /**
     * Преобразует список AdminUserDTO в список User
     * @param userDTOs список DTO
     * @return список сущностей User
     */
    public List<User> userDTOsToUsers(List<AdminUserDTO> userDTOs) {
        return userDTOs.stream().filter(Objects::nonNull).map(this::userDTOToUser).toList();
    }

    /**
     * Преобразует AdminUserDTO в User
     * @param userDTO DTO пользователя
     * @return сущность User
     */
    public User userDTOToUser(AdminUserDTO userDTO) {
        if (userDTO == null) {
            return null;
        } else {
            User user = new User();
            // Маппинг основных полей
            user.setId(userDTO.getId());
            user.setLogin(userDTO.getLogin());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setImageUrl(userDTO.getImageUrl());

            // Маппинг аудит-полей
            user.setCreatedBy(userDTO.getCreatedBy());
            user.setCreatedDate(userDTO.getCreatedDate());
            user.setLastModifiedBy(userDTO.getLastModifiedBy());
            user.setLastModifiedDate(userDTO.getLastModifiedDate());

            // Маппинг дополнительных полей
            user.setActivated(userDTO.isActivated());
            user.setLangKey(userDTO.getLangKey());

            // Преобразование authorities из строк в сущности
            Set<Authority> authorities = this.authoritiesFromStrings(userDTO.getAuthorities());
            user.setAuthorities(authorities);

            return user;
        }
    }

    /**
     * Преобразует набор строк authorities в набор сущностей Authority
     * @param authoritiesAsString набор строк с именами authorities
     * @return набор сущностей Authority
     */
    private Set<Authority> authoritiesFromStrings(Set<String> authoritiesAsString) {
        Set<Authority> authorities = new HashSet<>();

        if (authoritiesAsString != null) {
            authorities = authoritiesAsString
                .stream()
                .map(string -> {
                    Authority auth = new Authority();
                    auth.setName(string);
                    return auth;
                })
                .collect(Collectors.toSet());
        }

        return authorities;
    }

    /**
     * Создает User только с id (для связей)
     * @param id идентификатор пользователя
     * @return User только с установленным id
     */
    public User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        User user = new User();
        user.setId(id);
        return user;
    }

    /**
     * Преобразует User в UserDTO только с id
     * @param user сущность User
     * @return UserDTO только с id
     */
    @Named("id") // Имя для ссылки в других мапперах
    @BeanMapping(ignoreByDefault = true) // Игнорировать все поля по умолчанию
    @Mapping(target = "id", source = "id") // Маппить только id
    public UserDTO toDtoId(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDto = new UserDTO();
        userDto.setId(user.getId());
        return userDto;
    }

    /**
     * Преобразует набор User в набор UserDTO только с id
     * @param users набор User
     * @return набор UserDTO только с id
     */
    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    public Set<UserDTO> toDtoIdSet(Set<User> users) {
        if (users == null) {
            return Collections.emptySet();
        }

        Set<UserDTO> userSet = new HashSet<>();
        for (User userEntity : users) {
            userSet.add(this.toDtoId(userEntity));
        }

        return userSet;
    }

    /**
     * Преобразует User в UserDTO с id и login
     * @param user сущность User
     * @return UserDTO с id и login
     */
    @Named("login")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    public UserDTO toDtoLogin(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDto = new UserDTO();
        userDto.setId(user.getId());
        userDto.setLogin(user.getLogin());
        return userDto;
    }

    /**
     * Преобразует набор User в набор UserDTO с id и login
     * @param users набор User
     * @return набор UserDTO с id и login
     */
    @Named("loginSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    public Set<UserDTO> toDtoLoginSet(Set<User> users) {
        if (users == null) {
            return Collections.emptySet();
        }

        Set<UserDTO> userSet = new HashSet<>();
        for (User userEntity : users) {
            userSet.add(this.toDtoLogin(userEntity));
        }

        return userSet;
    }
}
