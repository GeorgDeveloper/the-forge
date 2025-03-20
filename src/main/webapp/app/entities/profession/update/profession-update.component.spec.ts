import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { ProfessionService } from '../service/profession.service';
import { IProfession } from '../profession.model';
import { ProfessionFormService } from './profession-form.service';

import { ProfessionUpdateComponent } from './profession-update.component';

describe('Profession Management Update Component', () => {
  let comp: ProfessionUpdateComponent;
  let fixture: ComponentFixture<ProfessionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let professionFormService: ProfessionFormService;
  let professionService: ProfessionService;
  let employeeService: EmployeeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProfessionUpdateComponent],
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
      .overrideTemplate(ProfessionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProfessionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    professionFormService = TestBed.inject(ProfessionFormService);
    professionService = TestBed.inject(ProfessionService);
    employeeService = TestBed.inject(EmployeeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Employee query and add missing value', () => {
      const profession: IProfession = { id: 30557 };
      const employees: IEmployee[] = [{ id: 1749 }];
      profession.employees = employees;

      const employeeCollection: IEmployee[] = [{ id: 1749 }];
      jest.spyOn(employeeService, 'query').mockReturnValue(of(new HttpResponse({ body: employeeCollection })));
      const additionalEmployees = [...employees];
      const expectedCollection: IEmployee[] = [...additionalEmployees, ...employeeCollection];
      jest.spyOn(employeeService, 'addEmployeeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ profession });
      comp.ngOnInit();

      expect(employeeService.query).toHaveBeenCalled();
      expect(employeeService.addEmployeeToCollectionIfMissing).toHaveBeenCalledWith(
        employeeCollection,
        ...additionalEmployees.map(expect.objectContaining),
      );
      expect(comp.employeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const profession: IProfession = { id: 30557 };
      const employee: IEmployee = { id: 1749 };
      profession.employees = [employee];

      activatedRoute.data = of({ profession });
      comp.ngOnInit();

      expect(comp.employeesSharedCollection).toContainEqual(employee);
      expect(comp.profession).toEqual(profession);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProfession>>();
      const profession = { id: 12610 };
      jest.spyOn(professionFormService, 'getProfession').mockReturnValue(profession);
      jest.spyOn(professionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ profession });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: profession }));
      saveSubject.complete();

      // THEN
      expect(professionFormService.getProfession).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(professionService.update).toHaveBeenCalledWith(expect.objectContaining(profession));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProfession>>();
      const profession = { id: 12610 };
      jest.spyOn(professionFormService, 'getProfession').mockReturnValue({ id: null });
      jest.spyOn(professionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ profession: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: profession }));
      saveSubject.complete();

      // THEN
      expect(professionFormService.getProfession).toHaveBeenCalled();
      expect(professionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProfession>>();
      const profession = { id: 12610 };
      jest.spyOn(professionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ profession });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(professionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEmployee', () => {
      it('Should forward to employeeService', () => {
        const entity = { id: 1749 };
        const entity2 = { id: 1545 };
        jest.spyOn(employeeService, 'compareEmployee');
        comp.compareEmployee(entity, entity2);
        expect(employeeService.compareEmployee).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
