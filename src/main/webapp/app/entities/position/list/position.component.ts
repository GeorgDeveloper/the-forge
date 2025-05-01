import { Component, NgZone, OnInit, WritableSignal, computed, inject, signal } from '@angular/core';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { FormsModule } from '@angular/forms';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { InfiniteScrollDirective } from 'ngx-infinite-scroll';
import { EntityArrayResponseType, PositionService } from '../service/position.service';
import { PositionDeleteDialogComponent } from '../delete/position-delete-dialog.component';
import { IPosition } from '../position.model';
import { JobDescriptionService } from '../../job-description/service/job-description.service';
import { IJobDescription } from '../../job-description/job-description.model';

@Component({
  selector: 'jhi-position',
  templateUrl: './position.component.html',
  imports: [RouterModule, FormsModule, SharedModule, SortDirective, SortByDirective, InfiniteScrollDirective],
})
export class PositionComponent implements OnInit {
  subscription: Subscription | null = null;
  positions = signal<IPosition[]>([]);
  isLoading = false;

  // Добавляем сигнал для хранения загруженных JobDescription
  loadedJobDescriptions = signal<Record<number, IJobDescription | null>>({});

  sortState = sortStateSignal({});

  itemsPerPage = ITEMS_PER_PAGE;
  links: WritableSignal<Record<string, undefined | Record<string, string | undefined>>> = signal({});
  hasMorePage = computed(() => !!this.links().next);
  isFirstFetch = computed(() => Object.keys(this.links()).length === 0);

  public readonly router = inject(Router);
  protected readonly positionService = inject(PositionService);
  protected readonly jobDescriptionService = inject(JobDescriptionService); // Добавляем сервис
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected readonly parseLinks = inject(ParseLinks);
  protected readonly modalService = inject(NgbModal);
  protected readonly ngZone = inject(NgZone);

  trackId = (item: IPosition): number => this.positionService.getPositionIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.reset()),
        tap(() => this.load()),
      )
      .subscribe();
  }

  reset(): void {
    this.positions.set([]);
    this.loadedJobDescriptions.set({}); // Сбрасываем загруженные описания
  }

  loadNextPage(): void {
    this.load();
  }

  delete(position: IPosition): void {
    const modalRef = this.modalService.open(PositionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.position = position;
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
        // После загрузки позиций загружаем их JobDescription
        this.loadJobDescriptionsForPositions();
      },
    });
  }

  // Новый метод: загружает JobDescription для всех позиций
  loadJobDescriptionsForPositions(): void {
    const positions = this.positions();
    positions.forEach(position => {
      if (position.jobDescription?.id && !this.loadedJobDescriptions()[position.jobDescription.id]) {
        this.jobDescriptionService.find(position.jobDescription.id).subscribe({
          next: (response: HttpResponse<IJobDescription>) => {
            if (response.body) {
              this.loadedJobDescriptions.update(data => ({
                ...data,
                [response.body!.id!]: response.body,
              }));
            }
          },
          error: (error: HttpErrorResponse) => {
            console.error('Error loading job description:', error);
          },
        });
      }
    });
  }

  // Геттер для получения descriptionName по id JobDescription
  getDescriptionName(jobDescriptionId: number | null | undefined): string | null {
    if (!jobDescriptionId) return null;
    return this.loadedJobDescriptions()[jobDescriptionId]?.descriptionName || `Job Description ${jobDescriptionId}`;
  }

  // Остальные методы остаются без изменений
  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(event);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.positions.set(dataFromBody);
  }

  protected fillComponentAttributesFromResponseBody(data: IPosition[] | null): IPosition[] {
    if (this.links().prev) {
      const positionsNew = this.positions();
      if (data) {
        for (const d of data) {
          if (!positionsNew.some(op => op.id === d.id)) {
            positionsNew.push(d);
          }
        }
      }
      return positionsNew;
    }
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links.set(this.parseLinks.parseAll(linkHeader));
    } else {
      this.links.set({});
    }
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const queryObject: any = {
      size: this.itemsPerPage,
    };
    if (this.hasMorePage()) {
      Object.assign(queryObject, this.links().next);
    } else if (this.isFirstFetch()) {
      Object.assign(queryObject, { sort: this.sortService.buildSortParam(this.sortState()) });
    }

    return this.positionService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(sortState: SortState): void {
    this.links.set({});

    const queryParamsObj = {
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
