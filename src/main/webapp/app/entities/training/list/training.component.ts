import { Component, NgZone, OnInit, WritableSignal, inject, signal } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { FormatMediumDatePipe } from 'app/shared/date';
import { ItemCountComponent } from 'app/shared/pagination';
import { FormsModule } from '@angular/forms';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { ITraining } from '../training.model';
import { EntityArrayResponseType, TrainingService } from '../service/training.service';
import { TrainingDeleteDialogComponent } from '../delete/training-delete-dialog.component';
import { IEmployee } from '../../employee/employee.model';
import { EmployeeService } from '../../employee/service/employee.service';

@Component({
  selector: 'jhi-training',
  templateUrl: './training.component.html',
  imports: [RouterModule, FormsModule, SharedModule, SortDirective, SortByDirective, FormatMediumDatePipe, ItemCountComponent],
})
export class TrainingComponent implements OnInit {
  // Подписка на изменения маршрута
  subscription: Subscription | null = null;

  // Сигнал для хранения списка тренировок
  trainings = signal<ITraining[]>([]);

  // Флаг загрузки данных
  isLoading = false;

  // Сигнал для кэширования данных о сотрудниках
  loadedEmployees = signal<Record<number, IEmployee | null>>({});

  // Сигнал для состояния сортировки
  sortState = sortStateSignal({});

  // Настройки пагинации
  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  // Инжектируем зависимости
  public readonly router = inject(Router);
  protected readonly trainingService = inject(TrainingService);
  protected readonly employeeService = inject(EmployeeService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected readonly modalService = inject(NgbModal);
  protected readonly ngZone = inject(NgZone);

  // Метод для получения идентификатора тренировки
  trackId = (item: ITraining): number => this.trainingService.getTrainingIdentifier(item);

  ngOnInit(): void {
    // Подписываемся на изменения параметров маршрута
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        // Обновляем параметры компонента из маршрута
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        // Загружаем данные
        tap(() => this.load()),
      )
      .subscribe();
  }

  // Метод для удаления тренировки
  delete(training: ITraining): void {
    const modalRef = this.modalService.open(TrainingDeleteDialogComponent, {
      size: 'lg',
      backdrop: 'static',
    });
    modalRef.componentInstance.training = training;

    // После закрытия модального окна обновляем список
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  // Основной метод загрузки данных
  load(): void {
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
        // После загрузки тренировок загружаем данные сотрудников
        this.loadEmployeesForTrainings();
      },
    });
  }

  // Метод для загрузки данных о сотрудниках
  loadEmployeesForTrainings(): void {
    const trainings = this.trainings();

    // Для каждой тренировки загружаем данные сотрудника, если они еще не загружены
    trainings.forEach(training => {
      if (training.employee?.id && !this.loadedEmployees()[training.employee.id]) {
        this.employeeService.find(training.employee.id).subscribe({
          next: (response: HttpResponse<IEmployee>) => {
            if (response.body) {
              // Обновляем кэш сотрудников
              this.loadedEmployees.update(data => ({
                ...data,
                [response.body!.id!]: response.body,
              }));
            }
          },
          error: (error: HttpErrorResponse) => {
            console.error('Error loading employee:', error);
          },
        });
      }
    });
  }

  // Метод для получения полного имени сотрудника
  getEmployeeFullName(employeeId: number | null | undefined): string {
    if (!employeeId) return '';

    const employee = this.loadedEmployees()[employeeId];
    return employee ? `${employee.firstName || ''} ${employee.lastName || ''}`.trim() : `Employee ${employeeId}`;
  }

  // Методы навигации и обработки сортировки
  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(this.page, event);
  }

  navigateToPage(page: number): void {
    this.handleNavigation(page, this.sortState());
  }

  // Вспомогательные методы для работы с маршрутами и ответами API
  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.trainings.set(dataFromBody);
  }

  protected fillComponentAttributesFromResponseBody(data: ITraining[] | null): ITraining[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const pageToLoad: number = this.page;

    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(this.sortState()),
    };

    return this.trainingService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page: number, sortState: SortState): void {
    const queryParamsObj = {
      page,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(sortState),
    };

    this.ngZone.run(() => {
      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    });
  }
}
