import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAdditionalTraining, NewAdditionalTraining } from '../additional-training.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAdditionalTraining for edit and NewAdditionalTrainingFormGroupInput for create.
 */
type AdditionalTrainingFormGroupInput = IAdditionalTraining | PartialWithRequiredKeyOf<NewAdditionalTraining>;

type AdditionalTrainingFormDefaults = Pick<NewAdditionalTraining, 'id'>;

type AdditionalTrainingFormGroupContent = {
  id: FormControl<IAdditionalTraining['id'] | NewAdditionalTraining['id']>;
  trainingName: FormControl<IAdditionalTraining['trainingName']>;
  trainingDate: FormControl<IAdditionalTraining['trainingDate']>;
  validityPeriod: FormControl<IAdditionalTraining['validityPeriod']>;
  nextTrainingDate: FormControl<IAdditionalTraining['nextTrainingDate']>;
  profession: FormControl<IAdditionalTraining['profession']>;
};

export type AdditionalTrainingFormGroup = FormGroup<AdditionalTrainingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AdditionalTrainingFormService {
  createAdditionalTrainingFormGroup(additionalTraining: AdditionalTrainingFormGroupInput = { id: null }): AdditionalTrainingFormGroup {
    const additionalTrainingRawValue = {
      ...this.getFormDefaults(),
      ...additionalTraining,
    };
    return new FormGroup<AdditionalTrainingFormGroupContent>({
      id: new FormControl(
        { value: additionalTrainingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      trainingName: new FormControl(additionalTrainingRawValue.trainingName, {
        validators: [Validators.required],
      }),
      trainingDate: new FormControl(additionalTrainingRawValue.trainingDate, {
        validators: [Validators.required],
      }),
      validityPeriod: new FormControl(additionalTrainingRawValue.validityPeriod, {
        validators: [Validators.required],
      }),
      nextTrainingDate: new FormControl(additionalTrainingRawValue.nextTrainingDate),
      profession: new FormControl(additionalTrainingRawValue.profession),
    });
  }

  getAdditionalTraining(form: AdditionalTrainingFormGroup): IAdditionalTraining | NewAdditionalTraining {
    return form.getRawValue() as IAdditionalTraining | NewAdditionalTraining;
  }

  resetForm(form: AdditionalTrainingFormGroup, additionalTraining: AdditionalTrainingFormGroupInput): void {
    const additionalTrainingRawValue = { ...this.getFormDefaults(), ...additionalTraining };
    form.reset(
      {
        ...additionalTrainingRawValue,
        id: { value: additionalTrainingRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AdditionalTrainingFormDefaults {
    return {
      id: null,
    };
  }
}
