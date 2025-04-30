import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITeam, NewTeam } from '../team.model';

// Тип для частичного обновления команды (обязательно должен содержать id)
export type PartialUpdateTeam = Partial<ITeam> & Pick<ITeam, 'id'>;

// Типы для HTTP-ответов
export type EntityResponseType = HttpResponse<ITeam>;
export type EntityArrayResponseType = HttpResponse<ITeam[]>;

@Injectable({ providedIn: 'root' })
export class TeamService {
  // Внедрение зависимостей через inject()
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  // Базовый URL для API команд
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/teams');

  /**
   * Создание новой команды
   * @param team - данные новой команды (NewTeam)
   * @returns Observable с ответом сервера
   */
  create(team: NewTeam): Observable<EntityResponseType> {
    return this.http.post<ITeam>(this.resourceUrl, team, { observe: 'response' });
  }

  /**
   * Полное обновление команды
   * @param team - обновленные данные команды (ITeam)
   * @returns Observable с ответом сервера
   */
  update(team: ITeam): Observable<EntityResponseType> {
    return this.http.put<ITeam>(`${this.resourceUrl}/${this.getTeamIdentifier(team)}`, team, { observe: 'response' });
  }

  /**
   * Частичное обновление команды
   * @param team - данные для частичного обновления (PartialUpdateTeam)
   * @returns Observable с ответом сервера
   */
  partialUpdate(team: PartialUpdateTeam): Observable<EntityResponseType> {
    return this.http.patch<ITeam>(`${this.resourceUrl}/${this.getTeamIdentifier(team)}`, team, { observe: 'response' });
  }

  /**
   * Поиск команды по ID
   * @param id - идентификатор команды
   * @returns Observable с ответом сервера
   */
  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITeam>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  /**
   * Поиск команды с сотрудниками по ID
   * @param id - идентификатор команды
   * @returns Observable с ответом сервера, содержащим команду и ее сотрудников
   */
  findWithEmployeesAndUsers(id: number): Observable<EntityResponseType> {
    return this.http.get<ITeam>(`${this.resourceUrl}/${id}/with-employees`, { observe: 'response' });
  }

  /**
   * Получение всех команд для конкретного пользователя
   * @param id - идентификатор пользователя
   * @param req - дополнительные параметры запроса (опционально)
   * @returns Observable с массивом команд
   */
  getAllTeamsByIdUser(id: number, req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITeam[]>(`${this.resourceUrl}/all/${id}`, {
      params: options,
      observe: 'response',
    });
  }

  /**
   * Запрос списка команд с возможностью пагинации/фильтрации
   * @param req - параметры запроса (опционально)
   * @returns Observable с ответом сервера
   */
  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITeam[]>(this.resourceUrl, {
      params: options,
      observe: 'response',
    });
  }

  /**
   * Удаление команды по ID
   * @param id - идентификатор команды
   * @returns Observable с ответом сервера
   */
  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  /**
   * Получение идентификатора команды
   * @param team - объект команды или DTO
   * @returns number - идентификатор команды
   */
  getTeamIdentifier(team: Pick<ITeam, 'id'>): number {
    return team.id;
  }

  /**
   * Сравнение двух команд по ID
   * @param o1 - первая команда или null
   * @param o2 - вторая команда или null
   * @returns boolean - true если ID совпадают или оба null
   */
  compareTeam(o1: Pick<ITeam, 'id'> | null, o2: Pick<ITeam, 'id'> | null): boolean {
    return o1 && o2 ? this.getTeamIdentifier(o1) === this.getTeamIdentifier(o2) : o1 === o2;
  }

  /**
   * Добавление команд в коллекцию, если их там еще нет
   * @param teamCollection - исходная коллекция команд
   * @param teamsToCheck - команды для проверки и добавления
   * @returns новая коллекция с уникальными командами
   */
  addTeamToCollectionIfMissing<Type extends Pick<ITeam, 'id'>>(
    teamCollection: Type[],
    ...teamsToCheck: (Type | null | undefined)[]
  ): Type[] {
    // Фильтрация null/undefined значений
    const teams: Type[] = teamsToCheck.filter(isPresent);

    if (teams.length > 0) {
      const teamCollectionIdentifiers = teamCollection.map(teamItem => this.getTeamIdentifier(teamItem));

      // Фильтрация команд, которых еще нет в коллекции
      const teamsToAdd = teams.filter(teamItem => {
        const teamIdentifier = this.getTeamIdentifier(teamItem);
        if (teamCollectionIdentifiers.includes(teamIdentifier)) {
          return false;
        }
        teamCollectionIdentifiers.push(teamIdentifier);
        return true;
      });

      return [...teamsToAdd, ...teamCollection];
    }
    return teamCollection;
  }
}
