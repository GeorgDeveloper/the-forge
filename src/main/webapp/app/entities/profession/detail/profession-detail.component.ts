import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import FormatMediumDatePipe from 'app/shared/date/format-medium-date.pipe';
import { IProfession } from '../profession.model';

@Component({
  selector: 'jhi-profession-detail',
  templateUrl: './profession-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class ProfessionDetailComponent {
  profession = input<IProfession | null>(null);

  previousState(): void {
    window.history.back();
  }
}
