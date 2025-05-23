import { Component, OnInit, signal } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { combineLatest, Observable, of } from 'rxjs';

import { CalendarEvent, EventType } from './calendar-event.model';
import { CalendarService } from './calendar.service';
import { CalendarDayComponent } from './calendar-day.component';
import { CalendarEventListComponent } from './calendar-event-list.component';
import { CalendarEventModalComponent } from './calendar-event-modal.component';

import { catchError } from 'rxjs/operators';

@Component({
  selector: 'jhi-calendar',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, CalendarDayComponent, CalendarEventListComponent, DatePipe],
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss'],
})
export class CalendarComponent implements OnInit {
  currentDate = signal<Date>(new Date()); // Текущий месяц
  selectedDate = signal<Date>(new Date()); // Выбранная дата
  events = signal<CalendarEvent[]>([]); // Все события
  isLoading = signal<boolean>(false); // Флаг загрузки

  constructor(
    private calendarService: CalendarService,
    private modalService: NgbModal,
  ) {}

  ngOnInit(): void {
    this.loadAllEvents();
  }

  // Загрузка всех событий (календарных + задач)
  loadAllEvents(): void {
    this.isLoading.set(true);

    combineLatest([
      this.calendarService.getEvents().pipe(catchError(() => of([] as CalendarEvent[]))),
      this.calendarService.getTasksAsEvents().pipe(catchError(() => of([] as CalendarEvent[]))),
    ]).subscribe({
      next: ([events, taskEvents]) => {
        // Убеждаемся, что оба значения - массивы
        const safeEvents = Array.isArray(events) ? events : [];
        const safeTaskEvents = Array.isArray(taskEvents) ? taskEvents : [];

        this.events.set([...safeEvents, ...safeTaskEvents]);
        this.isLoading.set(false);
      },
      error: err => {
        console.error('Error loading data:', err);
        this.events.set([]);
        this.isLoading.set(false);
      },
    });
  }

  // Выбор даты
  selectDate(date: Date): void {
    this.selectedDate.set(date);
  }

  // Переход к предыдущему месяцу
  previousMonth(): void {
    const newDate = new Date(this.currentDate());
    newDate.setMonth(newDate.getMonth() - 1);
    this.currentDate.set(newDate);
    this.loadAllEvents();
  }

  // Переход к следующему месяцу
  nextMonth(): void {
    const newDate = new Date(this.currentDate());
    newDate.setMonth(newDate.getMonth() + 1);
    this.currentDate.set(newDate);
    this.loadAllEvents();
  }

  // Проверка, является ли дата сегодняшней
  isToday(date: Date): boolean {
    const today = new Date();
    return date.getDate() === today.getDate() && date.getMonth() === today.getMonth() && date.getFullYear() === today.getFullYear();
  }

  // Проверка, является ли дата выбранной
  isSelectedDate(date: Date): boolean {
    const selected = this.selectedDate();
    return (
      date.getDate() === selected.getDate() && date.getMonth() === selected.getMonth() && date.getFullYear() === selected.getFullYear()
    );
  }

  // Получение дней для отображения в месяце
  getDaysInMonth(): { date: Date; isOtherMonth: boolean }[] {
    const date = this.currentDate();
    const year = date.getFullYear();
    const month = date.getMonth();

    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);

    const startDay = new Date(firstDay);
    startDay.setDate(firstDay.getDate() - (firstDay.getDay() || 7) + 1);

    const endDay = new Date(lastDay);
    endDay.setDate(lastDay.getDate() + (7 - (lastDay.getDay() || 7)));

    const days = [];
    const currentDay = new Date(startDay);

    while (currentDay <= endDay) {
      days.push({
        date: new Date(currentDay),
        isOtherMonth: currentDay.getMonth() !== month,
      });
      currentDay.setDate(currentDay.getDate() + 1);
    }

    return days;
  }

  // Проверка наличия событий на дату
  hasEventsOnDate(date: Date): boolean {
    return this.getEventsForDate(date).length > 0;
  }

  // Получение событий для конкретной даты
  getEventsForDate(date: Date): CalendarEvent[] {
    return this.events().filter(event => new Date(event.date).toDateString() === date.toDateString());
  }

  // Открытие модального окна для добавления события
  openAddEventModal(): void {
    const modalRef = this.modalService.open(CalendarEventModalComponent, {
      size: 'lg',
      backdrop: 'static',
    });

    modalRef.componentInstance.date = this.selectedDate();

    modalRef.closed.subscribe((result: CalendarEvent) => {
      if (result) {
      }
    });
  }
}
