import { IJobDescription } from 'app/entities/job-description/job-description.model';

export interface IPosition {
  id: number;
  positionName?: string | null;
  jobDescription?: IJobDescription | null;
}

export type NewPosition = Omit<IPosition, 'id'> & { id: null };
