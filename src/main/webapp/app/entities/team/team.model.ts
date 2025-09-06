import { IEmployee } from 'app/entities/employee/employee.model';
import { IUser } from '../user/user.model';
import { IOrganization } from '../organization/organization.model';

// Интерфейс ITeam определяет структуру данных для объекта "Команда"
export interface ITeam {
  id: number; // Уникальный идентификатор команды
  teamName?: string | null; // Название команды (может быть null или отсутствовать)
  employees?: IEmployee[] | null; // Массив сотрудников, входящих в команду (может быть null или отсутствовать)
  users?: IUser[] | null; // Массив пользователей, входящих в команду (может быть null или отсутствовать)
  organization?: IOrganization | null; // Организация, к которой относится команда
}

// Тип NewTeam определяет структуру данных для создания новой команды, включая поле 'id'.
// Он исключает поле 'id' из интерфейса ITeam и добавляет поле 'id' со значением null.
// Это полезно при создании новых объектов, когда ID еще не присвоен.
export type NewTeam = Omit<ITeam, 'id'> & { id: null };
