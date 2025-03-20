import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TrainingResolve from './route/training-routing-resolve.service';

const trainingRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/training.component').then(m => m.TrainingComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/training-detail.component').then(m => m.TrainingDetailComponent),
    resolve: {
      training: TrainingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/training-update.component').then(m => m.TrainingUpdateComponent),
    resolve: {
      training: TrainingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/training-update.component').then(m => m.TrainingUpdateComponent),
    resolve: {
      training: TrainingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default trainingRoute;
