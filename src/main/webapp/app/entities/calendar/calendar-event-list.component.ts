import { Component, Input } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { CalendarEvent, EventType } from './calendar-event.model';
import { CalendarEventModalComponent } from './calendar-event-modal.component';

@Component({
  selector: 'jhi-calendar-event-list',
  standalone: true,
  templateUrl: './calendar-event-list.component.html',
  styleUrls: ['./calendar-event-list.component.scss'],
})
export class CalendarEventListComponent {
  @Input() date: Date | null = null;
  @Input() events: CalendarEvent[] = [];

  constructor(
    private modalService: NgbModal,
    private router: Router,
  ) {}

  openEventModal(event: CalendarEvent): void {
    if (event.type === EventType.TASK) {
      this.router.navigate(['/task', event.id, 'view']);
    } else {
      const modalRef = this.modalService.open(CalendarEventModalComponent, {
        size: 'lg',
        backdrop: 'static',
      });
      modalRef.componentInstance.event = event;
      modalRef.componentInstance.isEditMode = true;
    }
  }

  getEventTypeClass(type: string): string {
    return `event-type-${type.toLowerCase()}`;
  }
}
