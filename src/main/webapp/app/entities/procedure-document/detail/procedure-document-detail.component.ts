import { Component, input, inject } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IProcedureDocument } from '../procedure-document.model';
import { ProcedureDocumentService } from '../service/procedure-document.service';

@Component({
  selector: 'jhi-procedure-document-detail',
  templateUrl: './procedure-document-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class ProcedureDocumentDetailComponent {
  procedureDocument = input<IProcedureDocument | null>(null);

  protected procedureDocumentService = inject(ProcedureDocumentService);

  previousState(): void {
    window.history.back();
  }

  downloadPdf(): void {
    const document = this.procedureDocument();
    if (document?.id) {
      this.procedureDocumentService.downloadPdfFile(document.id).subscribe({
        next: blob => {
          const url = window.URL.createObjectURL(blob);
          const link = window.document.createElement('a');
          link.href = url;
          link.download = document.pdfFileName || 'procedure-document.pdf';
          link.click();
          window.URL.revokeObjectURL(url);
        },
        error: () => {
          alert('Ошибка при скачивании файла');
        },
      });
    }
  }

  viewPdf(): void {
    const document = this.procedureDocument();
    if (document?.id) {
      this.procedureDocumentService.downloadPdfFile(document.id).subscribe({
        next: blob => {
          const url = window.URL.createObjectURL(blob);
          window.open(url, '_blank');
        },
        error: () => {
          alert('Ошибка при открытии файла');
        },
      });
    }
  }
}
