import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IJobDescription } from '../job-description.model';
import { JobDescriptionService } from '../service/job-description.service';

const jobDescriptionResolve = (route: ActivatedRouteSnapshot): Observable<null | IJobDescription> => {
  const id = route.params.id;
  if (id) {
    return inject(JobDescriptionService)
      .find(id)
      .pipe(
        mergeMap((jobDescription: HttpResponse<IJobDescription>) => {
          if (jobDescription.body) {
            return of(jobDescription.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default jobDescriptionResolve;
