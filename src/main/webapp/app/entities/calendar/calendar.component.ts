import { Component, OnInit, signal, inject } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { combineLatest, Observable, of } from 'rxjs';
import { TranslateModule, TranslateService } from '@ngx-translate/core';

import { CalendarEvent, EventType } from './calendar-event.model';
import { CalendarService } from './calendar.service';
import { CalendarDayComponent } from './calendar-day.component';
import { CalendarEventListComponent } from './calendar-event-list.component';
import { CalendarEventModalComponent } from './calendar-event-modal.component';

import { catchError } from 'rxjs/operators';

@Component({
  selector: 'jhi-calendar',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, CalendarDayComponent, CalendarEventListComponent, DatePipe, TranslateModule],
  templateUrl: './calendar.component.html',
  styleUrls: ['./calendar.component.scss'],
})
export class CalendarComponent implements OnInit {
  currentDate = signal<Date>(new Date()); // Текущий месяц
  selectedDate = signal<Date>(new Date()); // Выбранная дата
  events = signal<CalendarEvent[]>([]); // Все события
  isLoading = signal<boolean>(false); // Флаг загрузки

  private translateService = inject(TranslateService);
  private router = inject(Router);

  constructor(
    private calendarService: CalendarService,
    private modalService: NgbModal,
  ) {}

  ngOnInit(): void {
    this.loadAllEvents();
  }

  // Загрузка всех событий (календарных + задач + инструктажи)
  loadAllEvents(): void {
    this.isLoading.set(true);

    combineLatest([
      this.calendarService.getEvents().pipe(catchError(() => of([] as CalendarEvent[]))),
      this.calendarService.getTasksAsEvents().pipe(catchError(() => of([] as CalendarEvent[]))),
      this.calendarService.getTrainingsAsEvents().pipe(catchError(() => of([] as CalendarEvent[]))),
      this.calendarService.getAdditionalTrainingsAsEvents().pipe(catchError(() => of([] as CalendarEvent[]))),
      this.calendarService.getSafetyInstructionsAsEvents().pipe(catchError(() => of([] as CalendarEvent[]))),
    ]).subscribe({
      next: ([events, taskEvents, trainingEvents, additionalTrainingEvents, safetyInstructionEvents]) => {
        // Убеждаемся, что все значения - массивы
        const safeEvents = Array.isArray(events) ? events : [];
        const safeTaskEvents = Array.isArray(taskEvents) ? taskEvents : [];
        const safeTrainingEvents = Array.isArray(trainingEvents) ? trainingEvents : [];
        const safeAdditionalTrainingEvents = Array.isArray(additionalTrainingEvents) ? additionalTrainingEvents : [];
        const safeSafetyInstructionEvents = Array.isArray(safetyInstructionEvents) ? safetyInstructionEvents : [];

        this.events.set([
          ...safeEvents,
          ...safeTaskEvents,
          ...safeTrainingEvents,
          ...safeAdditionalTrainingEvents,
          ...safeSafetyInstructionEvents,
        ]);
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

  openAddEventModal(): void {
    const modalRef = this.modalService.open(CalendarEventModalComponent, {
      backdrop: true,
      backdropClass: 'modal-backdrop',
      keyboard: true,
      centered: true,
      // Prevent accessibility issue by not hiding app root; ng-bootstrap manages focus within modal
      // and does not require manually adding aria-hidden on ancestors
    });
    modalRef.componentInstance.isEditMode = false;
    modalRef.componentInstance.date = this.selectedDate();

    modalRef.result
      .then(newEvent => {
        if (!newEvent) return;
        // Router logic: create through respective entities when type is instruction/task/additional, else as calendar event
        this.createFromCalendar(newEvent as CalendarEvent);
      })
      .catch(() => void 0);
  }

  private createFromCalendar(event: CalendarEvent): void {
    // Instruction → Training entity, Additional training → AdditionalTraining entity, Task → Task entity, else Calendar event
    switch (event.type) {
      case EventType.INSTRUCTION:
        this.redirectToTrainingCreate(event);
        break;
      case EventType.ADDITIONAL_TRAINING:
        this.redirectToAdditionalTrainingCreate(event);
        break;
      case EventType.TASK:
        this.redirectToTaskCreate(event);
        break;
      default:
        this.createCalendarEvent(event);
        break;
    }
  }

  private redirectToTrainingCreate(event: CalendarEvent): void {
    // Navigate with prefill state via router to training/new
    // Title → trainingName, date → lastTrainingDate, validityPeriod if provided, description
    this.router.navigate(['/training/new'], {
      state: {
        prefill: {
          trainingName: event.title,
          lastTrainingDate: event.date,
          validityPeriod: event.validityPeriod ?? null,
          description: event.description ?? null,
        },
      },
    });
  }

  private redirectToAdditionalTrainingCreate(event: CalendarEvent): void {
    this.router.navigate(['/additional-training/new'], {
      state: {
        prefill: {
          trainingName: event.title,
          trainingDate: event.date,
          validityPeriod: event.validityPeriod ?? null,
          description: event.description ?? null,
        },
      },
    });
  }

  private redirectToTaskCreate(event: CalendarEvent): void {
    this.router.navigate(['/task/new'], {
      state: {
        prefill: {
          taskName: event.title,
          plannedCompletionDate: event.date,
          body: event.description ?? null,
          priority: event.priority ?? 'MEDIUM',
          status: 'TODO',
          creationDate: new Date().toISOString().split('T')[0],
        },
      },
    });
  }

  private createCalendarEvent(event: CalendarEvent): void {
    this.isLoading.set(true);
    this.calendarService
      .createEvent({
        title: event.title,
        description: event.description,
        date: event.date,
        startTime: event.startTime,
        endTime: event.endTime,
        type: event.type,
        location: event.location,
        participants: event.participants,
        reminder: event.reminder,
        priority: event.priority,
      })
      .subscribe({
        next: () => this.loadAllEvents(),
        error: () => this.isLoading.set(false),
      });
  }

  // Получение переведенного названия месяца
  getTranslatedMonthYear(): string {
    const date = this.currentDate();
    const monthNames = [
      'january',
      'february',
      'march',
      'april',
      'may',
      'june',
      'july',
      'august',
      'september',
      'october',
      'november',
      'december',
    ];

    const monthKey = monthNames[date.getMonth()];
    const monthName = this.translateService.instant(`calendar.months.${monthKey}`);
    const year = date.getFullYear();

    return `${monthName} ${year}`;
  }

  // Получение переведенной даты
  getTranslatedDate(): string {
    const date = this.selectedDate();
    const monthNames = [
      'january',
      'february',
      'march',
      'april',
      'may',
      'june',
      'july',
      'august',
      'september',
      'october',
      'november',
      'december',
    ];

    const day = date.getDate();
    const monthKey = monthNames[date.getMonth()];
    const monthName = this.translateService.instant(`calendar.months.${monthKey}`);
    const year = date.getFullYear();

    return `${day} ${monthName} ${year}`;
  }
}
