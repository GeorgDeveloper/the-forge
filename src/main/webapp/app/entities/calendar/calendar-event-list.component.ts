import { Component, Input, inject } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { CalendarEvent, EventType } from './calendar-event.model';
import { CalendarEventModalComponent } from './calendar-event-modal.component';
import { TranslateModule, TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'jhi-calendar-event-list',
  standalone: true,
  templateUrl: './calendar-event-list.component.html',
  styleUrls: ['./calendar-event-list.component.scss'],
  imports: [TranslateModule],
})
export class CalendarEventListComponent {
  @Input() date: Date | null = null;
  @Input() events: CalendarEvent[] = [];

  private translateService = inject(TranslateService);

  constructor(
    private modalService: NgbModal,
    private router: Router,
  ) {}

  openEventModal(event: CalendarEvent): void {
    if (event.type === EventType.TASK) {
      this.router.navigate(['/task', event.id, 'view']);
      return;
    }

    if (event.type === EventType.INSTRUCTION && event.id) {
      this.router.navigate(['/training', event.id, 'view']);
      return;
    }

    if (event.type === EventType.ADDITIONAL_TRAINING && event.id) {
      this.router.navigate(['/additional-training', event.id, 'view']);
      return;
    }

    const modalRef = this.modalService.open(CalendarEventModalComponent, {
      size: 'lg',
      backdrop: 'static',
    });
    modalRef.componentInstance.event = event;
    modalRef.componentInstance.isEditMode = true;
  }

  getEventTypeClass(type: string): string {
    return `event-type-${type.toLowerCase()}`;
  }

  // Получение переведенного статуса
  getTranslatedStatus(status: string): string {
    return this.translateService.instant(`calendar.status.${status}`);
  }

  // Получение переведенного приоритета
  getTranslatedPriority(priority: string): string {
    return this.translateService.instant(`calendar.priority.${priority}`);
  }

  // Получение переведенного типа события
  getTranslatedEventType(type: string): string {
    return this.translateService.instant(`calendar.eventTypes.${type}`);
  }

  // Удаление дублирующего префикса типа из заголовка события
  getStrippedTitle(event: CalendarEvent): string {
    if (!event.title) return '';

    const knownTypePrefixes = [
      // ru
      'Задача',
      'Инструктаж',
      'Инструктажи',
      'Доп. обучение',
      'Обучение',
      'Событие',
      'События',
      // en
      'Task',
      'Tasks',
      'Instruction',
      'Instructions',
      'Additional training',
      'Training',
      'Event',
      'Events',
    ];

    let title = event.title.replace(/[\u00A0\u200E\u200F\uFEFF]/g, ' ').trim();
    const pattern = new RegExp(`^(?:[\u00A0\u200E\u200F\uFEFF]|\s)*(${knownTypePrefixes.join('|')})\s*[:\-–—]?\s*`, 'i');

    for (let i = 0; i < 3 && pattern.test(title); i++) {
      title = title.replace(pattern, '').trim();
    }
    return title;
  }

  // Получение переведенного описания события
  getTranslatedDescription(event: CalendarEvent): string {
    // Для специализированных типов детали выводятся отдельными блоками ниже,
    // чтобы избежать дублирования текста в описании
    if (event.type === 'INSTRUCTION' || event.type === 'ADDITIONAL_TRAINING') {
      return '';
    }

    let translatedDescription = event.description || '';
    translatedDescription = translatedDescription.replace('Employee:', this.translateService.instant('calendar.fields.employee') + ':');
    translatedDescription = translatedDescription.replace('Profession:', this.translateService.instant('calendar.fields.profession') + ':');
    translatedDescription = translatedDescription.replace('Position:', this.translateService.instant('calendar.fields.position') + ':');
    translatedDescription = translatedDescription.replace('Unknown employee', this.translateService.instant('calendar.unknownEmployee'));
    translatedDescription = translatedDescription.replace(
      'Unknown profession',
      this.translateService.instant('calendar.unknownProfession'),
    );
    translatedDescription = translatedDescription.replace('Unknown position', this.translateService.instant('calendar.unknownPosition'));
    translatedDescription = translatedDescription.replace('No title', this.translateService.instant('calendar.noTitle'));
    translatedDescription = translatedDescription.replace('Data loading error', this.translateService.instant('calendar.loadError'));

    return translatedDescription;
  }
}
