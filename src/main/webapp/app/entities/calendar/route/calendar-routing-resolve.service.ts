import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

const calendarResolve = (route: ActivatedRouteSnapshot): Observable<null> => {
  return of(null);
};

export default calendarResolve;
