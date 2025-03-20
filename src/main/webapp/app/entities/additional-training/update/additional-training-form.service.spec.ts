import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../additional-training.test-samples';

import { AdditionalTrainingFormService } from './additional-training-form.service';

describe('AdditionalTraining Form Service', () => {
  let service: AdditionalTrainingFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdditionalTrainingFormService);
  });

  describe('Service methods', () => {
    describe('createAdditionalTrainingFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAdditionalTrainingFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            trainingName: expect.any(Object),
            trainingDate: expect.any(Object),
            validityPeriod: expect.any(Object),
            nextTrainingDate: expect.any(Object),
            profession: expect.any(Object),
          }),
        );
      });

      it('passing IAdditionalTraining should create a new form with FormGroup', () => {
        const formGroup = service.createAdditionalTrainingFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            trainingName: expect.any(Object),
            trainingDate: expect.any(Object),
            validityPeriod: expect.any(Object),
            nextTrainingDate: expect.any(Object),
            profession: expect.any(Object),
          }),
        );
      });
    });

    describe('getAdditionalTraining', () => {
      it('should return NewAdditionalTraining for default AdditionalTraining initial value', () => {
        const formGroup = service.createAdditionalTrainingFormGroup(sampleWithNewData);

        const additionalTraining = service.getAdditionalTraining(formGroup) as any;

        expect(additionalTraining).toMatchObject(sampleWithNewData);
      });

      it('should return NewAdditionalTraining for empty AdditionalTraining initial value', () => {
        const formGroup = service.createAdditionalTrainingFormGroup();

        const additionalTraining = service.getAdditionalTraining(formGroup) as any;

        expect(additionalTraining).toMatchObject({});
      });

      it('should return IAdditionalTraining', () => {
        const formGroup = service.createAdditionalTrainingFormGroup(sampleWithRequiredData);

        const additionalTraining = service.getAdditionalTraining(formGroup) as any;

        expect(additionalTraining).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAdditionalTraining should not enable id FormControl', () => {
        const formGroup = service.createAdditionalTrainingFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAdditionalTraining should disable id FormControl', () => {
        const formGroup = service.createAdditionalTrainingFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
