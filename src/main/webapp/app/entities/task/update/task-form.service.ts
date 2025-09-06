import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITask, NewTask } from '../task.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITask for edit and NewTaskFormGroupInput for create.
 */
type TaskFormGroupInput = ITask | PartialWithRequiredKeyOf<NewTask>;

type TaskFormDefaults = Pick<NewTask, 'id'>;

type TaskFormGroupContent = {
  id: FormControl<ITask['id'] | NewTask['id']>;
  taskName: FormControl<ITask['taskName']>;
  creationDate: FormControl<ITask['creationDate']>;
  plannedCompletionDate: FormControl<ITask['plannedCompletionDate']>;
  actualCompletionDate: FormControl<ITask['actualCompletionDate']>;
  status: FormControl<ITask['status']>;
  priority: FormControl<ITask['priority']>;
  body: FormControl<ITask['body']>;
  employee: FormControl<ITask['employee']>;
};

export type TaskFormGroup = FormGroup<TaskFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TaskFormService {
  createTaskFormGroup(task: TaskFormGroupInput = { id: null }): TaskFormGroup {
    const taskRawValue = {
      ...this.getFormDefaults(),
      ...task,
    };
    return new FormGroup<TaskFormGroupContent>({
      id: new FormControl(
        { value: taskRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      taskName: new FormControl(taskRawValue.taskName, {
        validators: [Validators.required],
      }),
      creationDate: new FormControl(taskRawValue.creationDate, {
        validators: [Validators.required],
      }),
      plannedCompletionDate: new FormControl(taskRawValue.plannedCompletionDate, {
        validators: [Validators.required],
      }),
      actualCompletionDate: new FormControl(taskRawValue.actualCompletionDate),
      status: new FormControl(taskRawValue.status, {
        validators: [Validators.required],
      }),
      priority: new FormControl(taskRawValue.priority, {
        validators: [Validators.required],
      }),
      body: new FormControl(taskRawValue.body, {
        validators: [Validators.required],
      }),
      employee: new FormControl(taskRawValue.employee),
    });
  }

  getTask(form: TaskFormGroup): ITask | NewTask {
    return form.getRawValue() as ITask | NewTask;
  }

  resetForm(form: TaskFormGroup, task: TaskFormGroupInput): void {
    const taskRawValue = { ...this.getFormDefaults(), ...task };
    form.reset(
      {
        ...taskRawValue,
        id: { value: taskRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TaskFormDefaults {
    return {
      id: null,
    };
  }
}
