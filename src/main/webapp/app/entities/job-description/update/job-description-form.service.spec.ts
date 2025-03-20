import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../job-description.test-samples';

import { JobDescriptionFormService } from './job-description-form.service';

describe('JobDescription Form Service', () => {
  let service: JobDescriptionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(JobDescriptionFormService);
  });

  describe('Service methods', () => {
    describe('createJobDescriptionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createJobDescriptionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            descriptionName: expect.any(Object),
            approvalDate: expect.any(Object),
          }),
        );
      });

      it('passing IJobDescription should create a new form with FormGroup', () => {
        const formGroup = service.createJobDescriptionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            descriptionName: expect.any(Object),
            approvalDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getJobDescription', () => {
      it('should return NewJobDescription for default JobDescription initial value', () => {
        const formGroup = service.createJobDescriptionFormGroup(sampleWithNewData);

        const jobDescription = service.getJobDescription(formGroup) as any;

        expect(jobDescription).toMatchObject(sampleWithNewData);
      });

      it('should return NewJobDescription for empty JobDescription initial value', () => {
        const formGroup = service.createJobDescriptionFormGroup();

        const jobDescription = service.getJobDescription(formGroup) as any;

        expect(jobDescription).toMatchObject({});
      });

      it('should return IJobDescription', () => {
        const formGroup = service.createJobDescriptionFormGroup(sampleWithRequiredData);

        const jobDescription = service.getJobDescription(formGroup) as any;

        expect(jobDescription).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IJobDescription should not enable id FormControl', () => {
        const formGroup = service.createJobDescriptionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewJobDescription should disable id FormControl', () => {
        const formGroup = service.createJobDescriptionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
