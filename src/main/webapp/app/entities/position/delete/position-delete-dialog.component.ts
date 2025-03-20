import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPosition } from '../position.model';
import { PositionService } from '../service/position.service';

@Component({
  templateUrl: './position-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PositionDeleteDialogComponent {
  position?: IPosition;

  protected positionService = inject(PositionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.positionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
