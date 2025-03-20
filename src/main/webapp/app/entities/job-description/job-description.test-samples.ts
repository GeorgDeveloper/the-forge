import dayjs from 'dayjs/esm';

import { IJobDescription, NewJobDescription } from './job-description.model';

export const sampleWithRequiredData: IJobDescription = {
  id: 21333,
  descriptionName: 'helpfully clearly',
  approvalDate: dayjs('2025-03-19'),
};

export const sampleWithPartialData: IJobDescription = {
  id: 11748,
  descriptionName: 'eek along',
  approvalDate: dayjs('2025-03-19'),
};

export const sampleWithFullData: IJobDescription = {
  id: 19606,
  descriptionName: 'before solace positively',
  approvalDate: dayjs('2025-03-19'),
};

export const sampleWithNewData: NewJobDescription = {
  descriptionName: 'amongst',
  approvalDate: dayjs('2025-03-19'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
