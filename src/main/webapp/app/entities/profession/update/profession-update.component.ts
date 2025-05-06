import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IProfession } from '../profession.model';
import { ProfessionService } from '../service/profession.service';
import { ProfessionFormGroup, ProfessionFormService } from './profession-form.service';

@Component({
  selector: 'jhi-profession-update',
  templateUrl: './profession-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProfessionUpdateComponent implements OnInit {
  isSaving = false;
  profession: IProfession | null = null;

  employeesSharedCollection: IEmployee[] = [];

  protected professionService = inject(ProfessionService);
  protected professionFormService = inject(ProfessionFormService);
  protected employeeService = inject(EmployeeService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProfessionFormGroup = this.professionFormService.createProfessionFormGroup();

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ profession }) => {
      this.profession = profession;
      if (profession) {
        this.updateForm(profession);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const profession = this.professionFormService.getProfession(this.editForm);
    console.log('Sending profession:', JSON.stringify(profession));
    if (profession.id !== null) {
      this.subscribeToSaveResponse(this.professionService.update(profession));
    } else {
      this.subscribeToSaveResponse(this.professionService.create(profession));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProfession>>): void {
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

  protected updateForm(profession: IProfession): void {
    this.profession = profession;
    this.professionFormService.resetForm(this.editForm, profession);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      ...(profession.employees ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, ...(this.profession?.employees ?? [])),
        ),
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
