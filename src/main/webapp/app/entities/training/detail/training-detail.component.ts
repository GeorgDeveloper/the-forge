import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { ITraining } from '../training.model';

@Component({
  selector: 'jhi-training-detail',
  templateUrl: './training-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class TrainingDetailComponent {
  training = input<ITraining | null>(null);

  previousState(): void {
    window.history.back();
  }
}
