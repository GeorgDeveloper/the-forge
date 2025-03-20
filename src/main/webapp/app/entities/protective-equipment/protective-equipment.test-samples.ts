import { IProtectiveEquipment, NewProtectiveEquipment } from './protective-equipment.model';

export const sampleWithRequiredData: IProtectiveEquipment = {
  id: 28361,
  equipmentName: 'to reconstitute',
  quantity: 7693,
  issuanceFrequency: 7979,
};

export const sampleWithPartialData: IProtectiveEquipment = {
  id: 25425,
  equipmentName: 'knavishly',
  quantity: 7017,
  issuanceFrequency: 26861,
};

export const sampleWithFullData: IProtectiveEquipment = {
  id: 9960,
  equipmentName: 'doorpost',
  quantity: 28599,
  issuanceFrequency: 17048,
};

export const sampleWithNewData: NewProtectiveEquipment = {
  equipmentName: 'brr',
  quantity: 23179,
  issuanceFrequency: 14071,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
