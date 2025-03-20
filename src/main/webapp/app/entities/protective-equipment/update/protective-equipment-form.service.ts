import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProtectiveEquipment, NewProtectiveEquipment } from '../protective-equipment.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProtectiveEquipment for edit and NewProtectiveEquipmentFormGroupInput for create.
 */
type ProtectiveEquipmentFormGroupInput = IProtectiveEquipment | PartialWithRequiredKeyOf<NewProtectiveEquipment>;

type ProtectiveEquipmentFormDefaults = Pick<NewProtectiveEquipment, 'id'>;

type ProtectiveEquipmentFormGroupContent = {
  id: FormControl<IProtectiveEquipment['id'] | NewProtectiveEquipment['id']>;
  equipmentName: FormControl<IProtectiveEquipment['equipmentName']>;
  quantity: FormControl<IProtectiveEquipment['quantity']>;
  issuanceFrequency: FormControl<IProtectiveEquipment['issuanceFrequency']>;
  profession: FormControl<IProtectiveEquipment['profession']>;
};

export type ProtectiveEquipmentFormGroup = FormGroup<ProtectiveEquipmentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProtectiveEquipmentFormService {
  createProtectiveEquipmentFormGroup(protectiveEquipment: ProtectiveEquipmentFormGroupInput = { id: null }): ProtectiveEquipmentFormGroup {
    const protectiveEquipmentRawValue = {
      ...this.getFormDefaults(),
      ...protectiveEquipment,
    };
    return new FormGroup<ProtectiveEquipmentFormGroupContent>({
      id: new FormControl(
        { value: protectiveEquipmentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      equipmentName: new FormControl(protectiveEquipmentRawValue.equipmentName, {
        validators: [Validators.required],
      }),
      quantity: new FormControl(protectiveEquipmentRawValue.quantity, {
        validators: [Validators.required],
      }),
      issuanceFrequency: new FormControl(protectiveEquipmentRawValue.issuanceFrequency, {
        validators: [Validators.required],
      }),
      profession: new FormControl(protectiveEquipmentRawValue.profession),
    });
  }

  getProtectiveEquipment(form: ProtectiveEquipmentFormGroup): IProtectiveEquipment | NewProtectiveEquipment {
    return form.getRawValue() as IProtectiveEquipment | NewProtectiveEquipment;
  }

  resetForm(form: ProtectiveEquipmentFormGroup, protectiveEquipment: ProtectiveEquipmentFormGroupInput): void {
    const protectiveEquipmentRawValue = { ...this.getFormDefaults(), ...protectiveEquipment };
    form.reset(
      {
        ...protectiveEquipmentRawValue,
        id: { value: protectiveEquipmentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProtectiveEquipmentFormDefaults {
    return {
      id: null,
    };
  }
}
