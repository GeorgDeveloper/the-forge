import dayjs from 'dayjs/esm';
import { IEmployee } from 'app/entities/employee/employee.model';
import { TaskStatus } from 'app/entities/enumerations/task-status.model';
import { TaskPriority } from 'app/entities/enumerations/task-priority.model';

export interface ITask {
  id: number;
  taskName?: string | null;
  creationDate?: dayjs.Dayjs | null;
  plannedCompletionDate?: dayjs.Dayjs | null;
  status?: keyof typeof TaskStatus | null;
  priority?: keyof typeof TaskPriority | null;
  body?: string | null;
  employee?: IEmployee | null;
}

export type NewTask = Omit<ITask, 'id'> & { id: null };
