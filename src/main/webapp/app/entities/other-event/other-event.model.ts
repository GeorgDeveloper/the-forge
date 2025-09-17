import dayjs from 'dayjs/esm';

export interface IOtherEvent {
  id: number;
  title?: string | null;
  eventDate?: dayjs.Dayjs | null;
  startTime?: string | null;
  endTime?: string | null;
  location?: string | null;
  description?: string | null;
  completed?: boolean | null;
}

export type NewOtherEvent = Omit<IOtherEvent, 'id'> & { id: null };
