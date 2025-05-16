import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmployee } from '../employee.model';
import { EmployeeService } from '../service/employee.service';

const employeeResolve = (route: ActivatedRouteSnapshot): Observable<null | IEmployee> => {
  const id = route.params['id'];

  if (id) {
    //     return inject(EmployeeService)
    //       .find(id)
    //       .pipe(
    //         mergeMap((employee: HttpResponse<IEmployee>) => {
    //           if (employee.body) {
    //             return of(employee.body);
    //           }
    //           inject(Router).navigate(['404']);
    //           return EMPTY;
    //         }),
    //       );
    // Если ID присутствует в маршруте
    if (id) {
      // Используем inject для получения сервисов
      const employeeService = inject(EmployeeService);
      const router = inject(Router);

      // Запрашиваем данные команды через сервис
      return employeeService.find(id).pipe(
        // Используем mergeMap для обработки ответа
        mergeMap((employee: HttpResponse<IEmployee>) => {
          // Если сервер вернул данные команды
          if (employee.body) {
            // Возвращаем данные команды
            return of(employee.body);
          }

          // Если не найдена (body === null)
          // Перенаправляем на страницу 404
          router.navigate(['404']);
          // Возвращаем EMPTY (завершенный Observable без значений)
          return EMPTY;
        }),
      );
    }
  }
  return of(null);
};

export default employeeResolve;
