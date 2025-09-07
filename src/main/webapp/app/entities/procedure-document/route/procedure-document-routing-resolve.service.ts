import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProcedureDocument } from '../procedure-document.model';
import { ProcedureDocumentService } from '../service/procedure-document.service';

@Injectable({ providedIn: 'root' })
export class ProcedureDocumentResolve implements Resolve<IProcedureDocument | null> {
  constructor(
    protected service: ProcedureDocumentService,
    protected router: Router,
  ) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProcedureDocument | null> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((procedureDocument: any) => {
          if (procedureDocument.body) {
            return of(procedureDocument.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        }),
      );
    }
    return of(null);
  }
}

export default ProcedureDocumentResolve;
