import { Component, input, signal } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { ITask } from '../task.model';
import { IEmployee } from '../../employee/employee.model';
import { EmployeeService } from '../../employee/service/employee.service';

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

  constructor(private employeeService: EmployeeService) {}

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
}
