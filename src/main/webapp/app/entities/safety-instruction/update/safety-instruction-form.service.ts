import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISafetyInstruction, NewSafetyInstruction } from '../safety-instruction.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISafetyInstruction for edit and NewSafetyInstructionFormGroupInput for create.
 */
type SafetyInstructionFormGroupInput = ISafetyInstruction | PartialWithRequiredKeyOf<NewSafetyInstruction>;

type SafetyInstructionFormDefaults = Pick<NewSafetyInstruction, 'id'>;

type SafetyInstructionFormGroupContent = {
  id: FormControl<ISafetyInstruction['id'] | NewSafetyInstruction['id']>;
  instructionName: FormControl<ISafetyInstruction['instructionName']>;
  introductionDate: FormControl<ISafetyInstruction['introductionDate']>;
  profession: FormControl<ISafetyInstruction['profession']>;
  position: FormControl<ISafetyInstruction['position']>;
};

export type SafetyInstructionFormGroup = FormGroup<SafetyInstructionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SafetyInstructionFormService {
  createSafetyInstructionFormGroup(safetyInstruction: SafetyInstructionFormGroupInput = { id: null }): SafetyInstructionFormGroup {
    const safetyInstructionRawValue = {
      ...this.getFormDefaults(),
      ...safetyInstruction,
    };
    return new FormGroup<SafetyInstructionFormGroupContent>({
      id: new FormControl(
        { value: safetyInstructionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      instructionName: new FormControl(safetyInstructionRawValue.instructionName, {
        validators: [Validators.required],
      }),
      introductionDate: new FormControl(safetyInstructionRawValue.introductionDate, {
        validators: [Validators.required],
      }),
      profession: new FormControl(safetyInstructionRawValue.profession),
      position: new FormControl(safetyInstructionRawValue.position),
    });
  }

  getSafetyInstruction(form: SafetyInstructionFormGroup): ISafetyInstruction | NewSafetyInstruction {
    return form.getRawValue() as ISafetyInstruction | NewSafetyInstruction;
  }

  resetForm(form: SafetyInstructionFormGroup, safetyInstruction: SafetyInstructionFormGroupInput): void {
    const safetyInstructionRawValue = { ...this.getFormDefaults(), ...safetyInstruction };
    form.reset(
      {
        ...safetyInstructionRawValue,
        id: { value: safetyInstructionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SafetyInstructionFormDefaults {
    return {
      id: null,
    };
  }
}
