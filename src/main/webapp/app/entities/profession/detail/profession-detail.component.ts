import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IProfession } from '../profession.model';

@Component({
  selector: 'jhi-profession-detail',
  templateUrl: './profession-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ProfessionDetailComponent {
  profession = input<IProfession | null>(null);

  previousState(): void {
    window.history.back();
  }
}
