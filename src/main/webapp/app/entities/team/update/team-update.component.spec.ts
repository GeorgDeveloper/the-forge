import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { TeamService } from '../service/team.service';
import { ITeam } from '../team.model';
import { TeamFormService } from './team-form.service';
import { TeamUpdateComponent } from './team-update.component';

describe('Team Management Update Component', () => {
  let comp: TeamUpdateComponent;
  let fixture: ComponentFixture<TeamUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let teamFormService: TeamFormService;
  let teamService: TeamService;

  beforeEach(() => {
    // Настройка тестового модуля
    TestBed.configureTestingModule({
      imports: [TeamUpdateComponent], // Импортируем тестируемый компонент
      providers: [
        provideHttpClient(), // Мок HttpClient
        FormBuilder, // Сервис для работы с формами
        {
          provide: ActivatedRoute, // Мок ActivatedRoute
          useValue: {
            params: from([{}]), // Пустые параметры маршрута
          },
        },
      ],
    })
      .overrideTemplate(TeamUpdateComponent, '') // Отключаем шаблон компонента
      .compileComponents(); // Компилируем компонент

    // Создаем экземпляр компонента и необходимые сервисы
    fixture = TestBed.createComponent(TeamUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    teamFormService = TestBed.inject(TeamFormService);
    teamService = TestBed.inject(TeamService);
    comp = fixture.componentInstance;
  });

  // Тестирование метода ngOnInit
  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const team: ITeam = { id: 14592 }; // Тестовые данные команды

      // Мокируем данные маршрута
      activatedRoute.data = of({ team });
      // Вызываем инициализацию компонента
      comp.ngOnInit();

      // Проверяем что данные команды установлены
      expect(comp.team).toEqual(team);
    });
  });

  // Тестирование метода save
  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // Подготовка теста (GIVEN)
      const saveSubject = new Subject<HttpResponse<ITeam>>(); // Subject для эмуляции ответа
      const team = { id: 1226 }; // Существующая команда

      // Мокируем методы сервисов
      jest.spyOn(teamFormService, 'getTeam').mockReturnValue(team);
      jest.spyOn(teamService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');

      // Устанавливаем данные маршрута
      activatedRoute.data = of({ team });
      comp.ngOnInit();

      // Выполнение теста (WHEN)
      comp.save();
      expect(comp.isSaving).toEqual(true); // Проверяем флаг сохранения

      // Эмулируем успешный ответ сервера
      saveSubject.next(new HttpResponse({ body: team }));
      saveSubject.complete();

      // Проверки (THEN)
      expect(teamFormService.getTeam).toHaveBeenCalled(); // Проверяем вызов getTeam
      expect(comp.previousState).toHaveBeenCalled(); // Проверяем переход назад
      expect(teamService.update).toHaveBeenCalledWith(expect.objectContaining(team)); // Проверяем вызов update
      expect(comp.isSaving).toEqual(false); // Проверяем сброс флага сохранения
    });

    it('Should call create service on save for new entity', () => {
      // Подготовка теста (GIVEN)
      const saveSubject = new Subject<HttpResponse<ITeam>>();
      const team = { id: 1226 }; // Новая команда (но с id для теста)

      // Мокируем методы сервисов
      jest.spyOn(teamFormService, 'getTeam').mockReturnValue({ id: null }); // Новая команда
      jest.spyOn(teamService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');

      // Устанавливаем данные маршрута (новая команда)
      activatedRoute.data = of({ team: null });
      comp.ngOnInit();

      // Выполнение теста (WHEN)
      comp.save();
      expect(comp.isSaving).toEqual(true);

      // Эмулируем успешный ответ сервера
      saveSubject.next(new HttpResponse({ body: team }));
      saveSubject.complete();

      // Проверки (THEN)
      expect(teamFormService.getTeam).toHaveBeenCalled();
      expect(teamService.create).toHaveBeenCalled(); // Проверяем вызов create
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // Подготовка теста (GIVEN)
      const saveSubject = new Subject<HttpResponse<ITeam>>();
      const team = { id: 1226 };

      // Мокируем методы сервисов
      jest.spyOn(teamService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');

      // Устанавливаем данные маршрута
      activatedRoute.data = of({ team });
      comp.ngOnInit();

      // Выполнение теста (WHEN)
      comp.save();
      expect(comp.isSaving).toEqual(true);

      // Эмулируем ошибку
      saveSubject.error('This is an error!');

      // Проверки (THEN)
      expect(teamService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false); // Флаг должен быть сброшен
      expect(comp.previousState).not.toHaveBeenCalled(); // Переход не должен произойти
    });
  });
});
