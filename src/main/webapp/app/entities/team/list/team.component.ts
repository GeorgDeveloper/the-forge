import { Component, NgZone, OnInit, WritableSignal, computed, inject, signal } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
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
import { EntityArrayResponseType, TeamService } from '../service/team.service';
import { TeamDeleteDialogComponent } from '../delete/team-delete-dialog.component';
import { ITeam } from '../team.model';

@Component({
  selector: 'jhi-team',
  templateUrl: './team.component.html',
  imports: [RouterModule, FormsModule, SharedModule, SortDirective, SortByDirective, InfiniteScrollDirective],
})
export class TeamComponent implements OnInit {
  subscription: Subscription | null = null; // Подписка на Observable для управления жизненным циклом
  teams = signal<ITeam[]>([]); // Сигнал для хранения списка команд
  isLoading = false; // Флаг загрузки данных

  sortState = sortStateSignal({}); // Сигнал для хранения состояния сортировки

  itemsPerPage = ITEMS_PER_PAGE; // Количество элементов на странице
  links: WritableSignal<Record<string, undefined | Record<string, string | undefined>>> = signal({}); // Сигнал для хранения ссылок пагинации
  hasMorePage = computed(() => !!this.links().next); // Вычисляемое свойство - есть ли еще страницы
  isFirstFetch = computed(() => Object.keys(this.links()).length === 0); // Вычисляемое свойство - первая ли загрузка данных

  // Инжектируемые сервисы
  public readonly router = inject(Router);
  protected readonly teamService = inject(TeamService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected parseLinks = inject(ParseLinks);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);

  // Функция для отслеживания элементов по id
  trackId = (item: ITeam): number => this.teamService.getTeamIdentifier(item);

  ngOnInit(): void {
    // Подписываемся на изменения параметров маршрута и данных
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        // Обновляем атрибуты компонента на основе параметров маршрута
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        // Сбрасываем состояние перед загрузкой новых данных
        tap(() => this.reset()),
        // Загружаем данные
        tap(() => this.load()),
      )
      .subscribe();
  }

  // Сброс состояния компонента
  reset(): void {
    this.teams.set([]);
  }

  // Загрузка следующей страницы данных
  loadNextPage(): void {
    this.load();
  }

  // Открытие диалога удаления команды
  delete(team: ITeam): void {
    const modalRef = this.modalService.open(TeamDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.team = team;
    // Подписываемся на событие закрытия модального окна
    modalRef.closed
      .pipe(
        // Фильтруем только успешное удаление
        filter(reason => reason === ITEM_DELETED_EVENT),
        // Перезагружаем данные после удаления
        tap(() => this.load()),
      )
      .subscribe();
  }

  // Основной метод загрузки данных
  load(): void {
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  // Обработка изменения сортировки
  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(event);
  }

  // Заполнение атрибутов компонента из параметров маршрута
  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  // Обработка успешного ответа от сервера
  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.teams.set(dataFromBody);
  }

  // Обработка данных из тела ответа
  protected fillComponentAttributesFromResponseBody(data: ITeam[] | null): ITeam[] {
    // Если есть предыдущая страница, добавляем новые данные к существующим (бесконечный скролл)
    if (this.links().prev) {
      const teamsNew = this.teams();
      if (data) {
        for (const d of data) {
          if (teamsNew.some(op => op.id === d.id)) {
            teamsNew.push(d);
          }
        }
      }
      return teamsNew;
    }
    return data ?? [];
  }

  // Обработка заголовков ответа (пагинация)
  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links.set(this.parseLinks.parseAll(linkHeader));
    } else {
      this.links.set({});
    }
  }

  // Запрос данных с сервера
  protected queryBackend(): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const queryObject: any = {
      size: this.itemsPerPage,
    };
    // Если есть следующая страница, используем ее параметры
    if (this.hasMorePage()) {
      Object.assign(queryObject, this.links().next);
    } else if (this.isFirstFetch()) {
      // Для первой загрузки добавляем параметры сортировки
      Object.assign(queryObject, { sort: this.sortService.buildSortParam(this.sortState()) });
    }

    return this.teamService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  // Обработка навигации при изменении сортировки
  protected handleNavigation(sortState: SortState): void {
    this.links.set({}); // Сбрасываем ссылки пагинации

    const queryParamsObj = {
      sort: this.sortService.buildSortParam(sortState),
    };

    // Выполняем навигацию внутри зоны Angular
    this.ngZone.run(() => {
      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    });
  }
}
