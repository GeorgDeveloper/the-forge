import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITeam } from '../team.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../team.test-samples';
import { TeamService } from './team.service';

// Тестовые данные
const requireRestSample: ITeam = {
  ...sampleWithRequiredData, // Используем sample с обязательными полями
};

describe('Team Service', () => {
  let service: TeamService;
  let httpMock: HttpTestingController; // Мок HTTP-клиента
  let expectedResult: ITeam | ITeam[] | boolean | null; // Переменная для проверки результатов

  beforeEach(() => {
    // Настройка тестового модуля
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(), // Реальный HttpClient
        provideHttpClientTesting(), // Мок для HttpClient
      ],
    });

    // Инициализация переменных
    expectedResult = null;
    service = TestBed.inject(TeamService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  // Группа тестов методов сервиса
  describe('Service methods', () => {
    // Тест метода find()
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      // Вызываем метод и подписываемся на результат
      service.find(123).subscribe(resp => (expectedResult = resp.body));

      // Ожидаем HTTP-запрос и мокируем ответ
      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);

      // Проверяем результат
      expect(expectedResult).toMatchObject(expected);
    });

    // Тест метода create()
    it('should create a Team', () => {
      const team = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(team).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    // Тест метода update()
    it('should update a Team', () => {
      const team = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(team).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    // Тест метода partialUpdate()
    it('should partial update a Team', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    // Тест метода query()
    it('should return a list of Team', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify(); // Проверяем что нет незавершенных запросов
      expect(expectedResult).toMatchObject([expected]);
    });

    // Тест метода delete()
    it('should delete a Team', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 }); // Мокируем успешный ответ
      expect(expectedResult).toBe(expected);
    });

    // Группа тестов для addTeamToCollectionIfMissing()
    describe('addTeamToCollectionIfMissing', () => {
      it('should add a Team to an empty array', () => {
        const team: ITeam = sampleWithRequiredData;
        expectedResult = service.addTeamToCollectionIfMissing([], team);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(team);
      });

      it('should not add a Team to an array that contains it', () => {
        const team: ITeam = sampleWithRequiredData;
        const teamCollection: ITeam[] = [{ ...team }, sampleWithPartialData];
        expectedResult = service.addTeamToCollectionIfMissing(teamCollection, team);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Team to an array that doesn't contain it", () => {
        const team: ITeam = sampleWithRequiredData;
        const teamCollection: ITeam[] = [sampleWithPartialData];
        expectedResult = service.addTeamToCollectionIfMissing(teamCollection, team);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(team);
      });

      it('should add only unique Team to an array', () => {
        const teamArray: ITeam[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const teamCollection: ITeam[] = [sampleWithRequiredData];
        expectedResult = service.addTeamToCollectionIfMissing(teamCollection, ...teamArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const team: ITeam = sampleWithRequiredData;
        const team2: ITeam = sampleWithPartialData;
        expectedResult = service.addTeamToCollectionIfMissing([], team, team2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(team);
        expect(expectedResult).toContain(team2);
      });

      it('should accept null and undefined values', () => {
        const team: ITeam = sampleWithRequiredData;
        expectedResult = service.addTeamToCollectionIfMissing([], null, team, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(team);
      });

      it('should return initial array if no Team is added', () => {
        const teamCollection: ITeam[] = [sampleWithRequiredData];
        expectedResult = service.addTeamToCollectionIfMissing(teamCollection, undefined, null);
        expect(expectedResult).toEqual(teamCollection);
      });
    });

    // Группа тестов для compareTeam()
    describe('compareTeam', () => {
      it('Should return true if both entities are null', () => {
        expect(service.compareTeam(null, null)).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 1226 };
        expect(service.compareTeam(entity1, null)).toEqual(false);
        expect(service.compareTeam(null, entity1)).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 1226 };
        const entity2 = { id: 14592 };
        expect(service.compareTeam(entity1, entity2)).toEqual(false);
        expect(service.compareTeam(entity2, entity1)).toEqual(false);
      });

      it('Should return true if primaryKey matches', () => {
        const entity1 = { id: 1226 };
        const entity2 = { id: 1226 };
        expect(service.compareTeam(entity1, entity2)).toEqual(true);
        expect(service.compareTeam(entity2, entity1)).toEqual(true);
      });
    });
  });

  // Проверка, что нет незавершенных HTTP-запросов после каждого теста
  afterEach(() => {
    httpMock.verify();
  });
});
