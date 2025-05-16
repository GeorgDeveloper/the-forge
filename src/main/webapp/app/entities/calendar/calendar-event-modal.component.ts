// calendar-event-modal.component.ts
import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { CalendarEvent, EventType } from './calendar-event.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'jhi-calendar-event-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './calendar-event-modal.component.html',
  styleUrls: ['./calendar-event-modal.component.scss'],
})
export class CalendarEventModalComponent {
  // Режим редактирования (true) или создания (false)
  @Input() isEditMode: boolean = false;

  // Дата для нового события
  @Input() date?: Date;

  // Событие для редактирования
  @Input() event?: CalendarEvent;

  // Типы событий для выпадающего списка
  eventTypes = Object.values(EventType);

  // Новое событие
  newEvent: CalendarEvent = {
    title: '',
    description: '',
    date: this.date?.toISOString().split('T')[0] || new Date().toISOString().split('T')[0],
    type: EventType.TASK,
  };

  constructor(public activeModal: NgbActiveModal) {
    if (this.event) {
      this.newEvent = { ...this.event };
    }
  }

  /**
   * Сохраняет событие
   */
  save(): void {
    this.activeModal.close(this.newEvent);
  }

  /**
   * Отменяет редактирование
   */
  cancel(): void {
    this.activeModal.dismiss('cancel');
  }
}
