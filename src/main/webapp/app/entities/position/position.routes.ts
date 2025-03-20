import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PositionResolve from './route/position-routing-resolve.service';

const positionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/position.component').then(m => m.PositionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/position-detail.component').then(m => m.PositionDetailComponent),
    resolve: {
      position: PositionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/position-update.component').then(m => m.PositionUpdateComponent),
    resolve: {
      position: PositionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/position-update.component').then(m => m.PositionUpdateComponent),
    resolve: {
      position: PositionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default positionRoute;
