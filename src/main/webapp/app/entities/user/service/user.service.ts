// Импорт необходимых Angular декораторов и сервисов
import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

// Импорт вспомогательных утилит
import { isPresent } from 'app/core/util/operators'; // Проверка на существование значения
import { ApplicationConfigService } from 'app/core/config/application-config.service'; // Сервис конфигурации
import { createRequestOption } from 'app/core/request/request-util'; // Утилита для создания параметров запроса
import { IUser } from '../user.model'; // Интерфейс модели пользователя

// Типы для HTTP-ответов
export type EntityResponseType = HttpResponse<IUser>; // Ответ с одним пользователем
export type EntityArrayResponseType = HttpResponse<IUser[]>; // Ответ с массивом пользователей

// Декоратор Injectable с указанием, что сервис доступен на уровне root
@Injectable({ providedIn: 'root' })
export class UserService {
  // Внедрение зависимостей через inject()
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  // Базовый URL для API пользователей
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/users');

  /**
   * Найти пользователя по ID
   * @param id - идентификатор пользователя
   * @returns Observable с HttpResponse, содержащий IUser
   */
  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  /**
   * Запросить список пользователей с возможностью пагинации/фильтрации
   * @param req - параметры запроса (опционально)
   * @returns Observable с HttpResponse, содержащий массив IUser
   */
  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req); // Создание параметров запроса
    return this.http.get<IUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  /**
   * Получить идентификатор пользователя
   * @param user - объект пользователя с полем id
   * @returns числовой идентификатор
   */
  getUserIdentifier(user: Pick<IUser, 'id'>): number {
    return user.id;
  }

  /**
   * Сравнить двух пользователей по ID
   * @param o1 - первый пользователь или null
   * @param o2 - второй пользователь или null
   * @returns true, если ID совпадают или оба null/undefined
   */
  compareUser(o1: Pick<IUser, 'id'> | null, o2: Pick<IUser, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserIdentifier(o1) === this.getUserIdentifier(o2) : o1 === o2;
  }

  /**
   * Добавить пользователей в коллекцию, если их там нет
   * @param userCollection - исходная коллекция пользователей
   * @param usersToCheck - пользователи для проверки и добавления
   * @returns новая коллекция с уникальными пользователями
   */
  addUserToCollectionIfMissing<Type extends Pick<IUser, 'id'>>(
    userCollection: Type[],
    ...usersToCheck: (Type | null | undefined)[]
  ): Type[] {
    // Фильтрация null/undefined значений
    const users: Type[] = usersToCheck.filter(isPresent);

    if (users.length > 0) {
      // Получаем идентификаторы существующих пользователей
      const userCollectionIdentifiers = userCollection.map(userItem => this.getUserIdentifier(userItem));

      // Фильтруем пользователей для добавления
      const usersToAdd = users.filter(userItem => {
        const userIdentifier = this.getUserIdentifier(userItem);
        if (userCollectionIdentifiers.includes(userIdentifier)) {
          return false; // Пропускаем существующих
        }
        userCollectionIdentifiers.push(userIdentifier); // Запоминаем добавленный ID
        return true;
      });

      return [...usersToAdd, ...userCollection]; // Возвращаем новую объединенную коллекцию
    }
    return userCollection; // Возвращаем оригинал, если нечего добавлять
  }
}
