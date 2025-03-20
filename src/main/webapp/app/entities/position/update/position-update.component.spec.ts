import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IJobDescription } from 'app/entities/job-description/job-description.model';
import { JobDescriptionService } from 'app/entities/job-description/service/job-description.service';
import { PositionService } from '../service/position.service';
import { IPosition } from '../position.model';
import { PositionFormService } from './position-form.service';

import { PositionUpdateComponent } from './position-update.component';

describe('Position Management Update Component', () => {
  let comp: PositionUpdateComponent;
  let fixture: ComponentFixture<PositionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let positionFormService: PositionFormService;
  let positionService: PositionService;
  let jobDescriptionService: JobDescriptionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PositionUpdateComponent],
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
      .overrideTemplate(PositionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PositionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    positionFormService = TestBed.inject(PositionFormService);
    positionService = TestBed.inject(PositionService);
    jobDescriptionService = TestBed.inject(JobDescriptionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call jobDescription query and add missing value', () => {
      const position: IPosition = { id: 21947 };
      const jobDescription: IJobDescription = { id: 8232 };
      position.jobDescription = jobDescription;

      const jobDescriptionCollection: IJobDescription[] = [{ id: 8232 }];
      jest.spyOn(jobDescriptionService, 'query').mockReturnValue(of(new HttpResponse({ body: jobDescriptionCollection })));
      const expectedCollection: IJobDescription[] = [jobDescription, ...jobDescriptionCollection];
      jest.spyOn(jobDescriptionService, 'addJobDescriptionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ position });
      comp.ngOnInit();

      expect(jobDescriptionService.query).toHaveBeenCalled();
      expect(jobDescriptionService.addJobDescriptionToCollectionIfMissing).toHaveBeenCalledWith(jobDescriptionCollection, jobDescription);
      expect(comp.jobDescriptionsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const position: IPosition = { id: 21947 };
      const jobDescription: IJobDescription = { id: 8232 };
      position.jobDescription = jobDescription;

      activatedRoute.data = of({ position });
      comp.ngOnInit();

      expect(comp.jobDescriptionsCollection).toContainEqual(jobDescription);
      expect(comp.position).toEqual(position);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPosition>>();
      const position = { id: 15262 };
      jest.spyOn(positionFormService, 'getPosition').mockReturnValue(position);
      jest.spyOn(positionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ position });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: position }));
      saveSubject.complete();

      // THEN
      expect(positionFormService.getPosition).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(positionService.update).toHaveBeenCalledWith(expect.objectContaining(position));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPosition>>();
      const position = { id: 15262 };
      jest.spyOn(positionFormService, 'getPosition').mockReturnValue({ id: null });
      jest.spyOn(positionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ position: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: position }));
      saveSubject.complete();

      // THEN
      expect(positionFormService.getPosition).toHaveBeenCalled();
      expect(positionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPosition>>();
      const position = { id: 15262 };
      jest.spyOn(positionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ position });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(positionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareJobDescription', () => {
      it('Should forward to jobDescriptionService', () => {
        const entity = { id: 8232 };
        const entity2 = { id: 2589 };
        jest.spyOn(jobDescriptionService, 'compareJobDescription');
        comp.compareJobDescription(entity, entity2);
        expect(jobDescriptionService.compareJobDescription).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
