import { IEmployee } from 'app/entities/employee/employee.model'; // Импортируем интерфейс IEmployee

export interface ITeam {
  id: number;
  teamName?: string | null;
  employees?: IEmployee[] | null; // Добавляем свойство employees, тип - массив IEmployee
}

export type NewTeam = Omit<ITeam, 'id'> & { id: null };
