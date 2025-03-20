import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IJobDescription } from '../job-description.model';

@Component({
  selector: 'jhi-job-description-detail',
  templateUrl: './job-description-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class JobDescriptionDetailComponent {
  jobDescription = input<IJobDescription | null>(null);

  previousState(): void {
    window.history.back();
  }
}
