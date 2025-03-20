import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProtectiveEquipment } from '../protective-equipment.model';
import { ProtectiveEquipmentService } from '../service/protective-equipment.service';

@Component({
  templateUrl: './protective-equipment-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProtectiveEquipmentDeleteDialogComponent {
  protectiveEquipment?: IProtectiveEquipment;

  protected protectiveEquipmentService = inject(ProtectiveEquipmentService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.protectiveEquipmentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
