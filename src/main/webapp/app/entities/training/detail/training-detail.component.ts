import { Component, input, signal } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

import SharedModule from 'app/shared/shared.module';
import { ITraining } from '../training.model';
import { IEmployee } from '../../employee/employee.model';
import { EmployeeService } from '../../employee/service/employee.service';
import { FormatMediumDatePipe } from 'app/shared/date';
import { TrainingService } from '../service/training.service';
import dayjs from 'dayjs/esm';
import { Router } from '@angular/router';

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

  constructor(
    private employeeService: EmployeeService,
    private trainingService: TrainingService,
    private router: Router,
  ) {}

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

  conductTraining(): void {
    const training = this.training();
    if (!training) {
      return;
    }

    const now = dayjs();
    const periodMonths = training.validityPeriod ?? 0;
    const next = now.add(periodMonths, 'month');

    const confirmMsg = `Провести инструктаж сейчас?\nПоследний: ${now.format('YYYY-MM-DD')}\nСледующий: ${next.format('YYYY-MM-DD')}`;
    if (!window.confirm(confirmMsg)) {
      return;
    }

    const updated: ITraining = {
      ...training,
      lastTrainingDate: now,
      nextTrainingDate: next,
    };

    this.trainingService.update(updated).subscribe({
      next: (res: HttpResponse<ITraining>) => {
        // Перенаправим на эту же страницу для обновления данных
        const id = res.body?.id ?? training.id;
        this.router.navigate(['/training', id, 'view']);
      },
      error: (error: HttpErrorResponse) => {
        console.error('Error updating training:', error);
        alert('Не удалось сохранить изменения инструктажа');
      },
    });
  }
}
