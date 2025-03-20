import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ProfessionResolve from './route/profession-routing-resolve.service';

const professionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/profession.component').then(m => m.ProfessionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/profession-detail.component').then(m => m.ProfessionDetailComponent),
    resolve: {
      profession: ProfessionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/profession-update.component').then(m => m.ProfessionUpdateComponent),
    resolve: {
      profession: ProfessionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/profession-update.component').then(m => m.ProfessionUpdateComponent),
    resolve: {
      profession: ProfessionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default professionRoute;
