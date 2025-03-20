import { IProfession, NewProfession } from './profession.model';

export const sampleWithRequiredData: IProfession = {
  id: 6169,
  professionName: 'formation consequently birth',
};

export const sampleWithPartialData: IProfession = {
  id: 26397,
  professionName: 'longingly gadzooks',
};

export const sampleWithFullData: IProfession = {
  id: 11070,
  professionName: 'ugh muddy',
};

export const sampleWithNewData: NewProfession = {
  professionName: 'blah',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
