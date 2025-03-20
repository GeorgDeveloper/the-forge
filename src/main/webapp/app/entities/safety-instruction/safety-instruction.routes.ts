import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SafetyInstructionResolve from './route/safety-instruction-routing-resolve.service';

const safetyInstructionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/safety-instruction.component').then(m => m.SafetyInstructionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/safety-instruction-detail.component').then(m => m.SafetyInstructionDetailComponent),
    resolve: {
      safetyInstruction: SafetyInstructionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/safety-instruction-update.component').then(m => m.SafetyInstructionUpdateComponent),
    resolve: {
      safetyInstruction: SafetyInstructionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/safety-instruction-update.component').then(m => m.SafetyInstructionUpdateComponent),
    resolve: {
      safetyInstruction: SafetyInstructionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default safetyInstructionRoute;
