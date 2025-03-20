import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISafetyInstruction } from '../safety-instruction.model';
import { SafetyInstructionService } from '../service/safety-instruction.service';

const safetyInstructionResolve = (route: ActivatedRouteSnapshot): Observable<null | ISafetyInstruction> => {
  const id = route.params.id;
  if (id) {
    return inject(SafetyInstructionService)
      .find(id)
      .pipe(
        mergeMap((safetyInstruction: HttpResponse<ISafetyInstruction>) => {
          if (safetyInstruction.body) {
            return of(safetyInstruction.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default safetyInstructionResolve;
