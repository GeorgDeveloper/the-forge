import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IProfession } from 'app/entities/profession/profession.model';
import { ProfessionService } from 'app/entities/profession/service/profession.service';
import { IPosition } from 'app/entities/position/position.model';
import { PositionService } from 'app/entities/position/service/position.service';
import { ISafetyInstruction } from '../safety-instruction.model';
import { SafetyInstructionService } from '../service/safety-instruction.service';
import { SafetyInstructionFormService } from './safety-instruction-form.service';

import { SafetyInstructionUpdateComponent } from './safety-instruction-update.component';

describe('SafetyInstruction Management Update Component', () => {
  let comp: SafetyInstructionUpdateComponent;
  let fixture: ComponentFixture<SafetyInstructionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let safetyInstructionFormService: SafetyInstructionFormService;
  let safetyInstructionService: SafetyInstructionService;
  let professionService: ProfessionService;
  let positionService: PositionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SafetyInstructionUpdateComponent],
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
      .overrideTemplate(SafetyInstructionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SafetyInstructionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    safetyInstructionFormService = TestBed.inject(SafetyInstructionFormService);
    safetyInstructionService = TestBed.inject(SafetyInstructionService);
    professionService = TestBed.inject(ProfessionService);
    positionService = TestBed.inject(PositionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Profession query and add missing value', () => {
      const safetyInstruction: ISafetyInstruction = { id: 9685 };
      const profession: IProfession = { id: 12610 };
      safetyInstruction.profession = profession;

      const professionCollection: IProfession[] = [{ id: 12610 }];
      jest.spyOn(professionService, 'query').mockReturnValue(of(new HttpResponse({ body: professionCollection })));
      const additionalProfessions = [profession];
      const expectedCollection: IProfession[] = [...additionalProfessions, ...professionCollection];
      jest.spyOn(professionService, 'addProfessionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ safetyInstruction });
      comp.ngOnInit();

      expect(professionService.query).toHaveBeenCalled();
      expect(professionService.addProfessionToCollectionIfMissing).toHaveBeenCalledWith(
        professionCollection,
        ...additionalProfessions.map(expect.objectContaining),
      );
      expect(comp.professionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Position query and add missing value', () => {
      const safetyInstruction: ISafetyInstruction = { id: 9685 };
      const position: IPosition = { id: 15262 };
      safetyInstruction.position = position;

      const positionCollection: IPosition[] = [{ id: 15262 }];
      jest.spyOn(positionService, 'query').mockReturnValue(of(new HttpResponse({ body: positionCollection })));
      const additionalPositions = [position];
      const expectedCollection: IPosition[] = [...additionalPositions, ...positionCollection];
      jest.spyOn(positionService, 'addPositionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ safetyInstruction });
      comp.ngOnInit();

      expect(positionService.query).toHaveBeenCalled();
      expect(positionService.addPositionToCollectionIfMissing).toHaveBeenCalledWith(
        positionCollection,
        ...additionalPositions.map(expect.objectContaining),
      );
      expect(comp.positionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const safetyInstruction: ISafetyInstruction = { id: 9685 };
      const profession: IProfession = { id: 12610 };
      safetyInstruction.profession = profession;
      const position: IPosition = { id: 15262 };
      safetyInstruction.position = position;

      activatedRoute.data = of({ safetyInstruction });
      comp.ngOnInit();

      expect(comp.professionsSharedCollection).toContainEqual(profession);
      expect(comp.positionsSharedCollection).toContainEqual(position);
      expect(comp.safetyInstruction).toEqual(safetyInstruction);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISafetyInstruction>>();
      const safetyInstruction = { id: 5402 };
      jest.spyOn(safetyInstructionFormService, 'getSafetyInstruction').mockReturnValue(safetyInstruction);
      jest.spyOn(safetyInstructionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ safetyInstruction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: safetyInstruction }));
      saveSubject.complete();

      // THEN
      expect(safetyInstructionFormService.getSafetyInstruction).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(safetyInstructionService.update).toHaveBeenCalledWith(expect.objectContaining(safetyInstruction));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISafetyInstruction>>();
      const safetyInstruction = { id: 5402 };
      jest.spyOn(safetyInstructionFormService, 'getSafetyInstruction').mockReturnValue({ id: null });
      jest.spyOn(safetyInstructionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ safetyInstruction: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: safetyInstruction }));
      saveSubject.complete();

      // THEN
      expect(safetyInstructionFormService.getSafetyInstruction).toHaveBeenCalled();
      expect(safetyInstructionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISafetyInstruction>>();
      const safetyInstruction = { id: 5402 };
      jest.spyOn(safetyInstructionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ safetyInstruction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(safetyInstructionService.update).toHaveBeenCalled();
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

    describe('comparePosition', () => {
      it('Should forward to positionService', () => {
        const entity = { id: 15262 };
        const entity2 = { id: 21947 };
        jest.spyOn(positionService, 'comparePosition');
        comp.comparePosition(entity, entity2);
        expect(positionService.comparePosition).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
