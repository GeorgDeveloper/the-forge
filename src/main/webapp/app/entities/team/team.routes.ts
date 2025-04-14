import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service'; // Сервис для контроля доступа к маршрутам на основе авторизации пользователя
import { ASC } from 'app/config/navigation.constants'; // Константа для определения порядка сортировки (по возрастанию)
import TeamResolve from './route/team-routing-resolve.service'; // Сервис для предварительной загрузки данных команды перед отображением компонента

// Определение маршрутов для компонента "Команда"
const teamRoute: Routes = [
  {
    path: '', // Путь по умолчанию (корневой) для компонента "Команда"
    loadComponent: () => import('./list/team.component').then(m => m.TeamComponent), // Ленивая загрузка компонента списка команд
    data: {
      defaultSort: `id,${ASC}`, // Параметры данных, передаваемые в компонент, в данном случае - сортировка по id по возрастанию
    },
    canActivate: [UserRouteAccessService], // Защита маршрута, доступ только для авторизованных пользователей
  },
  {
    path: ':id/view', // Путь для просмотра деталей команды с определенным id
    loadComponent: () => import('./detail/team-detail.component').then(m => m.TeamDetailComponent), // Ленивая загрузка компонента деталей команды
    resolve: {
      team: TeamResolve, // Предварительная загрузка данных команды с помощью сервиса TeamResolve
    },
    canActivate: [UserRouteAccessService], // Защита маршрута, доступ только для авторизованных пользователей
  },
  {
    path: 'new', // Путь для создания новой команды
    loadComponent: () => import('./update/team-update.component').then(m => m.TeamUpdateComponent), // Ленивая загрузка компонента обновления/создания команды
    resolve: {
      team: TeamResolve, // Предварительная загрузка данных команды с помощью сервиса TeamResolve (может использоваться для инициализации формы)
    },
    canActivate: [UserRouteAccessService], // Защита маршрута, доступ только для авторизованных пользователей
  },
  {
    path: ':id/edit', // Путь для редактирования команды с определенным id
    loadComponent: () => import('./update/team-update.component').then(m => m.TeamUpdateComponent), // Ленивая загрузка компонента обновления/создания команды
    resolve: {
      team: TeamResolve, // Предварительная загрузка данных команды с помощью сервиса TeamResolve
    },
    canActivate: [UserRouteAccessService], // Защита маршрута, доступ только для авторизованных пользователей
  },
];

export default teamRoute; // Экспорт массива маршрутов для использования в Angular Router
