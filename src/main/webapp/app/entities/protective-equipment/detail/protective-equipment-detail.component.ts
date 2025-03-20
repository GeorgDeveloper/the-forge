import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IProtectiveEquipment } from '../protective-equipment.model';

@Component({
  selector: 'jhi-protective-equipment-detail',
  templateUrl: './protective-equipment-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ProtectiveEquipmentDetailComponent {
  protectiveEquipment = input<IProtectiveEquipment | null>(null);

  previousState(): void {
    window.history.back();
  }
}
