// Модель данных для события календаря
export interface CalendarEvent {
  id?: number;
  title: string; // Название события
  description: string; // Описание
  date: string; // Дата в формате YYYY-MM-DD
  startTime?: string; // Время начала (HH:mm)
  endTime?: string; // Время окончания (HH:mm)
  type: EventType; // Тип события
  location?: string; // Место проведения
  participants?: string[]; // Список участников
  reminder?: boolean; // Напоминание
  priority?: string; // Приоритет (для задач)
  status?: string; // Статус (для задач)
}

// Перечисление типов событий
export enum EventType {
  TASK = 'TASK', // Задача
  INSTRUCTION = 'INSTRUCTION', // Инструктаж
  MEETING = 'MEETING', // Встреча
  TRAINING = 'TRAINING', // Тренировка
  OTHER = 'OTHER', // Другое
}
