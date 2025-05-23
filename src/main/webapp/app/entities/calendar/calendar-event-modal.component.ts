import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CalendarEvent, EventType } from './calendar-event.model';

@Component({
  selector: 'jhi-calendar-event-modal',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './calendar-event-modal.component.html',
  styleUrls: ['./calendar-event-modal.component.scss'],
})
export class CalendarEventModalComponent {
  @Input() isEditMode: boolean = false; // Режим редактирования
  @Input() date?: Date; // Дата для нового события
  @Input() event?: CalendarEvent; // Событие для редактирования

  eventTypes = Object.values(EventType); // Доступные типы событий
  newEvent: CalendarEvent; // Новое/редактируемое событие

  constructor(public activeModal: NgbActiveModal) {
    this.newEvent = this.event || {
      title: '',
      description: '',
      date: this.date?.toISOString().split('T')[0] || new Date().toISOString().split('T')[0],
      type: EventType.TASK,
    };
  }

  // Сохранение события
  save(): void {
    this.activeModal.close(this.newEvent);
  }

  // Отмена
  cancel(): void {
    this.activeModal.dismiss('cancel');
  }
}
