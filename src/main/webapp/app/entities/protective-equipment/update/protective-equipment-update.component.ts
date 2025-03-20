import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProfession } from 'app/entities/profession/profession.model';
import { ProfessionService } from 'app/entities/profession/service/profession.service';
import { IProtectiveEquipment } from '../protective-equipment.model';
import { ProtectiveEquipmentService } from '../service/protective-equipment.service';
import { ProtectiveEquipmentFormGroup, ProtectiveEquipmentFormService } from './protective-equipment-form.service';

@Component({
  selector: 'jhi-protective-equipment-update',
  templateUrl: './protective-equipment-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProtectiveEquipmentUpdateComponent implements OnInit {
  isSaving = false;
  protectiveEquipment: IProtectiveEquipment | null = null;

  professionsSharedCollection: IProfession[] = [];

  protected protectiveEquipmentService = inject(ProtectiveEquipmentService);
  protected protectiveEquipmentFormService = inject(ProtectiveEquipmentFormService);
  protected professionService = inject(ProfessionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProtectiveEquipmentFormGroup = this.protectiveEquipmentFormService.createProtectiveEquipmentFormGroup();

  compareProfession = (o1: IProfession | null, o2: IProfession | null): boolean => this.professionService.compareProfession(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ protectiveEquipment }) => {
      this.protectiveEquipment = protectiveEquipment;
      if (protectiveEquipment) {
        this.updateForm(protectiveEquipment);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const protectiveEquipment = this.protectiveEquipmentFormService.getProtectiveEquipment(this.editForm);
    if (protectiveEquipment.id !== null) {
      this.subscribeToSaveResponse(this.protectiveEquipmentService.update(protectiveEquipment));
    } else {
      this.subscribeToSaveResponse(this.protectiveEquipmentService.create(protectiveEquipment));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProtectiveEquipment>>): void {
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

  protected updateForm(protectiveEquipment: IProtectiveEquipment): void {
    this.protectiveEquipment = protectiveEquipment;
    this.protectiveEquipmentFormService.resetForm(this.editForm, protectiveEquipment);

    this.professionsSharedCollection = this.professionService.addProfessionToCollectionIfMissing<IProfession>(
      this.professionsSharedCollection,
      protectiveEquipment.profession,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.professionService
      .query()
      .pipe(map((res: HttpResponse<IProfession[]>) => res.body ?? []))
      .pipe(
        map((professions: IProfession[]) =>
          this.professionService.addProfessionToCollectionIfMissing<IProfession>(professions, this.protectiveEquipment?.profession),
        ),
      )
      .subscribe((professions: IProfession[]) => (this.professionsSharedCollection = professions));
  }
}
