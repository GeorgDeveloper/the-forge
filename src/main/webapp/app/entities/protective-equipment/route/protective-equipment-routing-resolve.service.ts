import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProtectiveEquipment } from '../protective-equipment.model';
import { ProtectiveEquipmentService } from '../service/protective-equipment.service';

const protectiveEquipmentResolve = (route: ActivatedRouteSnapshot): Observable<null | IProtectiveEquipment> => {
  const id = route.params.id;
  if (id) {
    return inject(ProtectiveEquipmentService)
      .find(id)
      .pipe(
        mergeMap((protectiveEquipment: HttpResponse<IProtectiveEquipment>) => {
          if (protectiveEquipment.body) {
            return of(protectiveEquipment.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default protectiveEquipmentResolve;
