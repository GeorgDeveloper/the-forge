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
  employeeId?: number; // ID сотрудника (для инструктажей)
  employeeName?: string; // Имя сотрудника (для инструктажей)
  professionId?: number; // ID профессии (для инструктажей)
  professionName?: string; // Название профессии (для инструктажей)
  positionId?: number; // ID должности (для инструктажей)
  positionName?: string; // Название должности (для инструктажей)
  validityPeriod?: number; // Период действия (для инструктажей)
}

// Перечисление типов событий
export enum EventType {
  TASK = 'TASK', // Задача
  INSTRUCTION = 'INSTRUCTION', // Инструктаж
  MEETING = 'MEETING', // Встреча
  TRAINING = 'TRAINING', // Тренировка
  OTHER = 'OTHER', // Другое
}
