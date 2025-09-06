import { Component, input, signal } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { ITask } from '../task.model';
import { IEmployee } from '../../employee/employee.model';
import { EmployeeService } from '../../employee/service/employee.service';
import { TaskService } from '../service/task.service';
import { TaskCompletionModalComponent } from './task-completion-modal.component';
import { TaskStatus } from '../../enumerations/task-status.model';
import dayjs from 'dayjs/esm';

@Component({
  selector: 'jhi-task-detail',
  templateUrl: './task-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class TaskDetailComponent {
  task = input<ITask | null>(null);

  loadedData = signal<{
    employees: Record<number, IEmployee | null>;
  }>({ employees: {} });

  constructor(
    private employeeService: EmployeeService,
    private taskService: TaskService,
    private modalService: NgbModal,
  ) {}

  ngOnInit() {
    const task = this.task();
    if (task?.employee?.id) {
      this.employeeService.find(task.employee.id).subscribe({
        next: (response: HttpResponse<IEmployee>) => {
          if (response.body) {
            this.loadedData.update(data => ({
              ...data,
              employees: { ...data.employees, [response.body!.id!]: response.body },
            }));
          }
        },
        error: (error: HttpErrorResponse) => {
          console.error('Error loading employee:', error);
        },
      });
    }
  }

  get employeeFullName(): string | null {
    const task = this.task();
    if (!task?.employee?.id) return null;

    const employee = this.loadedData().employees[task.employee.id];
    return employee ? `${employee.firstName || ''} ${employee.lastName || ''}`.trim() : `Employee ${task.employee.id}`;
  }

  previousState(): void {
    window.history.back();
  }

  completeTask(): void {
    const task = this.task();
    if (!task) {
      return;
    }

    const modalRef = this.modalService.open(TaskCompletionModalComponent, {
      size: 'md',
      backdrop: 'static',
    });

    modalRef.componentInstance.taskName = task.taskName;

    modalRef.result.then(
      (completionDate: string) => {
        const updatedTask: ITask = {
          ...task,
          actualCompletionDate: dayjs(completionDate),
          status: TaskStatus.DONE,
        };

        console.log('Updating task with:', updatedTask);
        this.taskService.update(updatedTask).subscribe({
          next: (response: HttpResponse<ITask>) => {
            console.log('Task updated successfully:', response.body);
            // Обновляем данные задачи
            if (response.body) {
              // Перенаправляем на эту же страницу для обновления данных
              window.location.reload();
            }
          },
          error: (error: HttpErrorResponse) => {
            console.error('Error updating task:', error);
            console.error('Error details:', error.error);
            alert('Не удалось завершить задачу: ' + (error.error?.message || error.message));
          },
        });
      },
      () => {
        // Модальное окно было отменено
      },
    );
  }
}
