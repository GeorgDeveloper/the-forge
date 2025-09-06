import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import dayjs from 'dayjs/esm';

@Component({
  selector: 'jhi-task-completion-modal',
  standalone: true,
  imports: [CommonModule, FormsModule, FontAwesomeModule],
  templateUrl: './task-completion-modal.component.html',
  styleUrls: ['./task-completion-modal.component.scss'],
})
export class TaskCompletionModalComponent {
  @Input() taskName?: string | null;

  completionDate: string = dayjs().format('YYYY-MM-DD');

  constructor(public activeModal: NgbActiveModal) {}

  // Сохранение даты выполнения
  save(): void {
    this.activeModal.close(this.completionDate);
  }

  // Отмена
  cancel(): void {
    this.activeModal.dismiss('cancel');
  }
}
