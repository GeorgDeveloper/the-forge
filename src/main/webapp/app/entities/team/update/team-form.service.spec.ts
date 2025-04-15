import { TestBed } from '@angular/core/testing';
import { sampleWithNewData, sampleWithRequiredData } from '../team.test-samples';
import { TeamFormService } from './team-form.service';

describe('Team Form Service', () => {
  let service: TeamFormService;

  beforeEach(() => {
    // Настройка тестового модуля Angular
    TestBed.configureTestingModule({});
    // Получение экземпляра сервиса для тестирования
    service = TestBed.inject(TeamFormService);
  });

  // Группа тестов для методов сервиса
  describe('Service methods', () => {
    // Тесты для метода createTeamFormGroup
    describe('createTeamFormGroup', () => {
      it('should create a new form with FormControl', () => {
        // Создание формы без начальных данных
        const formGroup = service.createTeamFormGroup();

        // Проверка что форма содержит ожидаемые контролы
        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object), // Проверяем наличие контрола id
            teamName: expect.any(Object), // Проверяем наличие контрола teamName
          }),
        );
      });

      it('passing ITeam should create a new form with FormGroup', () => {
        // Создание формы с тестовыми данными
        const formGroup = service.createTeamFormGroup(sampleWithRequiredData);

        // Проверка что форма содержит ожидаемые контролы
        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            teamName: expect.any(Object),
          }),
        );
      });
    });

    // Тесты для метода getTeam
    describe('getTeam', () => {
      it('should return NewTeam for default Team initial value', () => {
        // Создание формы с новыми тестовыми данными
        const formGroup = service.createTeamFormGroup(sampleWithNewData);

        // Получение данных из формы
        const team = service.getTeam(formGroup) as any;

        // Проверка что полученные данные соответствуют ожидаемым
        expect(team).toMatchObject(sampleWithNewData);
      });

      it('should return NewTeam for empty Team initial value', () => {
        // Создание пустой формы
        const formGroup = service.createTeamFormGroup();

        // Получение данных из формы
        const team = service.getTeam(formGroup) as any;

        // Проверка что получен пустой объект
        expect(team).toMatchObject({});
      });

      it('should return ITeam', () => {
        // Создание формы с обязательными тестовыми данными
        const formGroup = service.createTeamFormGroup(sampleWithRequiredData);

        // Получение данных из формы
        const team = service.getTeam(formGroup) as any;

        // Проверка что полученные данные соответствуют ожидаемым
        expect(team).toMatchObject(sampleWithRequiredData);
      });
    });

    // Тесты для метода resetForm
    describe('resetForm', () => {
      it('passing ITeam should not enable id FormControl', () => {
        // Создание пустой формы
        const formGroup = service.createTeamFormGroup();
        // Проверка что контрол id изначально disabled
        expect(formGroup.controls.id.disabled).toBe(true);

        // Сброс формы с тестовыми данными
        service.resetForm(formGroup, sampleWithRequiredData);

        // Проверка что контрол id остался disabled
        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTeam should disable id FormControl', () => {
        // Создание формы с тестовыми данными
        const formGroup = service.createTeamFormGroup(sampleWithRequiredData);
        // Проверка что контрол id изначально disabled
        expect(formGroup.controls.id.disabled).toBe(true);

        // Сброс формы с пустым id
        service.resetForm(formGroup, { id: null });

        // Проверка что контрол id остался disabled
        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
