import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ProcedureDocumentResolve from './route/procedure-document-routing-resolve.service';

const procedureDocumentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/procedure-document.component').then(m => m.ProcedureDocumentComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/procedure-document-detail.component').then(m => m.ProcedureDocumentDetailComponent),
    resolve: {
      procedureDocument: ProcedureDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/procedure-document-update.component').then(m => m.ProcedureDocumentUpdateComponent),
    resolve: {
      procedureDocument: ProcedureDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/procedure-document-update.component').then(m => m.ProcedureDocumentUpdateComponent),
    resolve: {
      procedureDocument: ProcedureDocumentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default procedureDocumentRoute;
