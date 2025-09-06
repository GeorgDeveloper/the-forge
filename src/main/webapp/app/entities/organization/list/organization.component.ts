import { Component, OnInit, inject, signal } from '@angular/core';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { HttpHeaders } from '@angular/common/http';
import { combineLatest, tap } from 'rxjs';
import SharedModule from 'app/shared/shared.module';
import { ItemCountComponent } from 'app/shared/pagination';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SortByDirective, SortDirective, SortService, sortStateSignal, type SortState } from 'app/shared/sort';
import { DEFAULT_SORT_DATA, SORT } from 'app/config/navigation.constants';
import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IOrganization } from '../organization.model';
import { OrganizationService, EntityArrayResponseType } from '../service/organization.service';

@Component({
  selector: 'jhi-organization',
  templateUrl: './organization.component.html',
  imports: [RouterModule, SharedModule, FormsModule, ReactiveFormsModule, SortDirective, SortByDirective, ItemCountComponent],
})
export class OrganizationComponent implements OnInit {
  organizations = signal<IOrganization[]>([]);
  isLoading = false;
  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;
  sortState = sortStateSignal({});

  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly router = inject(Router);
  protected readonly sortService = inject(SortService);
  protected readonly modalService = inject(NgbModal);
  protected readonly organizationService = inject(OrganizationService);

  ngOnInit(): void {
    // Инициализируем параметры из snapshot
    this.fillComponentAttributeFromRoute(this.activatedRoute.snapshot.queryParamMap, this.activatedRoute.snapshot.data);

    // Загружаем данные один раз при инициализации страницы
    this.load();
  }

  delete(organization: IOrganization): void {
    if (!organization.id) return;
    this.organizationService.delete(organization.id).subscribe({ next: () => this.load() });
  }

  refreshList(): void {
    this.load();
  }

  load(): void {
    this.isLoading = true;
    this.organizationService.query(this.getSortQueryParam(this.sortState())).subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onSuccess(res.body, res.headers);
        this.isLoading = false;
      },
      error: () => (this.isLoading = false),
    });
  }

  protected fillComponentAttributeFromRoute(params: any, data: any): void {
    const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
    this.sortState.set({ predicate: sort[0], order: sort[1]?.toLowerCase() === 'asc' ? 'asc' : 'desc' });
    this.page = +(params.get('page') ?? 1);
  }

  navigateToWithComponentValues(event: SortState): void {
    this.sortState.set(event);
    this.updateUrl();
  }

  navigateToPage(page: number): void {
    this.page = page;
    this.updateUrl();
  }

  private updateUrl(): void {
    const queryParams = {
      page: this.page,
      size: this.itemsPerPage,
      sort: this.sortState().predicate + ',' + (this.sortState().order === 'asc' ? 'ASC' : 'DESC'),
    };

    this.router.navigate([], {
      relativeTo: this.activatedRoute,
      queryParams,
      queryParamsHandling: 'merge',
    });

    // НЕ загружаем данные автоматически - только по кнопке "Обновить список"
  }

  protected onSuccess(data: IOrganization[] | null, headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
    this.page = Number(headers.get(PAGE_HEADER));
    this.organizations.set(data ?? []);
  }

  protected getSortQueryParam(sortState: { predicate?: string; order?: 'asc' | 'desc' }): string[] {
    const predicate = sortState.predicate ?? 'id';
    const order = sortState.order ?? 'asc';
    return [`page=${this.page - 1}`, `size=${this.itemsPerPage}`, `sort=${predicate},${order.toUpperCase()}`];
  }
}
