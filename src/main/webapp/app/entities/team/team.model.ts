import { IEmployee } from 'app/entities/employee/employee.model';

// Интерфейс ITeam определяет структуру данных для объекта "Команда"
export interface ITeam {
  id: number; // Уникальный идентификатор команды
  teamName?: string | null; // Название команды (может быть null или отсутствовать)
  employees?: IEmployee[] | null; // Массив сотрудников, входящих в команду (может быть null или отсутствовать)
}

// Тип NewTeam определяет структуру данных для создания новой команды, включая поле 'id'.
// Он исключает поле 'id' из интерфейса ITeam и добавляет поле 'id' со значением null.
// Это полезно при создании новых объектов, когда ID еще не присвоен.
export type NewTeam = Omit<ITeam, 'id'> & { id: null };
