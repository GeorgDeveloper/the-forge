import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IJobDescription } from '../job-description.model';
import { JobDescriptionService } from '../service/job-description.service';
import { JobDescriptionFormGroup, JobDescriptionFormService } from './job-description-form.service';

@Component({
  selector: 'jhi-job-description-update',
  templateUrl: './job-description-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class JobDescriptionUpdateComponent implements OnInit {
  isSaving = false;
  jobDescription: IJobDescription | null = null;

  protected jobDescriptionService = inject(JobDescriptionService);
  protected jobDescriptionFormService = inject(JobDescriptionFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: JobDescriptionFormGroup = this.jobDescriptionFormService.createJobDescriptionFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jobDescription }) => {
      this.jobDescription = jobDescription;
      if (jobDescription) {
        this.updateForm(jobDescription);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const jobDescription = this.jobDescriptionFormService.getJobDescription(this.editForm);
    if (jobDescription.id !== null) {
      this.subscribeToSaveResponse(this.jobDescriptionService.update(jobDescription));
    } else {
      this.subscribeToSaveResponse(this.jobDescriptionService.create(jobDescription));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJobDescription>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(jobDescription: IJobDescription): void {
    this.jobDescription = jobDescription;
    this.jobDescriptionFormService.resetForm(this.editForm, jobDescription);
  }
}
