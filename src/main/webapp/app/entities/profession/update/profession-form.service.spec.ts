import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../profession.test-samples';

import { ProfessionFormService } from './profession-form.service';

describe('Profession Form Service', () => {
  let service: ProfessionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProfessionFormService);
  });

  describe('Service methods', () => {
    describe('createProfessionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProfessionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            professionName: expect.any(Object),
            employees: expect.any(Object),
          }),
        );
      });

      it('passing IProfession should create a new form with FormGroup', () => {
        const formGroup = service.createProfessionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            professionName: expect.any(Object),
            employees: expect.any(Object),
          }),
        );
      });
    });

    describe('getProfession', () => {
      it('should return NewProfession for default Profession initial value', () => {
        const formGroup = service.createProfessionFormGroup(sampleWithNewData);

        const profession = service.getProfession(formGroup) as any;

        expect(profession).toMatchObject(sampleWithNewData);
      });

      it('should return NewProfession for empty Profession initial value', () => {
        const formGroup = service.createProfessionFormGroup();

        const profession = service.getProfession(formGroup) as any;

        expect(profession).toMatchObject({});
      });

      it('should return IProfession', () => {
        const formGroup = service.createProfessionFormGroup(sampleWithRequiredData);

        const profession = service.getProfession(formGroup) as any;

        expect(profession).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProfession should not enable id FormControl', () => {
        const formGroup = service.createProfessionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProfession should disable id FormControl', () => {
        const formGroup = service.createProfessionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
