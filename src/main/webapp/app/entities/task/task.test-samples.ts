import dayjs from 'dayjs/esm';

import { ITask, NewTask } from './task.model';

export const sampleWithRequiredData: ITask = {
  id: 9181,
  taskName: 'or consequently',
  creationDate: dayjs('2025-03-20'),
  plannedCompletionDate: dayjs('2025-03-20'),
  status: 'IN_PROGRESS',
  priority: 'HIGH',
  body: 'unlike tusk near',
};

export const sampleWithPartialData: ITask = {
  id: 873,
  taskName: 'cannon dense',
  creationDate: dayjs('2025-03-20'),
  plannedCompletionDate: dayjs('2025-03-20'),
  status: 'DONE',
  priority: 'LOW',
  body: 'croon metabolite',
};

export const sampleWithFullData: ITask = {
  id: 13396,
  taskName: 'where wherever',
  creationDate: dayjs('2025-03-19'),
  plannedCompletionDate: dayjs('2025-03-20'),
  status: 'IN_PROGRESS',
  priority: 'CRITICAL',
  body: 'except',
};

export const sampleWithNewData: NewTask = {
  taskName: 'expostulate',
  creationDate: dayjs('2025-03-19'),
  plannedCompletionDate: dayjs('2025-03-20'),
  status: 'DONE',
  priority: 'MEDIUM',
  body: 'utterly before godparent',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
