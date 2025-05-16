import { Component, inject, Input, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IEmployee } from '../employee.model';
import { IPosition } from '../../position/position.model';
import { IProfession } from '../../profession/profession.model';
import { ITeam } from '../../team/team.model';
import { PositionService } from '../../position/service/position.service';
import { TeamService } from '../../team/service/team.service';
import { EmployeeService } from '../service/employee.service';
import { EmployeeProfessionDeleteDialogComponent } from './employee-profession-delete-dialog.component';

@Component({
  selector: 'jhi-employee-detail',
  templateUrl: './employee-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
  standalone: true,
})
export class EmployeeDetailComponent implements OnInit {
  employee = signal<IEmployee | null>(null);
  isLoading = signal(false);

  loadedData = signal<{
    positions: Record<number, IPosition | null>;
    teams: Record<number, ITeam | null>;
  }>({ positions: {}, teams: {} });

  private route = inject(ActivatedRoute);
  private employeeService = inject(EmployeeService);
  private positionService = inject(PositionService);
  private teamService = inject(TeamService);
  private modalService = inject(NgbModal);

  ngOnInit(): void {
    this.loadEmployee();
  }

  loadEmployee(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isLoading.set(true);
      this.employeeService.find(+id).subscribe({
        next: (response: HttpResponse<IEmployee>) => {
          this.employee.set(response.body);
          if (response.body) {
            this.loadPositionAndTeam(response.body);
          }
          this.isLoading.set(false);
        },
        error: () => {
          this.isLoading.set(false);
        },
      });
    }
  }

  private loadPositionAndTeam(employee: IEmployee): void {
    if (employee.position?.id) {
      this.positionService.find(employee.position.id).subscribe({
        next: (response: HttpResponse<IPosition>) => {
          if (response.body) {
            this.loadedData.update(data => ({
              ...data,
              positions: { ...data.positions, [response.body!.id!]: response.body },
            }));
          }
        },
        error: (error: HttpErrorResponse) => {
          console.error('Error loading position:', error);
        },
      });
    }

    if (employee.team?.id) {
      this.teamService.find(employee.team.id).subscribe({
        next: (response: HttpResponse<ITeam>) => {
          if (response.body) {
            this.loadedData.update(data => ({
              ...data,
              teams: { ...data.teams, [response.body!.id!]: response.body },
            }));
          }
        },
        error: (error: HttpErrorResponse) => {
          console.error('Error loading team:', error);
        },
      });
    }
  }

  get positionName(): string | null {
    const emp = this.employee();
    if (!emp?.position?.id) return null;
    return this.loadedData().positions[emp.position.id]?.positionName || `Position ${emp.position.id}`;
  }

  get teamName(): string | null {
    const emp = this.employee();
    if (!emp?.team?.id) return null;
    return this.loadedData().teams[emp.team.id]?.teamName || `Team ${emp.team.id}`;
  }

  previousState(): void {
    window.history.back();
  }

  deleteProfessionFromEmployee(profession: IProfession): void {
    const modalRef = this.modalService.open(EmployeeProfessionDeleteDialogComponent, {
      size: 'lg',
      backdrop: 'static',
      ariaLabelledBy: 'modal-basic-title',
    });

    const currentEmployee = this.employee();
    if (currentEmployee) {
      modalRef.componentInstance.profession = profession;
      modalRef.componentInstance.employee = currentEmployee;

      modalRef.closed.subscribe((reason: string) => {
        if (reason === 'deleted') {
          this.loadEmployee();
        }
      });
    }
  }
}
