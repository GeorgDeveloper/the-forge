// Импорт необходимых модулей для тестирования
import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

// Импорт моделей и тестовых данных
import { IUser } from '../user.model';
import { sampleWithFullData, sampleWithPartialData, sampleWithRequiredData } from '../user.test-samples';
import { UserService } from './user.service';

// Тестовый образец данных, соответствующий REST API
const requireRestSample: IUser = {
  ...sampleWithRequiredData, // Копируем все свойства из sampleWithRequiredData
};

describe('User Service', () => {
  let service: UserService;
  let httpMock: HttpTestingController; // Мок для HTTP-запросов
  let expectedResult: IUser | IUser[] | boolean | null; // Переменная для хранения ожидаемого результата

  beforeEach(() => {
    // Настройка тестового модуля
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(), // Предоставляем HttpClient
        provideHttpClientTesting(), // Предоставляем мок для HTTP тестирования
      ],
    });

    // Инициализация переменных перед каждым тестом
    expectedResult = null;
    service = TestBed.inject(UserService); // Получаем экземпляр сервиса
    httpMock = TestBed.inject(HttpTestingController); // Получаем HTTP мок
  });

  describe('Service methods', () => {
    // Тест для метода find()
    it('should find an element', () => {
      // Подготовка тестовых данных
      const returnedFromService = { ...requireRestSample }; // Данные, которые вернет мок
      const expected = { ...sampleWithRequiredData }; // Ожидаемый результат

      // Вызов тестируемого метода
      service.find(123).subscribe(resp => (expectedResult = resp.body));

      // Проверка HTTP-запроса
      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService); // Эмулируем успешный ответ сервера

      // Проверка результата
      expect(expectedResult).toMatchObject(expected);
    });

    // Тест для метода query()
    it('should return a list of User', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]); // Возвращаем массив пользователей
      httpMock.verify(); // Проверяем, что больше запросов не осталось
      expect(expectedResult).toMatchObject([expected]);
    });

    // Группа тестов для метода addUserToCollectionIfMissing()
    describe('addUserToCollectionIfMissing', () => {
      it('should add a User to an empty array', () => {
        const user: IUser = sampleWithRequiredData;
        expectedResult = service.addUserToCollectionIfMissing([], user);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(user);
      });

      it('should not add a User to an array that contains it', () => {
        const user: IUser = sampleWithRequiredData;
        const userCollection: IUser[] = [
          { ...user }, // Клонируем пользователя
          sampleWithPartialData,
        ];
        expectedResult = service.addUserToCollectionIfMissing(userCollection, user);
        expect(expectedResult).toHaveLength(2); // Длина не изменилась
      });

      it("should add a User to an array that doesn't contain it", () => {
        const user: IUser = sampleWithRequiredData;
        const userCollection: IUser[] = [sampleWithPartialData];
        expectedResult = service.addUserToCollectionIfMissing(userCollection, user);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(user);
      });

      it('should add only unique User to an array', () => {
        const userArray: IUser[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const userCollection: IUser[] = [sampleWithRequiredData];
        expectedResult = service.addUserToCollectionIfMissing(userCollection, ...userArray);
        expect(expectedResult).toHaveLength(3); // Добавлены только 2 уникальных пользователя
      });

      it('should accept varargs', () => {
        const user: IUser = sampleWithRequiredData;
        const user2: IUser = sampleWithPartialData;
        expectedResult = service.addUserToCollectionIfMissing([], user, user2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(user);
        expect(expectedResult).toContain(user2);
      });

      it('should accept null and undefined values', () => {
        const user: IUser = sampleWithRequiredData;
        expectedResult = service.addUserToCollectionIfMissing([], null, user, undefined);
        expect(expectedResult).toHaveLength(1); // null и undefined проигнорированы
        expect(expectedResult).toContain(user);
      });

      it('should return initial array if no User is added', () => {
        const userCollection: IUser[] = [sampleWithRequiredData];
        expectedResult = service.addUserToCollectionIfMissing(userCollection, undefined, null);
        expect(expectedResult).toEqual(userCollection); // Массив не изменился
      });
    });

    // Группа тестов для метода compareUser()
    describe('compareUser', () => {
      it('Should return true if both entities are null', () => {
        expect(service.compareUser(null, null)).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 3944 };
        expect(service.compareUser(entity1, null)).toEqual(false);
        expect(service.compareUser(null, entity1)).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 3944 };
        const entity2 = { id: 6275 };
        expect(service.compareUser(entity1, entity2)).toEqual(false);
      });

      it('Should return true if primaryKey matches', () => {
        const entity1 = { id: 3944 };
        const entity2 = { id: 3944 };
        expect(service.compareUser(entity1, entity2)).toEqual(true);
      });
    });
  });

  // Проверка после каждого теста, что нет незавершенных HTTP-запросов
  afterEach(() => {
    httpMock.verify();
  });
});
