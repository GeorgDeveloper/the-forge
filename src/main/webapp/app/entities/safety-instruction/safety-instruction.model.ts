import dayjs from 'dayjs/esm';
import { IProfession } from 'app/entities/profession/profession.model';
import { IPosition } from 'app/entities/position/position.model';

export interface ISafetyInstruction {
  id: number;
  instructionName?: string | null;
  introductionDate?: dayjs.Dayjs | null;
  profession?: IProfession | null;
  position?: IPosition | null;
}

export type NewSafetyInstruction = Omit<ISafetyInstruction, 'id'> & { id: null };
