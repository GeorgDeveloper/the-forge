import dayjs from 'dayjs/esm';

export interface IJobDescription {
  id: number;
  descriptionName?: string | null;
  approvalDate?: dayjs.Dayjs | null;
}

export type NewJobDescription = Omit<IJobDescription, 'id'> & { id: null };
