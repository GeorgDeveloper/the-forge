import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IProfession } from 'app/entities/profession/profession.model';
import { ProfessionService } from 'app/entities/profession/service/profession.service';
import { ProtectiveEquipmentService } from '../service/protective-equipment.service';
import { IProtectiveEquipment } from '../protective-equipment.model';
import { ProtectiveEquipmentFormService } from './protective-equipment-form.service';

import { ProtectiveEquipmentUpdateComponent } from './protective-equipment-update.component';

describe('ProtectiveEquipment Management Update Component', () => {
  let comp: ProtectiveEquipmentUpdateComponent;
  let fixture: ComponentFixture<ProtectiveEquipmentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let protectiveEquipmentFormService: ProtectiveEquipmentFormService;
  let protectiveEquipmentService: ProtectiveEquipmentService;
  let professionService: ProfessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProtectiveEquipmentUpdateComponent],
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
      .overrideTemplate(ProtectiveEquipmentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProtectiveEquipmentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    protectiveEquipmentFormService = TestBed.inject(ProtectiveEquipmentFormService);
    protectiveEquipmentService = TestBed.inject(ProtectiveEquipmentService);
    professionService = TestBed.inject(ProfessionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Profession query and add missing value', () => {
      const protectiveEquipment: IProtectiveEquipment = { id: 14123 };
      const profession: IProfession = { id: 12610 };
      protectiveEquipment.profession = profession;

      const professionCollection: IProfession[] = [{ id: 12610 }];
      jest.spyOn(professionService, 'query').mockReturnValue(of(new HttpResponse({ body: professionCollection })));
      const additionalProfessions = [profession];
      const expectedCollection: IProfession[] = [...additionalProfessions, ...professionCollection];
      jest.spyOn(professionService, 'addProfessionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ protectiveEquipment });
      comp.ngOnInit();

      expect(professionService.query).toHaveBeenCalled();
      expect(professionService.addProfessionToCollectionIfMissing).toHaveBeenCalledWith(
        professionCollection,
        ...additionalProfessions.map(expect.objectContaining),
      );
      expect(comp.professionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const protectiveEquipment: IProtectiveEquipment = { id: 14123 };
      const profession: IProfession = { id: 12610 };
      protectiveEquipment.profession = profession;

      activatedRoute.data = of({ protectiveEquipment });
      comp.ngOnInit();

      expect(comp.professionsSharedCollection).toContainEqual(profession);
      expect(comp.protectiveEquipment).toEqual(protectiveEquipment);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProtectiveEquipment>>();
      const protectiveEquipment = { id: 26931 };
      jest.spyOn(protectiveEquipmentFormService, 'getProtectiveEquipment').mockReturnValue(protectiveEquipment);
      jest.spyOn(protectiveEquipmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ protectiveEquipment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: protectiveEquipment }));
      saveSubject.complete();

      // THEN
      expect(protectiveEquipmentFormService.getProtectiveEquipment).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(protectiveEquipmentService.update).toHaveBeenCalledWith(expect.objectContaining(protectiveEquipment));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProtectiveEquipment>>();
      const protectiveEquipment = { id: 26931 };
      jest.spyOn(protectiveEquipmentFormService, 'getProtectiveEquipment').mockReturnValue({ id: null });
      jest.spyOn(protectiveEquipmentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ protectiveEquipment: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: protectiveEquipment }));
      saveSubject.complete();

      // THEN
      expect(protectiveEquipmentFormService.getProtectiveEquipment).toHaveBeenCalled();
      expect(protectiveEquipmentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProtectiveEquipment>>();
      const protectiveEquipment = { id: 26931 };
      jest.spyOn(protectiveEquipmentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ protectiveEquipment });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(protectiveEquipmentService.update).toHaveBeenCalled();
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
