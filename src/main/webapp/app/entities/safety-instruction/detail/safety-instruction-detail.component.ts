import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { ISafetyInstruction } from '../safety-instruction.model';

@Component({
  selector: 'jhi-safety-instruction-detail',
  templateUrl: './safety-instruction-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class SafetyInstructionDetailComponent {
  safetyInstruction = input<ISafetyInstruction | null>(null);

  previousState(): void {
    window.history.back();
  }
}
