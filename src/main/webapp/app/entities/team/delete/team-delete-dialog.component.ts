import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITeam } from '../team.model';
import { TeamService } from '../service/team.service';

@Component({
  templateUrl: './team-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TeamDeleteDialogComponent {
  team?: ITeam;

  protected teamService = inject(TeamService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.teamService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
