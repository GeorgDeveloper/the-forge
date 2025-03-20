import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'theForgeApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'team',
    data: { pageTitle: 'theForgeApp.team.home.title' },
    loadChildren: () => import('./team/team.routes'),
  },
  {
    path: 'employee',
    data: { pageTitle: 'theForgeApp.employee.home.title' },
    loadChildren: () => import('./employee/employee.routes'),
  },
  {
    path: 'training',
    data: { pageTitle: 'theForgeApp.training.home.title' },
    loadChildren: () => import('./training/training.routes'),
  },
  {
    path: 'task',
    data: { pageTitle: 'theForgeApp.task.home.title' },
    loadChildren: () => import('./task/task.routes'),
  },
  {
    path: 'profession',
    data: { pageTitle: 'theForgeApp.profession.home.title' },
    loadChildren: () => import('./profession/profession.routes'),
  },
  {
    path: 'protective-equipment',
    data: { pageTitle: 'theForgeApp.protectiveEquipment.home.title' },
    loadChildren: () => import('./protective-equipment/protective-equipment.routes'),
  },
  {
    path: 'additional-training',
    data: { pageTitle: 'theForgeApp.additionalTraining.home.title' },
    loadChildren: () => import('./additional-training/additional-training.routes'),
  },
  {
    path: 'safety-instruction',
    data: { pageTitle: 'theForgeApp.safetyInstruction.home.title' },
    loadChildren: () => import('./safety-instruction/safety-instruction.routes'),
  },
  {
    path: 'position',
    data: { pageTitle: 'theForgeApp.position.home.title' },
    loadChildren: () => import('./position/position.routes'),
  },
  {
    path: 'job-description',
    data: { pageTitle: 'theForgeApp.jobDescription.home.title' },
    loadChildren: () => import('./job-description/job-description.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
