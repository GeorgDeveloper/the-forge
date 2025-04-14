import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITeam } from '../team.model';
import { TeamService } from '../service/team.service';

/**
 * Резолвер для получения данных команды перед загрузкой маршрута
 * @param route - Текущий активированный маршрут
 * @returns Observable с данными команды или null, либо перенаправляет на 404
 */
const teamResolve = (route: ActivatedRouteSnapshot): Observable<null | ITeam> => {
  // Получаем ID команды из параметров маршрута
  const id = route.params['id'];

  // Если ID присутствует в маршруте
  if (id) {
    // Используем inject для получения сервисов
    const teamService = inject(TeamService);
    const router = inject(Router);

    // Запрашиваем данные команды через сервис
    return teamService.find(id).pipe(
      // Используем mergeMap для обработки ответа
      mergeMap((team: HttpResponse<ITeam>) => {
        // Если сервер вернул данные команды
        if (team.body) {
          // Возвращаем данные команды
          return of(team.body);
        }

        // Если команда не найдена (body === null)
        // Перенаправляем на страницу 404
        router.navigate(['404']);
        // Возвращаем EMPTY (завершенный Observable без значений)
        return EMPTY;
      }),
    );
  }

  // Если ID не был предоставлен в маршруте
  // возвращаем null как Observable
  return of(null);
};

// Экспортируем резолвер по умолчанию
export default teamResolve;
