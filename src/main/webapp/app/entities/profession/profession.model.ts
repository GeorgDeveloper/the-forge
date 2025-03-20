import { IEmployee } from 'app/entities/employee/employee.model';

export interface IProfession {
  id: number;
  professionName?: string | null;
  employees?: IEmployee[] | null;
}

export type NewProfession = Omit<IProfession, 'id'> & { id: null };
