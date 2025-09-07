import { Component, input, inject } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { ISafetyInstruction } from '../safety-instruction.model';
import { SafetyInstructionService } from '../service/safety-instruction.service';

@Component({
  selector: 'jhi-safety-instruction-detail',
  templateUrl: './safety-instruction-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class SafetyInstructionDetailComponent {
  safetyInstruction = input<ISafetyInstruction | null>(null);

  protected safetyInstructionService = inject(SafetyInstructionService);

  previousState(): void {
    window.history.back();
  }

  downloadPdf(): void {
    const instruction = this.safetyInstruction();
    if (instruction?.id) {
      this.safetyInstructionService.downloadPdfFile(instruction.id).subscribe({
        next: blob => {
          const url = window.URL.createObjectURL(blob);
          const link = document.createElement('a');
          link.href = url;
          link.download = instruction.pdfFileName || 'instruction.pdf';
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
    const instruction = this.safetyInstruction();
    if (instruction?.id) {
      this.safetyInstructionService.downloadPdfFile(instruction.id).subscribe({
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
