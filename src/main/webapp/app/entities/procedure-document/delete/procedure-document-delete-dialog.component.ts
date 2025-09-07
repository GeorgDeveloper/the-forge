import { Component, inject } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

import { IProcedureDocument } from '../procedure-document.model';
import { ProcedureDocumentService } from '../service/procedure-document.service';

@Component({
  templateUrl: './procedure-document-delete-dialog.component.html',
  imports: [FontAwesomeModule],
})
export class ProcedureDocumentDeleteDialogComponent {
  procedureDocument?: IProcedureDocument;

  protected procedureDocumentService = inject(ProcedureDocumentService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.procedureDocumentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
