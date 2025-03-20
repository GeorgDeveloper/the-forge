import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IPosition } from '../position.model';

@Component({
  selector: 'jhi-position-detail',
  templateUrl: './position-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class PositionDetailComponent {
  position = input<IPosition | null>(null);

  previousState(): void {
    window.history.back();
  }
}
