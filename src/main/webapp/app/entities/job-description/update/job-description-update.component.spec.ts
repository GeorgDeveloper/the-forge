import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { JobDescriptionService } from '../service/job-description.service';
import { IJobDescription } from '../job-description.model';
import { JobDescriptionFormService } from './job-description-form.service';

import { JobDescriptionUpdateComponent } from './job-description-update.component';

describe('JobDescription Management Update Component', () => {
  let comp: JobDescriptionUpdateComponent;
  let fixture: ComponentFixture<JobDescriptionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let jobDescriptionFormService: JobDescriptionFormService;
  let jobDescriptionService: JobDescriptionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [JobDescriptionUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(JobDescriptionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(JobDescriptionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    jobDescriptionFormService = TestBed.inject(JobDescriptionFormService);
    jobDescriptionService = TestBed.inject(JobDescriptionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const jobDescription: IJobDescription = { id: 2589 };

      activatedRoute.data = of({ jobDescription });
      comp.ngOnInit();

      expect(comp.jobDescription).toEqual(jobDescription);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IJobDescription>>();
      const jobDescription = { id: 8232 };
      jest.spyOn(jobDescriptionFormService, 'getJobDescription').mockReturnValue(jobDescription);
      jest.spyOn(jobDescriptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ jobDescription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: jobDescription }));
      saveSubject.complete();

      // THEN
      expect(jobDescriptionFormService.getJobDescription).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(jobDescriptionService.update).toHaveBeenCalledWith(expect.objectContaining(jobDescription));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IJobDescription>>();
      const jobDescription = { id: 8232 };
      jest.spyOn(jobDescriptionFormService, 'getJobDescription').mockReturnValue({ id: null });
      jest.spyOn(jobDescriptionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ jobDescription: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: jobDescription }));
      saveSubject.complete();

      // THEN
      expect(jobDescriptionFormService.getJobDescription).toHaveBeenCalled();
      expect(jobDescriptionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IJobDescription>>();
      const jobDescription = { id: 8232 };
      jest.spyOn(jobDescriptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ jobDescription });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(jobDescriptionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
