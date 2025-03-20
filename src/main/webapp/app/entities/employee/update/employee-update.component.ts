import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPosition } from 'app/entities/position/position.model';
import { PositionService } from 'app/entities/position/service/position.service';
import { IProfession } from 'app/entities/profession/profession.model';
import { ProfessionService } from 'app/entities/profession/service/profession.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { EmployeeService } from '../service/employee.service';
import { IEmployee } from '../employee.model';
import { EmployeeFormGroup, EmployeeFormService } from './employee-form.service';

@Component({
  selector: 'jhi-employee-update',
  templateUrl: './employee-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EmployeeUpdateComponent implements OnInit {
  isSaving = false;
  employee: IEmployee | null = null;

  positionsSharedCollection: IPosition[] = [];
  professionsSharedCollection: IProfession[] = [];
  teamsSharedCollection: ITeam[] = [];

  protected employeeService = inject(EmployeeService);
  protected employeeFormService = inject(EmployeeFormService);
  protected positionService = inject(PositionService);
  protected professionService = inject(ProfessionService);
  protected teamService = inject(TeamService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EmployeeFormGroup = this.employeeFormService.createEmployeeFormGroup();

  comparePosition = (o1: IPosition | null, o2: IPosition | null): boolean => this.positionService.comparePosition(o1, o2);

  compareProfession = (o1: IProfession | null, o2: IProfession | null): boolean => this.professionService.compareProfession(o1, o2);

  compareTeam = (o1: ITeam | null, o2: ITeam | null): boolean => this.teamService.compareTeam(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employee }) => {
      this.employee = employee;
      if (employee) {
        this.updateForm(employee);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employee = this.employeeFormService.getEmployee(this.editForm);
    if (employee.id !== null) {
      this.subscribeToSaveResponse(this.employeeService.update(employee));
    } else {
      this.subscribeToSaveResponse(this.employeeService.create(employee));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployee>>): void {
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

  protected updateForm(employee: IEmployee): void {
    this.employee = employee;
    this.employeeFormService.resetForm(this.editForm, employee);

    this.positionsSharedCollection = this.positionService.addPositionToCollectionIfMissing<IPosition>(
      this.positionsSharedCollection,
      employee.position,
    );
    this.professionsSharedCollection = this.professionService.addProfessionToCollectionIfMissing<IProfession>(
      this.professionsSharedCollection,
      ...(employee.professions ?? []),
    );
    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing<ITeam>(this.teamsSharedCollection, employee.team);
  }

  protected loadRelationshipsOptions(): void {
    this.positionService
      .query()
      .pipe(map((res: HttpResponse<IPosition[]>) => res.body ?? []))
      .pipe(
        map((positions: IPosition[]) =>
          this.positionService.addPositionToCollectionIfMissing<IPosition>(positions, this.employee?.position),
        ),
      )
      .subscribe((positions: IPosition[]) => (this.positionsSharedCollection = positions));

    this.professionService
      .query()
      .pipe(map((res: HttpResponse<IProfession[]>) => res.body ?? []))
      .pipe(
        map((professions: IProfession[]) =>
          this.professionService.addProfessionToCollectionIfMissing<IProfession>(professions, ...(this.employee?.professions ?? [])),
        ),
      )
      .subscribe((professions: IProfession[]) => (this.professionsSharedCollection = professions));

    this.teamService
      .query()
      .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
      .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing<ITeam>(teams, this.employee?.team)))
      .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));
  }
}
