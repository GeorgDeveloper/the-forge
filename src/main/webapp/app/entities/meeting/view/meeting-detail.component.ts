import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import SharedModule from 'app/shared/shared.module';
import { IMeeting } from '../meeting.model';
import { FormatMediumDatePipe } from 'app/shared/date';
import { MeetingService } from '../service/meeting.service';

@Component({
  selector: 'jhi-meeting-detail',
  templateUrl: './meeting-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class MeetingDetailComponent implements OnInit {
  meeting = signal<IMeeting | null>(null);

  private readonly route = inject(ActivatedRoute);
  private readonly meetingService = inject(MeetingService);

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.meetingService.find(id).subscribe(res => this.meeting.set(res.body ?? null));
    }
  }

  previousState(): void {
    window.history.back();
  }
}
