import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAdditionalTraining } from '../additional-training.model';
import { AdditionalTrainingService } from '../service/additional-training.service';

const additionalTrainingResolve = (route: ActivatedRouteSnapshot): Observable<null | IAdditionalTraining> => {
  const id = route.params.id;
  if (id) {
    return inject(AdditionalTrainingService)
      .find(id)
      .pipe(
        mergeMap((additionalTraining: HttpResponse<IAdditionalTraining>) => {
          if (additionalTraining.body) {
            return of(additionalTraining.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default additionalTrainingResolve;
