import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TeamService } from '../service/team.service';
import SharedModule from 'app/shared/shared.module';
import { ITeam } from '../team.model';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'jhi-team-detail',
  templateUrl: './team-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class TeamDetailComponent {
  private teamService = inject(TeamService);
  private route = inject(ActivatedRoute);

  // Используем signal() вместо input()
  team = signal<ITeam | null>(null);
  isLoading = false;

  constructor() {
    this.loadTeamWithEmployees();
  }

  loadTeamWithEmployees(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isLoading = true;
      this.teamService.findWithEmployees(+id).subscribe({
        next: response => {
          this.team.set(response.body); // Теперь set доступен
          this.isLoading = false;
        },
        error: () => {
          this.isLoading = false;
        },
      });
    }
  }

  previousState(): void {
    window.history.back();
  }
}
