import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

const meetingRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/meeting.component').then(m => m.MeetingComponent),
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./view/meeting-detail.component').then(m => m.MeetingDetailComponent),
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/meeting-update.component').then(m => m.MeetingUpdateComponent),
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/meeting-update.component').then(m => m.MeetingUpdateComponent),
    canActivate: [UserRouteAccessService],
  },
];

export default meetingRoute;
