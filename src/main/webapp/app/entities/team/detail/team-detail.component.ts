import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ITeam } from '../team.model';

@Component({
  selector: 'jhi-team-detail',
  templateUrl: './team-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class TeamDetailComponent {
  team = input<ITeam | null>(null);

  previousState(): void {
    window.history.back();
  }
}
