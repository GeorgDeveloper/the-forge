import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IAdditionalTraining } from '../additional-training.model';

@Component({
  selector: 'jhi-additional-training-detail',
  templateUrl: './additional-training-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class AdditionalTrainingDetailComponent {
  additionalTraining = input<IAdditionalTraining | null>(null);

  previousState(): void {
    window.history.back();
  }
}
