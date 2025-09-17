import { Component, OnInit, inject, signal } from '@angular/core';
import { RouterModule } from '@angular/router';
import SharedModule from 'app/shared/shared.module';
import { IMeeting } from '../meeting.model';
import { MeetingService } from '../service/meeting.service';
import { HttpResponse } from '@angular/common/http';
import { FormatMediumDatePipe } from 'app/shared/date';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { filter } from 'rxjs/operators';
import { MeetingDeleteDialogComponent } from '../delete/meeting-delete-dialog.component';

@Component({
  selector: 'jhi-meeting',
  templateUrl: './meeting.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class MeetingComponent implements OnInit {
  isLoading = signal<boolean>(false);
  meetings = signal<IMeeting[]>([]);

  protected meetingService = inject(MeetingService);
  protected modalService = inject(NgbModal);

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.isLoading.set(true);
    this.meetingService.query().subscribe({
      next: (res: HttpResponse<IMeeting[]>) => {
        this.meetings.set(res.body ?? []);
        this.isLoading.set(false);
      },
      error: () => this.isLoading.set(false),
    });
  }

  delete(meeting: IMeeting): void {
    const modalRef = this.modalService.open(MeetingDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    (modalRef.componentInstance as MeetingDeleteDialogComponent).meeting = meeting;
    modalRef.closed.pipe(filter(reason => reason === ITEM_DELETED_EVENT)).subscribe(() => this.loadAll());
  }
}
