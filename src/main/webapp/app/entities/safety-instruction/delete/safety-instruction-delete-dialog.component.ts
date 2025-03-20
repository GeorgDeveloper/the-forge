import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISafetyInstruction } from '../safety-instruction.model';
import { SafetyInstructionService } from '../service/safety-instruction.service';

@Component({
  templateUrl: './safety-instruction-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SafetyInstructionDeleteDialogComponent {
  safetyInstruction?: ISafetyInstruction;

  protected safetyInstructionService = inject(SafetyInstructionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.safetyInstructionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
