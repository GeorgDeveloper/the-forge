// Импорт необходимых модулей и сервисов
import { Routes } from '@angular/router'; // Базовый тип для конфигурации маршрутов Angular
import { Authority } from 'app/config/authority.constants'; // Роли/права доступа приложения
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service'; // Сервис проверки прав доступа
import { errorRoute } from './layouts/error/error.route'; // Маршруты для обработки ошибок

// Определение основных маршрутов приложения
const routes: Routes = [
  // Главная страница
  {
    path: '', // Корневой URL
    loadComponent: () => import('./home/home.component'), // Ленивая загрузка компонента
    title: 'home.title', // Ключ перевода для заголовка страницы
  },

  // Навигационное меню (загружается в именованный outlet)
  {
    path: '', // Тот же URL, но для другого outlet
    loadComponent: () => import('./layouts/navbar/navbar.component'), // Компонент навигации
    outlet: 'navbar', // Именованный outlet для рендеринга
  },

  // Админ-панель (только для пользователей с ролью ADMIN)
  {
    path: 'admin', // URL для админки
    data: {
      authorities: [Authority.ADMIN], // Требуемая роль
    },
    canActivate: [UserRouteAccessService], // Защита маршрута
    loadChildren: () => import('./admin/admin.routes'), // Ленивая загрузка дочерних маршрутов
  },

  // Аккаунт (регистрация, профиль и т.д.)
  {
    path: 'account',
    loadChildren: () => import('./account/account.route'), // Ленивая загрузка маршрутов аккаунта
  },

  // Страница входа
  {
    path: 'login',
    loadComponent: () => import('./login/login.component'), // Компонент входа
    title: 'login.title', // Ключ перевода заголовка
  },

  // Маршруты для сущностей (динамическая загрузка)
  {
    path: '', // Базовый путь
    loadChildren: () => import(`./entities/entity.routes`), // Динамические маршруты сущностей
  },

  // Добавление маршрутов для обработки ошибок (404, 403 и т.д.)
  ...errorRoute, // Оператор spread "разворачивает" массив errorRoute
];

// Экспорт конфигурации маршрутов по умолчанию
export default routes;
