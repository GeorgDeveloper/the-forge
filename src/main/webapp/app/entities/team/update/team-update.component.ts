import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITeam } from '../team.model';
import { TeamService } from '../service/team.service';
import { TeamFormGroup, TeamFormService } from './team-form.service';
import { IEmployee } from 'app/entities/employee/employee.model'; // Import IEmployee
import { EmployeeService } from 'app/entities/employee/service/employee.service'; // Import EmployeeService

@Component({
  selector: 'jhi-team-update',
  templateUrl: './team-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TeamUpdateComponent implements OnInit {
  isSaving = false;
  team: ITeam | null = null;
  employees: IEmployee[] = []; // List of all available employees

  protected teamService = inject(TeamService);
  protected teamFormService = inject(TeamFormService);
  protected activatedRoute = inject(ActivatedRoute);
  protected employeeService = inject(EmployeeService); // Inject EmployeeService

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TeamFormGroup = this.teamFormService.createTeamFormGroup();

  ngOnInit(): void {
    this.loadAllEmployees(); // Load all employees

    this.activatedRoute.data.subscribe(({ team }) => {
      this.team = team;
      if (team) {
        this.updateForm(team);
      }
    });
  }

  loadAllEmployees(): void {
    this.employeeService.query().subscribe({
      next: (res: HttpResponse<IEmployee[]>) => {
        this.employees = res.body ?? [];
      },
      error: () => console.error('Error loading employees'),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const team = this.teamFormService.getTeam(this.editForm);
    if (team.id !== null) {
      this.subscribeToSaveResponse(this.teamService.update(team));
    } else {
      this.subscribeToSaveResponse(this.teamService.create(team));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITeam>>): void {
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

  protected updateForm(team: ITeam): void {
    this.team = team;
    this.teamFormService.resetForm(this.editForm, team);
  }
}
// import { Component, OnInit, inject } from '@angular/core';
// import { HttpResponse } from '@angular/common/http';
// import { ActivatedRoute } from '@angular/router';
// import { Observable } from 'rxjs';
// import { finalize } from 'rxjs/operators';
//
// import SharedModule from 'app/shared/shared.module';
// import { FormsModule, ReactiveFormsModule } from '@angular/forms';
//
// import { ITeam } from '../team.model';
// import { TeamService } from '../service/team.service';
// import { TeamFormGroup, TeamFormService } from './team-form.service';
//
// @Component({
//   selector: 'jhi-team-update',
//   templateUrl: './team-update.component.html',
//   imports: [SharedModule, FormsModule, ReactiveFormsModule],
// })
// export class TeamUpdateComponent implements OnInit {
//   isSaving = false;
//   team: ITeam | null = null;
//
//   protected teamService = inject(TeamService);
//   protected teamFormService = inject(TeamFormService);
//   protected activatedRoute = inject(ActivatedRoute);
//
//   // eslint-disable-next-line @typescript-eslint/member-ordering
//   editForm: TeamFormGroup = this.teamFormService.createTeamFormGroup();
//
//   ngOnInit(): void {
//     this.activatedRoute.data.subscribe(({ team }) => {
//       this.team = team;
//       if (team) {
//         this.updateForm(team);
//       }
//     });
//   }
//
//   previousState(): void {
//     window.history.back();
//   }
//
//   save(): void {
//     this.isSaving = true;
//     const team = this.teamFormService.getTeam(this.editForm);
//     if (team.id !== null) {
//       this.subscribeToSaveResponse(this.teamService.update(team));
//     } else {
//       this.subscribeToSaveResponse(this.teamService.create(team));
//     }
//   }
//
//   protected subscribeToSaveResponse(result: Observable<HttpResponse<ITeam>>): void {
//     result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
//       next: () => this.onSaveSuccess(),
//       error: () => this.onSaveError(),
//     });
//   }
//
//   protected onSaveSuccess(): void {
//     this.previousState();
//   }
//
//   protected onSaveError(): void {
//     // Api for inheritance.
//   }
//
//   protected onSaveFinalize(): void {
//     this.isSaving = false;
//   }
//
//   protected updateForm(team: ITeam): void {
//     this.team = team;
//     this.teamFormService.resetForm(this.editForm, team);
//   }
// }
