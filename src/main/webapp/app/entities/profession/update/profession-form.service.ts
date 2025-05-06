import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProfession, NewProfession } from '../profession.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProfession for edit and NewProfessionFormGroupInput for create.
 */
type ProfessionFormGroupInput = IProfession | PartialWithRequiredKeyOf<NewProfession>;

type ProfessionFormDefaults = Pick<NewProfession, 'id' | 'employees'>;

type ProfessionFormGroupContent = {
  id: FormControl<IProfession['id'] | NewProfession['id']>;
  professionName: FormControl<IProfession['professionName']>;
  employees: FormControl<IProfession['employees']>;
};

export type ProfessionFormGroup = FormGroup<ProfessionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProfessionFormService {
  createProfessionFormGroup(profession: ProfessionFormGroupInput = { id: null }): ProfessionFormGroup {
    const professionRawValue = {
      ...this.getFormDefaults(),
      ...profession,
    };
    return new FormGroup<ProfessionFormGroupContent>({
      id: new FormControl(
        { value: professionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      professionName: new FormControl(professionRawValue.professionName, {
        validators: [Validators.required],
      }),
      employees: new FormControl(professionRawValue.employees ?? []),
    });
  }

  getProfession(professionForm: ProfessionFormGroup): IProfession | NewProfession {
    const profession = professionForm.getRawValue() as IProfession | NewProfession;
    // Оставляем только ID сотрудников
    if (profession.employees) {
      profession.employees = profession.employees.map(e => ({ id: e.id }));
    }
    return profession;
  }

  resetForm(form: ProfessionFormGroup, profession: ProfessionFormGroupInput): void {
    const professionRawValue = { ...this.getFormDefaults(), ...profession };
    form.reset(
      {
        ...professionRawValue,
        id: { value: professionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProfessionFormDefaults {
    return {
      id: null,
      employees: [],
    };
  }
}
