// calendar-event-list.component.ts
import { Component, Input, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CalendarEvent } from './calendar-event.model';
import { CalendarEventModalComponent } from './calendar-event-modal.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-calendar-event-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './calendar-event-list.component.html',
  styleUrls: ['./calendar-event-list.component.scss'],
})
export class CalendarEventListComponent {
  // Дата, для которой показываются события
  @Input() date: Date | null = null;

  // Список событий
  @Input() events: CalendarEvent[] = [];

  constructor(private modalService: NgbModal) {}

  /**
   * Открывает модальное окно для просмотра/редактирования события
   * @param event Событие
   */
  openEventModal(event: CalendarEvent): void {
    const modalRef = this.modalService.open(CalendarEventModalComponent, {
      size: 'lg',
      backdrop: 'static',
    });

    modalRef.componentInstance.event = event;
    modalRef.componentInstance.isEditMode = true;
  }

  /**
   * Получает класс для типа события
   * @param type Тип события
   */
  getEventTypeClass(type: string): string {
    return `event-type-${type.toLowerCase()}`;
  }
}
