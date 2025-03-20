import { IPosition, NewPosition } from './position.model';

export const sampleWithRequiredData: IPosition = {
  id: 22893,
  positionName: 'yippee',
};

export const sampleWithPartialData: IPosition = {
  id: 8342,
  positionName: 'abacus',
};

export const sampleWithFullData: IPosition = {
  id: 24706,
  positionName: 'silt',
};

export const sampleWithNewData: NewPosition = {
  positionName: 'apud',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
