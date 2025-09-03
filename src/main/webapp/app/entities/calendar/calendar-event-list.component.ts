import { Component, Input } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { CalendarEvent, EventType } from './calendar-event.model';
import { CalendarEventModalComponent } from './calendar-event-modal.component';
import { TranslateModule } from '@ngx-translate/core';

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
}
