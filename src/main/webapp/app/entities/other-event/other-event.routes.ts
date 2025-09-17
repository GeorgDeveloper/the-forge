import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

const otherEventRoute: Routes = [
  {
    path: ':id/view',
    loadComponent: () => import('./view/other-event-detail.component').then(m => m.OtherEventDetailComponent),
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/other-event-update.component').then(m => m.OtherEventUpdateComponent),
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/other-event-update.component').then(m => m.OtherEventUpdateComponent),
    canActivate: [UserRouteAccessService],
  },
];

export default otherEventRoute;
