import { IEmployee } from 'app/entities/employee/employee.model';
import { IProfession } from 'app/entities/profession/profession.model';
import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { TranslateModule } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'jhi-employee-profession-delete-dialog',
  standalone: true,
  imports: [CommonModule, FaIconComponent, TranslateModule],
  template: `
    <div class="modal-header">
      <h4 class="modal-title" id="modal-basic-title" jhiTranslate="entity.delete.title"></h4>
      <button type="button" class="btn-close" aria-label="Close" (click)="dismiss()"></button>
    </div>

    <div class="modal-body">
      <p>
        {{
          'theForgeApp.employee.profession_update.delete.question'
            | translate: { lastName: employee.lastName, professionName: profession.professionName }
        }}
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
export class EmployeeProfessionDeleteDialogComponent {
  @Input({ required: true }) employee!: IEmployee;
  @Input({ required: true }) profession!: IProfession;

  constructor(
    protected activeModal: NgbActiveModal,
    protected employeeService: EmployeeService,
  ) {}

  dismiss(): void {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(): void {
    this.employeeService.removeProfessionFromEmployee(this.employee.id, this.profession.id).subscribe({
      next: () => this.activeModal.close('deleted'),
      error: () => this.activeModal.dismiss('error'),
    });
  }
}
