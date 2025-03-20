import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISafetyInstruction } from '../safety-instruction.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../safety-instruction.test-samples';

import { RestSafetyInstruction, SafetyInstructionService } from './safety-instruction.service';

const requireRestSample: RestSafetyInstruction = {
  ...sampleWithRequiredData,
  introductionDate: sampleWithRequiredData.introductionDate?.format(DATE_FORMAT),
};

describe('SafetyInstruction Service', () => {
  let service: SafetyInstructionService;
  let httpMock: HttpTestingController;
  let expectedResult: ISafetyInstruction | ISafetyInstruction[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SafetyInstructionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a SafetyInstruction', () => {
      const safetyInstruction = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(safetyInstruction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SafetyInstruction', () => {
      const safetyInstruction = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(safetyInstruction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SafetyInstruction', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SafetyInstruction', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SafetyInstruction', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSafetyInstructionToCollectionIfMissing', () => {
      it('should add a SafetyInstruction to an empty array', () => {
        const safetyInstruction: ISafetyInstruction = sampleWithRequiredData;
        expectedResult = service.addSafetyInstructionToCollectionIfMissing([], safetyInstruction);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(safetyInstruction);
      });

      it('should not add a SafetyInstruction to an array that contains it', () => {
        const safetyInstruction: ISafetyInstruction = sampleWithRequiredData;
        const safetyInstructionCollection: ISafetyInstruction[] = [
          {
            ...safetyInstruction,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSafetyInstructionToCollectionIfMissing(safetyInstructionCollection, safetyInstruction);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SafetyInstruction to an array that doesn't contain it", () => {
        const safetyInstruction: ISafetyInstruction = sampleWithRequiredData;
        const safetyInstructionCollection: ISafetyInstruction[] = [sampleWithPartialData];
        expectedResult = service.addSafetyInstructionToCollectionIfMissing(safetyInstructionCollection, safetyInstruction);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(safetyInstruction);
      });

      it('should add only unique SafetyInstruction to an array', () => {
        const safetyInstructionArray: ISafetyInstruction[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const safetyInstructionCollection: ISafetyInstruction[] = [sampleWithRequiredData];
        expectedResult = service.addSafetyInstructionToCollectionIfMissing(safetyInstructionCollection, ...safetyInstructionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const safetyInstruction: ISafetyInstruction = sampleWithRequiredData;
        const safetyInstruction2: ISafetyInstruction = sampleWithPartialData;
        expectedResult = service.addSafetyInstructionToCollectionIfMissing([], safetyInstruction, safetyInstruction2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(safetyInstruction);
        expect(expectedResult).toContain(safetyInstruction2);
      });

      it('should accept null and undefined values', () => {
        const safetyInstruction: ISafetyInstruction = sampleWithRequiredData;
        expectedResult = service.addSafetyInstructionToCollectionIfMissing([], null, safetyInstruction, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(safetyInstruction);
      });

      it('should return initial array if no SafetyInstruction is added', () => {
        const safetyInstructionCollection: ISafetyInstruction[] = [sampleWithRequiredData];
        expectedResult = service.addSafetyInstructionToCollectionIfMissing(safetyInstructionCollection, undefined, null);
        expect(expectedResult).toEqual(safetyInstructionCollection);
      });
    });

    describe('compareSafetyInstruction', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSafetyInstruction(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 5402 };
        const entity2 = null;

        const compareResult1 = service.compareSafetyInstruction(entity1, entity2);
        const compareResult2 = service.compareSafetyInstruction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 5402 };
        const entity2 = { id: 9685 };

        const compareResult1 = service.compareSafetyInstruction(entity1, entity2);
        const compareResult2 = service.compareSafetyInstruction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 5402 };
        const entity2 = { id: 5402 };

        const compareResult1 = service.compareSafetyInstruction(entity1, entity2);
        const compareResult2 = service.compareSafetyInstruction(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
