import { Component, Input } from '@angular/core';

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

  // Формирование классов для дня
  getDayClasses(): string {
    let classes = 'day';
    if (this.isCurrentDate) classes += ' current';
    if (this.isSelected) classes += ' selected';
    if (this.isOtherMonth) classes += ' other-month';
    if (this.hasEvents) classes += ' has-events';
    return classes;
  }
}
