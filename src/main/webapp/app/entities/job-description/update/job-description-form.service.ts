import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IJobDescription, NewJobDescription } from '../job-description.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IJobDescription for edit and NewJobDescriptionFormGroupInput for create.
 */
type JobDescriptionFormGroupInput = IJobDescription | PartialWithRequiredKeyOf<NewJobDescription>;

type JobDescriptionFormDefaults = Pick<NewJobDescription, 'id'>;

type JobDescriptionFormGroupContent = {
  id: FormControl<IJobDescription['id'] | NewJobDescription['id']>;
  descriptionName: FormControl<IJobDescription['descriptionName']>;
  approvalDate: FormControl<IJobDescription['approvalDate']>;
};

export type JobDescriptionFormGroup = FormGroup<JobDescriptionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class JobDescriptionFormService {
  createJobDescriptionFormGroup(jobDescription: JobDescriptionFormGroupInput = { id: null }): JobDescriptionFormGroup {
    const jobDescriptionRawValue = {
      ...this.getFormDefaults(),
      ...jobDescription,
    };
    return new FormGroup<JobDescriptionFormGroupContent>({
      id: new FormControl(
        { value: jobDescriptionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      descriptionName: new FormControl(jobDescriptionRawValue.descriptionName, {
        validators: [Validators.required],
      }),
      approvalDate: new FormControl(jobDescriptionRawValue.approvalDate, {
        validators: [Validators.required],
      }),
    });
  }

  getJobDescription(form: JobDescriptionFormGroup): IJobDescription | NewJobDescription {
    return form.getRawValue() as IJobDescription | NewJobDescription;
  }

  resetForm(form: JobDescriptionFormGroup, jobDescription: JobDescriptionFormGroupInput): void {
    const jobDescriptionRawValue = { ...this.getFormDefaults(), ...jobDescription };
    form.reset(
      {
        ...jobDescriptionRawValue,
        id: { value: jobDescriptionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): JobDescriptionFormDefaults {
    return {
      id: null,
    };
  }
}
