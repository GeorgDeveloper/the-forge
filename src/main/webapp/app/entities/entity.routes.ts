// Импорт типа Routes из Angular Router для типизации конфигурации маршрутов
import { Routes } from '@angular/router';

import additionalTrainingRoute from 'app/entities/additional-training/additional-training.routes';
import adminAuthorityRoute from 'app/entities/admin/authority/authority.routes';
import employeeRoute from 'app/entities/employee/employee.routes';
import jobDescriptionRoute from 'app/entities/job-description/job-description.routes';
import organizationRoute from 'app/entities/organization/organization.routes';
import positionRoute from 'app/entities/position/position.routes';
import procedureDocumentRoute from 'app/entities/procedure-document/procedure-document.routes';
import professionRoute from 'app/entities/profession/profession.routes';
import protectiveEquipmentRoute from 'app/entities/protective-equipment/protective-equipment.routes';
import safetyInstructionRoute from 'app/entities/safety-instruction/safety-instruction.routes';
import taskRoute from 'app/entities/task/task.routes';
import teamRoute from 'app/entities/team/team.routes';
import trainingRoute from 'app/entities/training/training.routes';
import meetingRoute from 'app/entities/meeting/meeting.routes';
import otherEventRoute from 'app/entities/other-event/other-event.routes';

const routes: Routes = [
  { path: 'additional-training', children: additionalTrainingRoute },
  { path: 'admin/authority', children: adminAuthorityRoute },
  { path: 'employee', children: employeeRoute },
  { path: 'job-description', children: jobDescriptionRoute },
  { path: 'organization', children: organizationRoute },
  { path: 'position', children: positionRoute },
  { path: 'procedure-document', children: procedureDocumentRoute },
  { path: 'profession', children: professionRoute },
  { path: 'protective-equipment', children: protectiveEquipmentRoute },
  { path: 'safety-instruction', children: safetyInstructionRoute },
  { path: 'task', children: taskRoute },
  { path: 'team', children: teamRoute },
  { path: 'training', children: trainingRoute },
  { path: 'meeting', children: meetingRoute },
  { path: 'other-event', children: otherEventRoute },
];

export default routes;
