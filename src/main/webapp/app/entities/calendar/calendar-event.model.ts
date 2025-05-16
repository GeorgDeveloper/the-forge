// calendar-event.model.ts
export interface CalendarEvent {
  id?: number;
  title: string;
  description: string;
  date: string; // Дата в формате ISO (YYYY-MM-DD)
  startTime?: string; // Время начала (HH:mm)
  endTime?: string; // Время окончания (HH:mm)
  type: EventType;
  location?: string;
  participants?: string[];
  reminder?: boolean;
}

export enum EventType {
  TASK = 'TASK',
  INSTRUCTION = 'INSTRUCTION',
  MEETING = 'MEETING',
  TRAINING = 'TRAINING',
  OTHER = 'OTHER',
}
