// Импорт необходимых модулей и сервисов Angular
import { ApplicationConfig, LOCALE_ID, importProvidersFrom, inject } from '@angular/core';
import { BrowserModule, Title } from '@angular/platform-browser';
import {
  NavigationError,
  Router,
  RouterFeatures,
  TitleStrategy,
  provideRouter,
  withComponentInputBinding,
  withDebugTracing,
  withNavigationErrorHandler,
} from '@angular/router';
import { ServiceWorkerModule } from '@angular/service-worker';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

// Импорт модулей ng-bootstrap
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

// Импорт конфигурации приложения
import './config/dayjs'; // Конфигурация библиотеки dayjs для работы с датами
import { TranslationModule } from 'app/shared/language/translation.module'; // Модуль переводов
import { environment } from 'environments/environment'; // Конфигурация среды
import { httpInterceptorProviders } from './core/interceptor'; // HTTP-интерсепторы
import routes from './app.routes'; // Основные маршруты приложения
import { NgbDateDayjsAdapter } from './config/datepicker-adapter'; // Адаптер для работы с датами
import { AppPageTitleStrategy } from './app-page-title-strategy'; // Кастомная стратегия заголовков

// Конфигурация дополнительных возможностей маршрутизатора
const routerFeatures: RouterFeatures[] = [
  // Включение привязки входных параметров компонентов из маршрута
  withComponentInputBinding(),

  // Обработчик ошибок навигации
  withNavigationErrorHandler((e: NavigationError) => {
    const router = inject(Router);
    // Перенаправление в зависимости от типа ошибки
    if (e.error.status === 403) {
      router.navigate(['/accessdenied']); // Доступ запрещен
    } else if (e.error.status === 404) {
      router.navigate(['/404']); // Страница не найдена
    } else if (e.error.status === 401) {
      router.navigate(['/login']); // Неавторизованный доступ
    } else {
      router.navigate(['/error']); // Общая ошибка
    }
  }),
];

// Включение отладочной информации маршрутизатора в development-режиме
if (environment.DEBUG_INFO_ENABLED) {
  routerFeatures.push(withDebugTracing());
}

// Основная конфигурация приложения
export const appConfig: ApplicationConfig = {
  providers: [
    // Настройка маршрутизатора с основными маршрутами и дополнительными функциями
    provideRouter(routes, ...routerFeatures),

    // Импорт стандартных Angular-модулей через importProvidersFrom
    importProvidersFrom(BrowserModule),

    // Регистрация Service Worker (PWA) - отключено по умолчанию
    importProvidersFrom(ServiceWorkerModule.register('ngsw-worker.js', { enabled: false })),

    // Модуль переводов
    importProvidersFrom(TranslationModule),

    // Настройка HTTP-клиента с интерсепторами
    provideHttpClient(withInterceptorsFromDi()),

    // Сервис для работы с заголовками страниц
    Title,

    // Установка локали по умолчанию (русский язык)
    { provide: LOCALE_ID, useValue: 'ru' },

    // Адаптер для работы с датами в ng-bootstrap
    { provide: NgbDateAdapter, useClass: NgbDateDayjsAdapter },

    // Провайдеры HTTP-интерсепторов
    httpInterceptorProviders,

    // Кастомная стратегия для заголовков страниц
    { provide: TitleStrategy, useClass: AppPageTitleStrategy },

    // Место для автоматически генерируемых JHipster-модулей
  ],
};
