import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProfession } from 'app/entities/profession/profession.model';
import { ProfessionService } from 'app/entities/profession/service/profession.service';
import { IAdditionalTraining } from '../additional-training.model';
import { AdditionalTrainingService } from '../service/additional-training.service';
import { AdditionalTrainingFormGroup, AdditionalTrainingFormService } from './additional-training-form.service';

@Component({
  selector: 'jhi-additional-training-update',
  templateUrl: './additional-training-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AdditionalTrainingUpdateComponent implements OnInit {
  isSaving = false;
  additionalTraining: IAdditionalTraining | null = null;

  professionsSharedCollection: IProfession[] = [];

  protected additionalTrainingService = inject(AdditionalTrainingService);
  protected additionalTrainingFormService = inject(AdditionalTrainingFormService);
  protected professionService = inject(ProfessionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AdditionalTrainingFormGroup = this.additionalTrainingFormService.createAdditionalTrainingFormGroup();

  compareProfession = (o1: IProfession | null, o2: IProfession | null): boolean => this.professionService.compareProfession(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ additionalTraining }) => {
      this.additionalTraining = additionalTraining;
      if (additionalTraining) {
        this.updateForm(additionalTraining);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const additionalTraining = this.additionalTrainingFormService.getAdditionalTraining(this.editForm);
    if (additionalTraining.id !== null) {
      this.subscribeToSaveResponse(this.additionalTrainingService.update(additionalTraining));
    } else {
      this.subscribeToSaveResponse(this.additionalTrainingService.create(additionalTraining));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAdditionalTraining>>): void {
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

  protected updateForm(additionalTraining: IAdditionalTraining): void {
    this.additionalTraining = additionalTraining;
    this.additionalTrainingFormService.resetForm(this.editForm, additionalTraining);

    this.professionsSharedCollection = this.professionService.addProfessionToCollectionIfMissing<IProfession>(
      this.professionsSharedCollection,
      additionalTraining.profession,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.professionService
      .query()
      .pipe(map((res: HttpResponse<IProfession[]>) => res.body ?? []))
      .pipe(
        map((professions: IProfession[]) =>
          this.professionService.addProfessionToCollectionIfMissing<IProfession>(professions, this.additionalTraining?.profession),
        ),
      )
      .subscribe((professions: IProfession[]) => (this.professionsSharedCollection = professions));
  }
}
