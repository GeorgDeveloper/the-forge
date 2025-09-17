import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import dayjs from 'dayjs/esm';
import { IMeeting, NewMeeting } from '../meeting.model';
import { MeetingService } from '../service/meeting.service';

type MeetingFormGroupContent = {
  id: FormControl<IMeeting['id'] | NewMeeting['id']>;
  title: FormControl<IMeeting['title']>;
  eventDate: FormControl<IMeeting['eventDate']>;
  startTime: FormControl<IMeeting['startTime']>;
  endTime: FormControl<IMeeting['endTime']>;
  location: FormControl<IMeeting['location']>;
  description: FormControl<IMeeting['description']>;
};

export type MeetingFormGroup = FormGroup<MeetingFormGroupContent>;

@Component({
  selector: 'jhi-meeting-update',
  templateUrl: './meeting-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MeetingUpdateComponent implements OnInit {
  isSaving = false;
  meeting: IMeeting | null = null;

  protected meetingService = inject(MeetingService);
  protected activatedRoute = inject(ActivatedRoute);

  editForm: MeetingFormGroup = new FormGroup<MeetingFormGroupContent>({
    id: new FormControl({ value: null, disabled: true }, { nonNullable: true, validators: [Validators.required] }),
    title: new FormControl(null, { validators: [Validators.required] }),
    eventDate: new FormControl(null, { validators: [Validators.required] }),
    startTime: new FormControl(null),
    endTime: new FormControl(null),
    location: new FormControl(null),
    description: new FormControl(null),
  });

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ meeting }) => {
      this.meeting = meeting;
      if (meeting) {
        this.updateForm(meeting);
      } else {
        const prefill = (history.state?.prefill ?? {}) as any;
        if (prefill) {
          this.editForm.patchValue({
            title: prefill.title ?? null,
            eventDate: prefill.date ? dayjs(prefill.date) : null,
            startTime: prefill.startTime ?? null,
            endTime: prefill.endTime ?? null,
            location: prefill.location ?? null,
            description: prefill.description ?? null,
          });
        }
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const meeting = this.getMeeting(this.editForm);
    if (meeting.id !== null) {
      this.subscribeToSaveResponse(this.meetingService.update(meeting));
    } else {
      this.subscribeToSaveResponse(this.meetingService.create(meeting as NewMeeting));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMeeting>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({ next: () => this.onSaveSuccess(), error: () => this.onSaveError() });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {}

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(meeting: IMeeting): void {
    this.meeting = meeting;
    this.editForm.reset({
      id: { value: meeting.id, disabled: true },
      title: meeting.title ?? null,
      eventDate: meeting.eventDate ?? null,
      startTime: meeting.startTime ?? null,
      endTime: meeting.endTime ?? null,
      location: meeting.location ?? null,
      description: meeting.description ?? null,
    } as any);
  }

  protected getMeeting(form: MeetingFormGroup): IMeeting | NewMeeting {
    const raw = form.getRawValue();
    return raw as any;
  }
}
