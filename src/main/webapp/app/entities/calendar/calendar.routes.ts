import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import EmployeeResolve from './route/calendar-routing-resolve.service';

const employeeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./calendar.component').then(m => m.CalendarComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default employeeRoute;
