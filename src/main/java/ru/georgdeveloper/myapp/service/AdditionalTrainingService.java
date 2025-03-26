// Указываем пакет, к которому принадлежит данный интерфейс
package ru.georgdeveloper.myapp.service;

// Импортируем необходимые классы
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.georgdeveloper.myapp.domain.AdditionalTraining;

/**
 * Сервисный интерфейс для управления сущностью {@link ru.georgdeveloper.myapp.domain.AdditionalTraining}.
 * Определяет основные CRUD-операции для работы с дополнительным обучением.
 */
public interface AdditionalTrainingService {
    /**
     * Сохраняет дополнительное обучение.
     *
     * @param additionalTraining сущность для сохранения
     * @return сохраненная сущность
     */
    AdditionalTraining save(AdditionalTraining additionalTraining);

    /**
     * Обновляет информацию о дополнительном обучении.
     *
     * @param additionalTraining сущность с обновленными данными
     * @return обновленная сущность
     */
    AdditionalTraining update(AdditionalTraining additionalTraining);

    /**
     * Частично обновляет информацию о дополнительном обучении.
     * Возвращает Optional, так как операция может не привести к обновлению.
     *
     * @param additionalTraining сущность с частично обновленными данными
     * @return Optional с обновленной сущностью, если обновление произошло
     */
    Optional<AdditionalTraining> partialUpdate(AdditionalTraining additionalTraining);

    /**
     * Получает все дополнительные обучения с пагинацией.
     *
     * @param pageable параметры пагинации (номер страницы, размер страницы и т.д.)
     * @return страница с дополнительным обучением
     */
    Page<AdditionalTraining> findAll(Pageable pageable);

    /**
     * Находит дополнительное обучение по идентификатору.
     *
     * @param id идентификатор сущности
     * @return Optional с найденной сущностью, если она существует
     */
    Optional<AdditionalTraining> findOne(Long id);

    /**
     * Удаляет дополнительное обучение по идентификатору.
     *
     * @param id идентификатор сущности для удаления
     */
    void delete(Long id);
}
