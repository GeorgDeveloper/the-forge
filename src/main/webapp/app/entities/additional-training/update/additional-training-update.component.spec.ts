import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IProfession } from 'app/entities/profession/profession.model';
import { ProfessionService } from 'app/entities/profession/service/profession.service';
import { AdditionalTrainingService } from '../service/additional-training.service';
import { IAdditionalTraining } from '../additional-training.model';
import { AdditionalTrainingFormService } from './additional-training-form.service';

import { AdditionalTrainingUpdateComponent } from './additional-training-update.component';

describe('AdditionalTraining Management Update Component', () => {
  let comp: AdditionalTrainingUpdateComponent;
  let fixture: ComponentFixture<AdditionalTrainingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let additionalTrainingFormService: AdditionalTrainingFormService;
  let additionalTrainingService: AdditionalTrainingService;
  let professionService: ProfessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AdditionalTrainingUpdateComponent],
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
      .overrideTemplate(AdditionalTrainingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AdditionalTrainingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    additionalTrainingFormService = TestBed.inject(AdditionalTrainingFormService);
    additionalTrainingService = TestBed.inject(AdditionalTrainingService);
    professionService = TestBed.inject(ProfessionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Profession query and add missing value', () => {
      const additionalTraining: IAdditionalTraining = { id: 25601 };
      const profession: IProfession = { id: 12610 };
      additionalTraining.profession = profession;

      const professionCollection: IProfession[] = [{ id: 12610 }];
      jest.spyOn(professionService, 'query').mockReturnValue(of(new HttpResponse({ body: professionCollection })));
      const additionalProfessions = [profession];
      const expectedCollection: IProfession[] = [...additionalProfessions, ...professionCollection];
      jest.spyOn(professionService, 'addProfessionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ additionalTraining });
      comp.ngOnInit();

      expect(professionService.query).toHaveBeenCalled();
      expect(professionService.addProfessionToCollectionIfMissing).toHaveBeenCalledWith(
        professionCollection,
        ...additionalProfessions.map(expect.objectContaining),
      );
      expect(comp.professionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const additionalTraining: IAdditionalTraining = { id: 25601 };
      const profession: IProfession = { id: 12610 };
      additionalTraining.profession = profession;

      activatedRoute.data = of({ additionalTraining });
      comp.ngOnInit();

      expect(comp.professionsSharedCollection).toContainEqual(profession);
      expect(comp.additionalTraining).toEqual(additionalTraining);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdditionalTraining>>();
      const additionalTraining = { id: 2143 };
      jest.spyOn(additionalTrainingFormService, 'getAdditionalTraining').mockReturnValue(additionalTraining);
      jest.spyOn(additionalTrainingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ additionalTraining });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: additionalTraining }));
      saveSubject.complete();

      // THEN
      expect(additionalTrainingFormService.getAdditionalTraining).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(additionalTrainingService.update).toHaveBeenCalledWith(expect.objectContaining(additionalTraining));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdditionalTraining>>();
      const additionalTraining = { id: 2143 };
      jest.spyOn(additionalTrainingFormService, 'getAdditionalTraining').mockReturnValue({ id: null });
      jest.spyOn(additionalTrainingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ additionalTraining: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: additionalTraining }));
      saveSubject.complete();

      // THEN
      expect(additionalTrainingFormService.getAdditionalTraining).toHaveBeenCalled();
      expect(additionalTrainingService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdditionalTraining>>();
      const additionalTraining = { id: 2143 };
      jest.spyOn(additionalTrainingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ additionalTraining });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(additionalTrainingService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProfession', () => {
      it('Should forward to professionService', () => {
        const entity = { id: 12610 };
        const entity2 = { id: 30557 };
        jest.spyOn(professionService, 'compareProfession');
        comp.compareProfession(entity, entity2);
        expect(professionService.compareProfession).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
