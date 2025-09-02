import { Component, Input } from '@angular/core';
import { CalendarEvent, EventType } from './calendar-event.model';

@Component({
  selector: 'jhi-calendar-day',
  standalone: true,
  templateUrl: './calendar-day.component.html',
  styleUrls: ['./calendar-day.component.scss'],
})
export class CalendarDayComponent {
  @Input() date: Date | null = null; // Дата для отображения
  @Input() hasEvents: boolean = false; // Есть ли события
  @Input() isCurrentDate: boolean = false; // Текущая дата
  @Input() isSelected: boolean = false; // Выбранная дата
  @Input() isOtherMonth: boolean = false; // День из другого месяца
  @Input() events: CalendarEvent[] = []; // События на этот день

  // Формирование классов для дня
  getDayClasses(): string {
    let classes = 'day';
    if (this.isCurrentDate) classes += ' current';
    if (this.isSelected) classes += ' selected';
    if (this.isOtherMonth) classes += ' other-month';
    if (this.hasEvents) classes += ' has-events';
    if (this.hasInstructions()) classes += ' has-instructions';
    return classes;
  }

  // Проверка наличия инструктажей на этот день
  hasInstructions(): boolean {
    return this.events.some(event => event.type === EventType.INSTRUCTION);
  }

  // Получение количества инструктажей
  getInstructionCount(): number {
    return this.events.filter(event => event.type === EventType.INSTRUCTION).length;
  }
}
