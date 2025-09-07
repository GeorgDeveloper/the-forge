import dayjs from 'dayjs/esm';
import { IProfession } from 'app/entities/profession/profession.model';
import { IPosition } from 'app/entities/position/position.model';

export interface IProcedureDocument {
  id: number;
  documentName?: string | null;
  introductionDate?: dayjs.Dayjs | null;
  description?: string | null;
  profession?: IProfession | null;
  position?: IPosition | null;
  pdfFileName?: string | null;
  pdfFileContentType?: string | null;
  pdfFile?: string | null;
}

export type NewProcedureDocument = Omit<IProcedureDocument, 'id'> & { id: null };
