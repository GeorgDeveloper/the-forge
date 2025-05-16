// calendar-day.component.ts
import { Component, Input, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CalendarEvent } from './calendar-event.model';

@Component({
  selector: 'jhi-calendar-day',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './calendar-day.component.html',
  styleUrls: ['./calendar-day.component.scss'],
})
export class CalendarDayComponent {
  // Дата для отображения
  @Input() date: Date | null = null;

  // Флаг, что на этот день есть события
  @Input() hasEvents: boolean = false;

  // Флаг, что это текущая дата
  @Input() isCurrentDate: boolean = false;

  // Флаг, что это выбранная дата
  @Input() isSelected: boolean = false;

  // Флаг, что это день из другого месяца
  @Input() isOtherMonth: boolean = false;

  /**
   * Получает классы для дня календаря
   */
  getDayClasses(): string {
    let classes = 'day';
    if (this.isCurrentDate) classes += ' current';
    if (this.isSelected) classes += ' selected';
    if (this.isOtherMonth) classes += ' other-month';
    if (this.hasEvents) classes += ' has-events';
    return classes;
  }
}
