import { Component, input, signal } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

import SharedModule from 'app/shared/shared.module';
import { ITraining } from '../training.model';
import { IEmployee } from '../../employee/employee.model';
import { EmployeeService } from '../../employee/service/employee.service';
import { FormatMediumDatePipe } from 'app/shared/date';

@Component({
  selector: 'jhi-training-detail',
  templateUrl: './training-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class TrainingDetailComponent {
  training = input<ITraining | null>(null);
  loadedData = signal<{
    employees: Record<number, IEmployee | null>;
  }>({ employees: {} });

  constructor(private employeeService: EmployeeService) {}

  ngOnInit() {
    const training = this.training();
    if (training?.employee?.id) {
      this.employeeService.find(training.employee.id).subscribe({
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
    const training = this.training();
    if (!training?.employee?.id) return null;

    const employee = this.loadedData().employees[training.employee.id];
    return employee ? `${employee.firstName || ''} ${employee.lastName || ''}`.trim() : `Employee ${training.employee.id}`;
  }

  previousState(): void {
    window.history.back();
  }
}
