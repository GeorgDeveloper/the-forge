import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TaskResolve from './route/task-routing-resolve.service';

// Определение маршрутов для компонентов задач
const taskRoute: Routes = [
  {
    // Маршрут для отображения списка задач
    path: '',
    loadComponent: () => import('./list/task.component').then(m => m.TaskComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    // Маршрут для отображения деталей задачи
    path: ':id/view',
    loadComponent: () => import('./detail/task-detail.component').then(m => m.TaskDetailComponent),
    resolve: {
      task: TaskResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    // Маршрут для создания новой задачи
    path: 'new',
    loadComponent: () => import('./update/task-update.component').then(m => m.TaskUpdateComponent),
    resolve: {
      task: TaskResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    // Маршрут для редактирования существующей задачи
    path: ':id/edit',
    loadComponent: () => import('./update/task-update.component').then(m => m.TaskUpdateComponent),
    resolve: {
      task: TaskResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default taskRoute;
