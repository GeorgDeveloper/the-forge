import dayjs from 'dayjs/esm';
import { IProfession } from 'app/entities/profession/profession.model';

export interface IAdditionalTraining {
  id: number;
  trainingName?: string | null;
  trainingDate?: dayjs.Dayjs | null;
  validityPeriod?: number | null;
  nextTrainingDate?: dayjs.Dayjs | null;
  profession?: IProfession | null;
}

export type NewAdditionalTraining = Omit<IAdditionalTraining, 'id'> & { id: null };
