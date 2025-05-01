import { ITeam } from '../team.model';
import { IUser } from 'app/entities/user/user.model';
import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { TeamService } from '../service/team.service';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { TranslateModule } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'jhi-team-user-delete-dialog',
  standalone: true,
  imports: [CommonModule, FaIconComponent, TranslateModule],
  template: `
    <div class="modal-header">
      <h4 class="modal-title" id="modal-basic-title" jhiTranslate="entity.delete.title"></h4>
      <button type="button" class="btn-close" aria-label="Close" (click)="dismiss()"></button>
    </div>

    <div class="modal-body">
      <p>
        {{ 'theForgeApp.team.user.delete.question' | translate: { login: user.login, teamName: team.teamName } }}
      </p>
    </div>

    <div class="modal-footer">
      <button type="button" class="btn btn-secondary" (click)="dismiss()">
        <fa-icon [icon]="['fas', 'ban']"></fa-icon>&nbsp;<span jhiTranslate="entity.action.cancel"></span>
      </button>

      <button type="button" class="btn btn-danger" (click)="confirmDelete()" data-cy="entityConfirmDeleteButton">
        <fa-icon [icon]="['fas', 'trash']"></fa-icon>&nbsp;<span jhiTranslate="entity.action.delete"></span>
      </button>
    </div>
  `,
})
export class TeamUserDeleteDialogComponent {
  @Input({ required: true }) user!: IUser;
  @Input({ required: true }) team!: ITeam;

  constructor(
    protected activeModal: NgbActiveModal,
    protected teamService: TeamService,
  ) {}

  dismiss(): void {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(): void {
    this.teamService.removeUserFromTeam(this.team.id, this.user.id).subscribe({
      next: () => this.activeModal.close('deleted'),
      error: () => this.activeModal.dismiss('error'),
    });
  }
}
