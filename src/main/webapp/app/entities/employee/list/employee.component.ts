import { Component, NgZone, OnInit, inject, signal } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
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
import { IEmployee } from '../employee.model';
import { IEmployeeWithLastInstructionDate } from '../employee-with-last-instruction-date.model';
import { EmployeeService, EntityArrayResponseType } from '../service/employee.service';
import { EmployeeDeleteDialogComponent } from '../delete/employee-delete-dialog.component';

@Component({
  selector: 'jhi-employee',
  templateUrl: './employee.component.html',
  imports: [RouterModule, FormsModule, SharedModule, SortDirective, SortByDirective, FormatMediumDatePipe, ItemCountComponent],
})
export class EmployeeComponent implements OnInit {
  subscription: Subscription | null = null;
  employees = signal<IEmployee[]>([]);
  employeesWithLastInstructionDate = signal<IEmployeeWithLastInstructionDate[]>([]);
  isLoading = false;

  sortState = sortStateSignal({});

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  public readonly router = inject(Router);
  protected readonly employeeService = inject(EmployeeService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);

  trackId = (item: IEmployee): number => this.employeeService.getEmployeeIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.load()),
      )
      .subscribe();
  }

  delete(employee: IEmployee): void {
    const modalRef = this.modalService.open(EmployeeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.employee = employee;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackendWithLastInstructionDate().subscribe({
      next: (res: HttpResponse<IEmployeeWithLastInstructionDate[]>) => {
        this.onResponseSuccessWithLastInstructionDate(res);
      },
    });
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(this.page, event);
  }

  navigateToPage(page: number): void {
    this.handleNavigation(page, this.sortState());
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected onResponseSuccessWithLastInstructionDate(response: HttpResponse<IEmployeeWithLastInstructionDate[]>): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBodyWithLastInstructionDate(response.body);
    this.employeesWithLastInstructionDate.set(dataFromBody);

    // Конвертируем DTO в обычные Employee для совместимости
    const employees: IEmployee[] = dataFromBody.map(dto => ({
      id: dto.id,
      firstName: dto.firstName,
      lastName: dto.lastName,
      birthDate: dto.birthDate,
      employeeNumber: dto.employeeNumber,
      hireDate: dto.hireDate,
      lastInstructionDate: dto.lastInstructionDate,
      position: dto.position,
      professions: [],
      team: dto.team,
    }));
    this.employees.set(employees);
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.employees.set(dataFromBody);
  }

  protected fillComponentAttributesFromResponseBodyWithLastInstructionDate(
    data: IEmployeeWithLastInstructionDate[] | null,
  ): IEmployeeWithLastInstructionDate[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseBody(data: IEmployee[] | null): IEmployee[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackendWithLastInstructionDate(): Observable<HttpResponse<IEmployeeWithLastInstructionDate[]>> {
    const { page } = this;

    this.isLoading = true;
    const pageToLoad: number = page;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    return this.employeeService.queryWithLastInstructionDate(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    const { page } = this;

    this.isLoading = true;
    const pageToLoad: number = page;
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      eagerload: true,
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    return this.employeeService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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
