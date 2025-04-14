import { TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { ActivatedRoute, ActivatedRouteSnapshot, Router, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { ITeam } from '../team.model';
import { TeamService } from '../service/team.service';
import teamResolve from './team-routing-resolve.service';

describe('Team routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let service: TeamService;
  let resultTeam: ITeam | null | undefined;

  beforeEach(() => {
    // Настройка тестового модуля
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(), // Мок HttpClient
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}), // Пустые параметры по умолчанию
            },
          },
        },
      ],
    });

    // Получаем моки сервисов
    mockRouter = TestBed.inject(Router);
    // Мокируем метод navigate
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));

    // Получаем snapshot маршрута
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    // Получаем сервис команд
    service = TestBed.inject(TeamService);
    // Инициализируем переменную результата
    resultTeam = undefined;
  });

  // Тесты для метода resolve
  describe('resolve', () => {
    it('should return ITeam returned by find', () => {
      // ПОДГОТОВКА (GIVEN)
      // Мокируем метод find сервиса
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      // Устанавливаем параметры маршрута
      mockActivatedRouteSnapshot.params = { id: 123 };

      // ДЕЙСТВИЕ (WHEN)
      // Запускаем резолвер в контексте инжектора
      TestBed.runInInjectionContext(() => {
        teamResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultTeam = result;
          },
        });
      });

      // ПРОВЕРКА (THEN)
      // Проверяем что find вызван с правильным ID
      expect(service.find).toHaveBeenCalledWith(123);
      // Проверяем что получены ожидаемые данные
      expect(resultTeam).toEqual({ id: 123 });
    });

    it('should return null if id is not provided', () => {
      // ПОДГОТОВКА
      service.find = jest.fn(); // Мок метода find
      mockActivatedRouteSnapshot.params = {}; // Пустые параметры

      // ДЕЙСТВИЕ
      TestBed.runInInjectionContext(() => {
        teamResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultTeam = result;
          },
        });
      });

      // ПРОВЕРКА
      expect(service.find).not.toHaveBeenCalled(); // Метод find не должен вызываться
      expect(resultTeam).toEqual(null); // Ожидаем null
    });

    it('should route to 404 page if data not found in server', () => {
      // ПОДГОТОВКА
      // Мокируем метод find возвращающий null
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse<ITeam>({ body: null })));
      mockActivatedRouteSnapshot.params = { id: 123 }; // Устанавливаем ID

      // ДЕЙСТВИЕ
      TestBed.runInInjectionContext(() => {
        teamResolve(mockActivatedRouteSnapshot).subscribe({
          next(result) {
            resultTeam = result;
          },
        });
      });

      // ПРОВЕРКА
      expect(service.find).toHaveBeenCalledWith(123); // Проверяем вызов find
      expect(resultTeam).toEqual(undefined); // Результат должен быть undefined
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']); // Должен быть переход на 404
    });
  });
});
