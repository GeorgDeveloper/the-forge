import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule, FormControl, FormGroup, Validators } from '@angular/forms';
import dayjs from 'dayjs/esm';
import { IOtherEvent, NewOtherEvent } from '../other-event.model';
import { OtherEventService } from '../service/other-event.service';

type OtherEventFormGroupContent = {
  id: FormControl<IOtherEvent['id'] | NewOtherEvent['id']>;
  title: FormControl<IOtherEvent['title']>;
  eventDate: FormControl<IOtherEvent['eventDate']>;
  startTime: FormControl<IOtherEvent['startTime']>;
  endTime: FormControl<IOtherEvent['endTime']>;
  location: FormControl<IOtherEvent['location']>;
  description: FormControl<IOtherEvent['description']>;
  completed: FormControl<IOtherEvent['completed']>;
};

export type OtherEventFormGroup = FormGroup<OtherEventFormGroupContent>;

@Component({
  selector: 'jhi-other-event-update',
  templateUrl: './other-event-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OtherEventUpdateComponent implements OnInit {
  isSaving = false;
  otherEvent: IOtherEvent | null = null;

  protected otherEventService = inject(OtherEventService);
  protected activatedRoute = inject(ActivatedRoute);

  editForm: OtherEventFormGroup = new FormGroup<OtherEventFormGroupContent>({
    id: new FormControl({ value: null, disabled: true }, { nonNullable: true, validators: [Validators.required] }),
    title: new FormControl(null, { validators: [Validators.required] }),
    eventDate: new FormControl(null, { validators: [Validators.required] }),
    startTime: new FormControl(null),
    endTime: new FormControl(null),
    location: new FormControl(null),
    description: new FormControl(null),
    completed: new FormControl(false),
  });

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ otherEvent }) => {
      this.otherEvent = otherEvent;
      if (otherEvent) {
        this.updateForm(otherEvent);
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
    const entity = this.getEntity(this.editForm);
    if (entity.id !== null) {
      this.subscribeToSaveResponse(this.otherEventService.update(entity));
    } else {
      this.subscribeToSaveResponse(this.otherEventService.create(entity as NewOtherEvent));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOtherEvent>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({ next: () => this.onSaveSuccess(), error: () => this.onSaveError() });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {}

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(otherEvent: IOtherEvent): void {
    this.otherEvent = otherEvent;
    this.editForm.reset({
      id: { value: otherEvent.id, disabled: true },
      title: otherEvent.title ?? null,
      eventDate: otherEvent.eventDate ?? null,
      startTime: otherEvent.startTime ?? null,
      endTime: otherEvent.endTime ?? null,
      location: otherEvent.location ?? null,
      description: otherEvent.description ?? null,
      completed: otherEvent.completed ?? false,
    } as any);
  }

  protected getEntity(form: OtherEventFormGroup): IOtherEvent | NewOtherEvent {
    const raw = form.getRawValue();
    return raw as any;
  }
}
