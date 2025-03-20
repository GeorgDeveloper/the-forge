import { ITeam, NewTeam } from './team.model';

export const sampleWithRequiredData: ITeam = {
  id: 31463,
  teamName: 'mysterious triumphantly',
};

export const sampleWithPartialData: ITeam = {
  id: 9762,
  teamName: 'oddball',
};

export const sampleWithFullData: ITeam = {
  id: 19481,
  teamName: 'upwardly well-lit wide',
};

export const sampleWithNewData: NewTeam = {
  teamName: 'unfreeze instructive waver',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
