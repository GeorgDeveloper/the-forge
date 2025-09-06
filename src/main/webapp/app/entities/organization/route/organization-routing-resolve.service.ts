import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { IOrganization } from '../organization.model';
import { OrganizationService } from '../service/organization.service';

export default function OrganizationResolve(route: ActivatedRouteSnapshot) {
  const id = route.params['id'];
  if (id) {
    return inject(OrganizationService)
      .find(+id)
      .pipe(
        mergeMap(res => {
          if (res.body) return of(res.body as IOrganization);
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of({ id: null } as any);
}
