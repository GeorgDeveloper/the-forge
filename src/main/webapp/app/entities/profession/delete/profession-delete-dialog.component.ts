import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProfession } from '../profession.model';
import { ProfessionService } from '../service/profession.service';

@Component({
  templateUrl: './profession-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProfessionDeleteDialogComponent {
  profession?: IProfession;

  protected professionService = inject(ProfessionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.professionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
