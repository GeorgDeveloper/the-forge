import dayjs from 'dayjs/esm';

import { ITraining, NewTraining } from './training.model';

export const sampleWithRequiredData: ITraining = {
  id: 29352,
  trainingName: 'energetic like incidentally',
  lastTrainingDate: dayjs('2025-03-19'),
  validityPeriod: 27834,
};

export const sampleWithPartialData: ITraining = {
  id: 17760,
  trainingName: 'whereas baa indeed',
  lastTrainingDate: dayjs('2025-03-19'),
  validityPeriod: 12066,
};

export const sampleWithFullData: ITraining = {
  id: 30290,
  trainingName: 'misjudge',
  lastTrainingDate: dayjs('2025-03-20'),
  validityPeriod: 19686,
  nextTrainingDate: dayjs('2025-03-19'),
};

export const sampleWithNewData: NewTraining = {
  trainingName: 'though',
  lastTrainingDate: dayjs('2025-03-20'),
  validityPeriod: 6304,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
