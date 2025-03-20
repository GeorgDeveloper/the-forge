import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../protective-equipment.test-samples';

import { ProtectiveEquipmentFormService } from './protective-equipment-form.service';

describe('ProtectiveEquipment Form Service', () => {
  let service: ProtectiveEquipmentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProtectiveEquipmentFormService);
  });

  describe('Service methods', () => {
    describe('createProtectiveEquipmentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProtectiveEquipmentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            equipmentName: expect.any(Object),
            quantity: expect.any(Object),
            issuanceFrequency: expect.any(Object),
            profession: expect.any(Object),
          }),
        );
      });

      it('passing IProtectiveEquipment should create a new form with FormGroup', () => {
        const formGroup = service.createProtectiveEquipmentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            equipmentName: expect.any(Object),
            quantity: expect.any(Object),
            issuanceFrequency: expect.any(Object),
            profession: expect.any(Object),
          }),
        );
      });
    });

    describe('getProtectiveEquipment', () => {
      it('should return NewProtectiveEquipment for default ProtectiveEquipment initial value', () => {
        const formGroup = service.createProtectiveEquipmentFormGroup(sampleWithNewData);

        const protectiveEquipment = service.getProtectiveEquipment(formGroup) as any;

        expect(protectiveEquipment).toMatchObject(sampleWithNewData);
      });

      it('should return NewProtectiveEquipment for empty ProtectiveEquipment initial value', () => {
        const formGroup = service.createProtectiveEquipmentFormGroup();

        const protectiveEquipment = service.getProtectiveEquipment(formGroup) as any;

        expect(protectiveEquipment).toMatchObject({});
      });

      it('should return IProtectiveEquipment', () => {
        const formGroup = service.createProtectiveEquipmentFormGroup(sampleWithRequiredData);

        const protectiveEquipment = service.getProtectiveEquipment(formGroup) as any;

        expect(protectiveEquipment).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProtectiveEquipment should not enable id FormControl', () => {
        const formGroup = service.createProtectiveEquipmentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProtectiveEquipment should disable id FormControl', () => {
        const formGroup = service.createProtectiveEquipmentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
