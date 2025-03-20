import dayjs from 'dayjs/esm';
import { IPosition } from 'app/entities/position/position.model';
import { IProfession } from 'app/entities/profession/profession.model';
import { ITeam } from 'app/entities/team/team.model';

export interface IEmployee {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  birthDate?: dayjs.Dayjs | null;
  employeeNumber?: string | null;
  hireDate?: dayjs.Dayjs | null;
  position?: IPosition | null;
  professions?: IProfession[] | null;
  team?: ITeam | null;
}

export type NewEmployee = Omit<IEmployee, 'id'> & { id: null };
