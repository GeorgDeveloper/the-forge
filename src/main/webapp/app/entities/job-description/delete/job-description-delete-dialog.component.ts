import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IJobDescription } from '../job-description.model';
import { JobDescriptionService } from '../service/job-description.service';

@Component({
  templateUrl: './job-description-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class JobDescriptionDeleteDialogComponent {
  jobDescription?: IJobDescription;

  protected jobDescriptionService = inject(JobDescriptionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.jobDescriptionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
