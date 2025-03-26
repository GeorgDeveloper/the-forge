package ru.georgdeveloper.myapp.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.georgdeveloper.myapp.domain.Team;

/**
 * Интерфейс сервиса для управления {@link ru.georgdeveloper.myapp.domain.Team}.
 */
public interface TeamService {
    /**
     * Сохранить команду.
     *
     * @param team сохраняемая команда.
     * @return сохраненная команда.
     */
    Team save(Team team);

    /**
     * Обновить команду.
     *
     * @param team обновляемая команда.
     * @return обновленная команда.
     */
    Team update(Team team);

    /**
     * Частично обновить команду.
     *
     * @param team команда для частичного обновления.
     * @return обновленная команда.
     */
    Optional<Team> partialUpdate(Team team);

    /**
     * Получить все команды.
     *
     * @param pageable параметры постраничного вывода.
     * @return список команд.
     */
    Page<Team> findAll(Pageable pageable);

    /**
     * Получить команду по идентификатору.
     *
     * @param id идентификатор команды.
     * @return найденная команда.
     */
    Optional<Team> findOne(Long id);

    /**
     * Удалить команду.
     *
     * @param id идентификатор удаляемой команды.
     */
    void delete(Long id);
}
