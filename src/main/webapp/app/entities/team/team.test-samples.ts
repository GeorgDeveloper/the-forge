// Импорт интерфейсов модели команды
import { ITeam, NewTeam } from './team.model';

/**
 * Тестовые данные команды с обязательными полями.
 * Соответствует минимальным требованиям интерфейса ITeam.
 * Используется в тестах, где нужен только минимальный валидный объект команды.
 */
export const sampleWithRequiredData: ITeam = {
  id: 31463, // Уникальный идентификатор команды
  teamName: 'mysterious triumphantly', // Название команды (обязательное поле)
};

/**
 * Тестовые данные команды с частично заполненными полями.
 * Может использоваться для тестирования обновлений или частичных данных.
 */
export const sampleWithPartialData: ITeam = {
  id: 9762, // Другой ID
  teamName: 'oddball', // Альтернативное название команды
};

/**
 * Полные тестовые данные команды.
 * Включает все возможные поля модели (в текущей реализации только id и teamName).
 */
export const sampleWithFullData: ITeam = {
  id: 19481,
  teamName: 'upwardly well-lit wide', // Развернутое название команды
};

/**
 * Данные для создания новой команды (NewTeam).
 * Отличается от ITeam тем, что id может быть null (для новых сущностей).
 */
export const sampleWithNewData: NewTeam = {
  teamName: 'unfreeze instructive waver', // Название новой команды
  id: null, // Явное указание null для нового объекта
};

// Замораживание объектов для предотвращения изменений в тестах
Object.freeze(sampleWithNewData); // Защита данных новой команды
Object.freeze(sampleWithRequiredData); // Защита обязательных данных
Object.freeze(sampleWithPartialData); // Защита частичных данных
Object.freeze(sampleWithFullData); // Защита полных данных
