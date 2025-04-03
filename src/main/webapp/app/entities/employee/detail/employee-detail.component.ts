import { Component, input, signal } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IEmployee } from '../employee.model';
import { IPosition } from '../../position/position.model';
import { ITeam } from '../../team/team.model';
import { PositionService } from '../../position/service/position.service';
import { TeamService } from '../../team/service/team.service';

@Component({
  selector: 'jhi-employee-detail',
  templateUrl: './employee-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class EmployeeDetailComponent {
  employee = input<IEmployee | null>(null);
  loadedData = signal<{
    positions: Record<number, IPosition | null>;
    teams: Record<number, ITeam | null>;
  }>({ positions: {}, teams: {} });

  constructor(
    private positionService: PositionService,
    private teamService: TeamService,
  ) {}

  ngOnInit() {
    const emp = this.employee();
    if (emp?.position?.id) {
      this.positionService.find(emp.position.id).subscribe({
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

    if (emp?.team?.id) {
      this.teamService.find(emp.team.id).subscribe({
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
}
