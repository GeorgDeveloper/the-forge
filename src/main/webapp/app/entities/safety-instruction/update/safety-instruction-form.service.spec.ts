import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../safety-instruction.test-samples';

import { SafetyInstructionFormService } from './safety-instruction-form.service';

describe('SafetyInstruction Form Service', () => {
  let service: SafetyInstructionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SafetyInstructionFormService);
  });

  describe('Service methods', () => {
    describe('createSafetyInstructionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSafetyInstructionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            instructionName: expect.any(Object),
            introductionDate: expect.any(Object),
            profession: expect.any(Object),
            position: expect.any(Object),
          }),
        );
      });

      it('passing ISafetyInstruction should create a new form with FormGroup', () => {
        const formGroup = service.createSafetyInstructionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            instructionName: expect.any(Object),
            introductionDate: expect.any(Object),
            profession: expect.any(Object),
            position: expect.any(Object),
          }),
        );
      });
    });

    describe('getSafetyInstruction', () => {
      it('should return NewSafetyInstruction for default SafetyInstruction initial value', () => {
        const formGroup = service.createSafetyInstructionFormGroup(sampleWithNewData);

        const safetyInstruction = service.getSafetyInstruction(formGroup) as any;

        expect(safetyInstruction).toMatchObject(sampleWithNewData);
      });

      it('should return NewSafetyInstruction for empty SafetyInstruction initial value', () => {
        const formGroup = service.createSafetyInstructionFormGroup();

        const safetyInstruction = service.getSafetyInstruction(formGroup) as any;

        expect(safetyInstruction).toMatchObject({});
      });

      it('should return ISafetyInstruction', () => {
        const formGroup = service.createSafetyInstructionFormGroup(sampleWithRequiredData);

        const safetyInstruction = service.getSafetyInstruction(formGroup) as any;

        expect(safetyInstruction).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISafetyInstruction should not enable id FormControl', () => {
        const formGroup = service.createSafetyInstructionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSafetyInstruction should disable id FormControl', () => {
        const formGroup = service.createSafetyInstructionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
