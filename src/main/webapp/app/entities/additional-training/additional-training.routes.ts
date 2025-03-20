import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AdditionalTrainingResolve from './route/additional-training-routing-resolve.service';

const additionalTrainingRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/additional-training.component').then(m => m.AdditionalTrainingComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/additional-training-detail.component').then(m => m.AdditionalTrainingDetailComponent),
    resolve: {
      additionalTraining: AdditionalTrainingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/additional-training-update.component').then(m => m.AdditionalTrainingUpdateComponent),
    resolve: {
      additionalTraining: AdditionalTrainingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/additional-training-update.component').then(m => m.AdditionalTrainingUpdateComponent),
    resolve: {
      additionalTraining: AdditionalTrainingResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default additionalTrainingRoute;
