import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpHeaders, HttpResponse, provideHttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subject, of } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { sampleWithRequiredData } from '../team.test-samples';
import { TeamService } from '../service/team.service';

import { TeamComponent } from './team.component';
import SpyInstance = jest.SpyInstance;

describe('Team Management Component', () => {
  let comp: TeamComponent;
  let fixture: ComponentFixture<TeamComponent>;
  let service: TeamService;
  let routerNavigateSpy: SpyInstance<Promise<boolean>>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TeamComponent],
      providers: [
        provideHttpClient(),
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc', // Мокируем данные маршрута
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc', // Параметры сортировки
              }),
            ),
            snapshot: {
              queryParams: {},
              queryParamMap: jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc', // Параметры сортировки для снапшота
              }),
            },
          },
        },
      ],
    })
      .overrideTemplate(TeamComponent, '') // Отключаем шаблон для изоляции тестов
      .compileComponents();

    fixture = TestBed.createComponent(TeamComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TeamService);
    routerNavigateSpy = jest.spyOn(comp.router, 'navigate'); // Следим за навигацией

    // Мокируем сервис запросов с разными ответами
    jest
      .spyOn(service, 'query')
      .mockReturnValueOnce(
        of(
          new HttpResponse({
            body: [{ id: 1226 }], // Первый ответ с одной командой
            headers: new HttpHeaders({
              link: '<http://localhost/api/foo?page=1&size=20>; rel="next"', // Заголовок пагинации
            }),
          }),
        ),
      )
      .mockReturnValueOnce(
        of(
          new HttpResponse({
            body: [{ id: 14592 }], // Второй ответ с другой командой
            headers: new HttpHeaders({
              link: '<http://localhost/api/foo?page=0&size=20>; rel="prev",<http://localhost/api/foo?page=2&size=20>; rel="next"',
            }),
          }),
        ),
      );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled(); // Проверяем вызов сервиса
    expect(comp.teams()[0]).toEqual(expect.objectContaining({ id: 1226 })); // Проверяем данные
  });

  describe('trackId', () => {
    it('Should forward to teamService', () => {
      const entity = { id: 1226 };
      jest.spyOn(service, 'getTeamIdentifier');
      const id = comp.trackId(entity);
      expect(service.getTeamIdentifier).toHaveBeenCalledWith(entity); // Проверяем вызов сервиса
      expect(id).toBe(entity.id); // Проверяем возвращаемое значение
    });
  });

  it('should calculate the sort attribute for a non-id attribute', () => {
    // WHEN
    comp.navigateToWithComponentValues({ predicate: 'non-existing-column', order: 'asc' });

    // THEN
    expect(routerNavigateSpy).toHaveBeenLastCalledWith(
      expect.anything(),
      expect.objectContaining({
        queryParams: expect.objectContaining({
          sort: ['non-existing-column,asc'], // Проверяем параметры сортировки
        }),
      }),
    );
  });

  it('should calculate the sort attribute for an id', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenLastCalledWith(expect.objectContaining({ sort: ['id,desc'] }));
  });

  it('should infinite scroll', () => {
    // GIVEN
    comp.loadNextPage(); // Первая загрузка
    comp.loadNextPage(); // Вторая загрузка
    comp.loadNextPage(); // Третья загрузка

    // THEN
    expect(service.query).toHaveBeenCalledTimes(3); // Проверяем количество вызовов
    expect(service.query).toHaveBeenNthCalledWith(2, expect.objectContaining({ page: '1' }));
    expect(service.query).toHaveBeenLastCalledWith(expect.objectContaining({ page: '2' }));
  });

  describe('delete', () => {
    let ngbModal: NgbModal;
    let deleteModalMock: any;

    beforeEach(() => {
      deleteModalMock = { componentInstance: {}, closed: new Subject() }; // Мок модального окна
      ngbModal = (comp as any).modalService;
      jest.spyOn(ngbModal, 'open').mockReturnValue(deleteModalMock); // Мокируем открытие модалки
    });

    it('on confirm should call load', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(comp, 'load');

        // WHEN
        comp.delete(sampleWithRequiredData);
        deleteModalMock.closed.next('deleted'); // Эмулируем успешное удаление
        tick(); // Ожидаем завершение асинхронных операций

        // THEN
        expect(ngbModal.open).toHaveBeenCalled();
        expect(comp.load).toHaveBeenCalled(); // Проверяем перезагрузку данных
      }),
    ));

    it('on dismiss should call load', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(comp, 'load');

        // WHEN
        comp.delete(sampleWithRequiredData);
        deleteModalMock.closed.next(); // Эмулируем отмену удаления
        tick();

        // THEN
        expect(ngbModal.open).toHaveBeenCalled();
        expect(comp.load).not.toHaveBeenCalled(); // Проверяем что данные не перезагружались
      }),
    ));
  });
});
