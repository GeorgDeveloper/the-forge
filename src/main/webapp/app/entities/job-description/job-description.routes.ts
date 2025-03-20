import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import JobDescriptionResolve from './route/job-description-routing-resolve.service';

const jobDescriptionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/job-description.component').then(m => m.JobDescriptionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/job-description-detail.component').then(m => m.JobDescriptionDetailComponent),
    resolve: {
      jobDescription: JobDescriptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/job-description-update.component').then(m => m.JobDescriptionUpdateComponent),
    resolve: {
      jobDescription: JobDescriptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/job-description-update.component').then(m => m.JobDescriptionUpdateComponent),
    resolve: {
      jobDescription: JobDescriptionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default jobDescriptionRoute;
