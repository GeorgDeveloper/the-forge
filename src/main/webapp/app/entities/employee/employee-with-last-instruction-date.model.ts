import dayjs from 'dayjs/esm';
import { IPosition } from 'app/entities/position/position.model';
import { ITeam } from 'app/entities/team/team.model';

export interface IEmployeeWithLastInstructionDate {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  birthDate?: dayjs.Dayjs | null;
  employeeNumber?: string | null;
  hireDate?: dayjs.Dayjs | null;
  lastInstructionDate?: dayjs.Dayjs | null;
  position?: IPosition | null;
  team?: ITeam | null;
}

export type NewEmployeeWithLastInstructionDate = Omit<IEmployeeWithLastInstructionDate, 'id'> & { id: null };
