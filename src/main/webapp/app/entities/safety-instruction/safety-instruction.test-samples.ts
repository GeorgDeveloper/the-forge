import dayjs from 'dayjs/esm';

import { ISafetyInstruction, NewSafetyInstruction } from './safety-instruction.model';

export const sampleWithRequiredData: ISafetyInstruction = {
  id: 28860,
  instructionName: 'rotating',
  introductionDate: dayjs('2025-03-19'),
};

export const sampleWithPartialData: ISafetyInstruction = {
  id: 7348,
  instructionName: 'across curl',
  introductionDate: dayjs('2025-03-19'),
};

export const sampleWithFullData: ISafetyInstruction = {
  id: 24562,
  instructionName: 'save past impressionable',
  introductionDate: dayjs('2025-03-19'),
};

export const sampleWithNewData: NewSafetyInstruction = {
  instructionName: 'bench minus',
  introductionDate: dayjs('2025-03-20'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
