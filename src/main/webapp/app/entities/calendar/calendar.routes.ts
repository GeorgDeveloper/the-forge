import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CalendarResolve from './route/calendar-routing-resolve.service';

const calendarRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./calendar.component').then(m => m.CalendarComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default calendarRoute;
