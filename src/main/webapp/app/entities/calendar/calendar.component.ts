import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';

import { CalendarEvent, EventType } from './calendar-event.model';
import { CalendarService } from './calendar.service';
import { CalendarEventModalComponent } from './calendar-event-modal.component';
import { CalendarDayComponent } from './calendar-day.component';
import { CalendarEventListComponent } from './calendar-event-list.component';

@Component({
  selector: 'jhi-calendar',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, CalendarDayComponent, CalendarEventListComponent],
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss'],
})
export class CalendarComponent implements OnInit {
  // Текущая дата
  currentDate = signal<Date>(new Date());

  // Выбранная дата
  selectedDate = signal<Date>(new Date());

  // События календаря
  events = signal<CalendarEvent[]>([]);

  // Загрузка данных
  isLoading = signal<boolean>(false);

  constructor(
    private calendarService: CalendarService,
    private modalService: NgbModal,
  ) {}

  ngOnInit(): void {
    this.loadEvents();
  }

  /**
   * Загружает события из API
   */
  loadEvents(): void {
    this.isLoading.set(true);
    this.calendarService.getEvents().subscribe({
      next: events => {
        this.events.set(events);
        this.isLoading.set(false);
      },
      error: err => {
        console.error('Error loading events:', err);
        this.isLoading.set(false);
      },
    });
  }

  /**
   * Изменяет выбранную дату
   * @param date Новая выбранная дата
   */
  selectDate(date: Date): void {
    this.selectedDate.set(date);
  }

  /**
   * Переход к предыдущему месяцу
   */
  previousMonth(): void {
    const newDate = new Date(this.currentDate());
    newDate.setMonth(newDate.getMonth() - 1);
    this.currentDate.set(newDate);
  }

  /**
   * Переход к следующему месяцу
   */
  nextMonth(): void {
    const newDate = new Date(this.currentDate());
    newDate.setMonth(newDate.getMonth() + 1);
    this.currentDate.set(newDate);
  }

  /**
   * Открывает модальное окно для создания нового события
   */
  openAddEventModal(): void {
    const modalRef = this.modalService.open(CalendarEventModalComponent, {
      size: 'lg',
      backdrop: 'static',
    });

    modalRef.componentInstance.date = this.selectedDate();

    modalRef.closed.subscribe((result: CalendarEvent) => {
      if (result) {
        this.addEvent(result);
      }
    });
  }

  /**
   * Добавляет новое событие
   * @param event Новое событие
   */
  addEvent(event: CalendarEvent): void {
    this.calendarService.addEvent(event).subscribe({
      next: newEvent => {
        this.events.update(events => [...events, newEvent]);
      },
      error: err => {
        console.error('Error adding event:', err);
      },
    });
  }

  /**
   * Получает события для указанной даты
   * @param date Дата для фильтрации событий
   * @returns Массив событий на указанную дату
   */
  getEventsForDate(date: Date): CalendarEvent[] {
    return this.events().filter(event => new Date(event.date).toDateString() === date.toDateString());
  }

  /**
   * Проверяет, есть ли события на указанную дату
   * @param date Дата для проверки
   * @returns true, если есть события на эту дату
   */
  hasEventsOnDate(date: Date): boolean {
    return this.getEventsForDate(date).length > 0;
  }

  /**
   * Проверяет, является ли дата сегодняшней
   */
  isToday(date: Date): boolean {
    const today = new Date();
    return date.getDate() === today.getDate() && date.getMonth() === today.getMonth() && date.getFullYear() === today.getFullYear();
  }

  /**
   * Проверяет, является ли дата выбранной
   */
  isSelectedDate(date: Date): boolean {
    const selected = this.selectedDate();
    return (
      date.getDate() === selected.getDate() && date.getMonth() === selected.getMonth() && date.getFullYear() === selected.getFullYear()
    );
  }

  /**
   * Возвращает массив дней для отображения в текущем месяце
   */
  getDaysInMonth(): { date: Date; isOtherMonth: boolean }[] {
    const date = this.currentDate();
    const year = date.getFullYear();
    const month = date.getMonth();

    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);

    // Начинаем с понедельника перед первым числом месяца
    const startDay = new Date(firstDay);
    startDay.setDate(firstDay.getDate() - (firstDay.getDay() || 7) + 1);

    // Заканчиваем в воскресенье после последнего числа месяца
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
}
