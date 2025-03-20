import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAdditionalTraining } from '../additional-training.model';
import { AdditionalTrainingService } from '../service/additional-training.service';

@Component({
  templateUrl: './additional-training-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AdditionalTrainingDeleteDialogComponent {
  additionalTraining?: IAdditionalTraining;

  protected additionalTrainingService = inject(AdditionalTrainingService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.additionalTrainingService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
