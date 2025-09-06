// Импорт типа Routes из Angular Router для типизации конфигурации маршрутов
import { Routes } from '@angular/router';

// Определение константы routes с типом Routes (массив объектов маршрутов)
const routes: Routes = [
  // Маршрут для управления ролями/правами доступа
  {
    path: 'authority', // URL сегмент
    data: { pageTitle: 'theForgeApp.adminAuthority.home.title' }, // Локализованный заголовок страницы
    loadChildren: () => import('./admin/authority/authority.routes'), // Ленивая загрузка дочерних маршрутов
  },

  // Маршрут для работы с командами
  {
    path: 'team',
    data: { pageTitle: 'theForgeApp.team.home.title' },
    loadChildren: () => import('./team/team.routes'),
  },

  // Маршрут для управления сотрудниками
  {
    path: 'employee',
    data: { pageTitle: 'theForgeApp.employee.home.title' },
    loadChildren: () => import('./employee/employee.routes'),
  },

  // Маршрут для тренингов
  {
    path: 'training',
    data: { pageTitle: 'theForgeApp.training.home.title' },
    loadChildren: () => import('./training/training.routes'),
  },

  // Маршрут для задач
  {
    path: 'task',
    data: { pageTitle: 'theForgeApp.task.home.title' },
    loadChildren: () => import('./task/task.routes'),
  },

  // Маршрут для организаций
  {
    path: 'organization',
    data: { pageTitle: 'theForgeApp.organization.home.title' },
    loadChildren: () => import('./organization/organization.routes'),
  },

  // Маршрут для профессий
  {
    path: 'profession',
    data: { pageTitle: 'theForgeApp.profession.home.title' },
    loadChildren: () => import('./profession/profession.routes'),
  },

  // Маршрут для средств индивидуальной защиты
  {
    path: 'protective-equipment',
    data: { pageTitle: 'theForgeApp.protectiveEquipment.home.title' },
    loadChildren: () => import('./protective-equipment/protective-equipment.routes'),
  },

  // Маршрут для дополнительного обучения
  {
    path: 'additional-training',
    data: { pageTitle: 'theForgeApp.additionalTraining.home.title' },
    loadChildren: () => import('./additional-training/additional-training.routes'),
  },

  // Маршрут для инструктажа по технике безопасности
  {
    path: 'safety-instruction',
    data: { pageTitle: 'theForgeApp.safetyInstruction.home.title' },
    loadChildren: () => import('./safety-instruction/safety-instruction.routes'),
  },

  // Маршрут для должностей
  {
    path: 'position',
    data: { pageTitle: 'theForgeApp.position.home.title' },
    loadChildren: () => import('./position/position.routes'),
  },

  // Маршрут для календаря
  {
    path: 'calendar',
    data: { pageTitle: 'Календарь событий' },
    loadChildren: () => import('./calendar/calendar.routes'),
  },

  // Маршрут для должностных инструкций
  {
    path: 'job-description',
    data: { pageTitle: 'theForgeApp.jobDescription.home.title' },
    loadChildren: () => import('./job-description/job-description.routes'),
  },

  /*
   * Специальный комментарий для JHipster - будет автоматически заменен
   * на сгенерированные маршруты при создании новых сущностей
   * jhipster-needle-add-entity-route - JHipster will add entity modules routes here
   */
];

// Экспорт конфигурации маршрутов по умолчанию
export default routes;
