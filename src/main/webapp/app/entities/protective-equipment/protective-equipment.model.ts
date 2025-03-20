import { IProfession } from 'app/entities/profession/profession.model';

export interface IProtectiveEquipment {
  id: number;
  equipmentName?: string | null;
  quantity?: number | null;
  issuanceFrequency?: number | null;
  profession?: IProfession | null;
}

export type NewProtectiveEquipment = Omit<IProtectiveEquipment, 'id'> & { id: null };
