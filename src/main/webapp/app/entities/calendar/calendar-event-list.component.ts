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

    // Navigate to Training detail for instruction events when possible
    if (event.type === EventType.INSTRUCTION) {
      // If event has employeeId, it likely came from Training → navigate to training detail
      if (event.id) {
        // Training routes are '/training/:id/view'
        this.router.navigate(['/training', event.id, 'view']);
        return;
      }
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
      'Task',
      'Tasks',
      'Задача',
      'Задачи',
      'Instruction',
      'Instructions',
      'Инструктаж',
      'Инструктажи',
      'Training',
      'Trainings',
      'Обучение',
      'Event',
      'Events',
      'Событие',
      'События',
    ];

    // Normalize invisible NBSP and bidi marks
    let title = event.title.replace(/[\u00A0\u200E\u200F\uFEFF]/g, ' ').trim();
    const pattern = new RegExp(`^(?:[\u00A0\u200E\u200F\uFEFF]|\s)*(${knownTypePrefixes.join('|')})\s*[:\-–—]?\s*`, 'i');

    // Remove repeated leading prefixes like "Task: Task: Title"
    for (let i = 0; i < 3 && pattern.test(title); i++) {
      title = title.replace(pattern, '').trim();
    }
    // Fallback: if there is still a simple word prefix followed by colon (e.g., unknown prefix), drop it once
    // Matches leading letters (latin/cyrillic) optionally with spaces, then a colon
    const genericPrefix = /^([A-Za-zА-Яа-яЁё]+)\s*[:\-–—]\s*/;
    if (genericPrefix.test(title)) {
      title = title.replace(genericPrefix, '').trim();
    }
    return title;
  }

  // Получение переведенного описания события
  getTranslatedDescription(event: CalendarEvent): string {
    if (event.type === 'INSTRUCTION') {
      if (event.employeeName) {
        return `${this.translateService.instant('calendar.fields.employee')}: ${event.employeeName}`;
      } else if (event.professionName && event.positionName) {
        return `${this.translateService.instant('calendar.fields.profession')}: ${event.professionName}, ${this.translateService.instant('calendar.fields.position')}: ${event.positionName}`;
      }
    }

    // Переводим статичные английские тексты из сервиса
    let translatedDescription = event.description;
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

    // Переводим названия инструктажей
    translatedDescription = translatedDescription.replace(
      'Инструктаж:',
      this.translateService.instant('calendar.eventTypes.INSTRUCTION') + ':',
    );

    return translatedDescription;
  }
}
