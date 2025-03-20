import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';

export interface ITraining {
  id: number;
  trainingName?: string | null;
  lastTrainingDate?: dayjs.Dayjs | null;
  validityPeriod?: number | null;
  nextTrainingDate?: dayjs.Dayjs | null;
  employee?: IEmployee | null;
}

export type NewTraining = Omit<ITraining, 'id'> & { id: null };
