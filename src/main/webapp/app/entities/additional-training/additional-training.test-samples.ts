import dayjs from 'dayjs/esm';

import { IAdditionalTraining, NewAdditionalTraining } from './additional-training.model';

export const sampleWithRequiredData: IAdditionalTraining = {
  id: 24157,
  trainingName: 'idle er list',
  trainingDate: dayjs('2025-03-19'),
  validityPeriod: 18675,
};

export const sampleWithPartialData: IAdditionalTraining = {
  id: 19750,
  trainingName: 'aha',
  trainingDate: dayjs('2025-03-20'),
  validityPeriod: 30163,
  nextTrainingDate: dayjs('2025-03-19'),
};

export const sampleWithFullData: IAdditionalTraining = {
  id: 17937,
  trainingName: 'unabashedly',
  trainingDate: dayjs('2025-03-20'),
  validityPeriod: 8263,
  nextTrainingDate: dayjs('2025-03-20'),
};

export const sampleWithNewData: NewAdditionalTraining = {
  trainingName: 'indeed trash unfurl',
  trainingDate: dayjs('2025-03-20'),
  validityPeriod: 28556,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
