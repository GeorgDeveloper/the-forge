import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPosition, NewPosition } from '../position.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPosition for edit and NewPositionFormGroupInput for create.
 */
type PositionFormGroupInput = IPosition | PartialWithRequiredKeyOf<NewPosition>;

type PositionFormDefaults = Pick<NewPosition, 'id'>;

type PositionFormGroupContent = {
  id: FormControl<IPosition['id'] | NewPosition['id']>;
  positionName: FormControl<IPosition['positionName']>;
  jobDescription: FormControl<IPosition['jobDescription']>;
};

export type PositionFormGroup = FormGroup<PositionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PositionFormService {
  createPositionFormGroup(position: PositionFormGroupInput = { id: null }): PositionFormGroup {
    const positionRawValue = {
      ...this.getFormDefaults(),
      ...position,
    };
    return new FormGroup<PositionFormGroupContent>({
      id: new FormControl(
        { value: positionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      positionName: new FormControl(positionRawValue.positionName, {
        validators: [Validators.required],
      }),
      jobDescription: new FormControl(positionRawValue.jobDescription),
    });
  }

  getPosition(form: PositionFormGroup): IPosition | NewPosition {
    return form.getRawValue() as IPosition | NewPosition;
  }

  resetForm(form: PositionFormGroup, position: PositionFormGroupInput): void {
    const positionRawValue = { ...this.getFormDefaults(), ...position };
    form.reset(
      {
        ...positionRawValue,
        id: { value: positionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PositionFormDefaults {
    return {
      id: null,
    };
  }
}
