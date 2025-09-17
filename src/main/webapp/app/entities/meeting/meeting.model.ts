import dayjs from 'dayjs/esm';

export interface IMeeting {
  id: number;
  title?: string | null;
  eventDate?: dayjs.Dayjs | null;
  startTime?: string | null;
  endTime?: string | null;
  location?: string | null;
  description?: string | null;
}

export type NewMeeting = Omit<IMeeting, 'id'> & { id: null };
