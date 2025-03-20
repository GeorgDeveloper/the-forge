import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ProtectiveEquipmentResolve from './route/protective-equipment-routing-resolve.service';

const protectiveEquipmentRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/protective-equipment.component').then(m => m.ProtectiveEquipmentComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/protective-equipment-detail.component').then(m => m.ProtectiveEquipmentDetailComponent),
    resolve: {
      protectiveEquipment: ProtectiveEquipmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/protective-equipment-update.component').then(m => m.ProtectiveEquipmentUpdateComponent),
    resolve: {
      protectiveEquipment: ProtectiveEquipmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/protective-equipment-update.component').then(m => m.ProtectiveEquipmentUpdateComponent),
    resolve: {
      protectiveEquipment: ProtectiveEquipmentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default protectiveEquipmentRoute;
