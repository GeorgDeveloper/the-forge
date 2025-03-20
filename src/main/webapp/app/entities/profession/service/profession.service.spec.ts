import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IProfession } from '../profession.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../profession.test-samples';

import { ProfessionService } from './profession.service';

const requireRestSample: IProfession = {
  ...sampleWithRequiredData,
};

describe('Profession Service', () => {
  let service: ProfessionService;
  let httpMock: HttpTestingController;
  let expectedResult: IProfession | IProfession[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProfessionService);
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

    it('should create a Profession', () => {
      const profession = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(profession).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Profession', () => {
      const profession = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(profession).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Profession', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Profession', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Profession', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProfessionToCollectionIfMissing', () => {
      it('should add a Profession to an empty array', () => {
        const profession: IProfession = sampleWithRequiredData;
        expectedResult = service.addProfessionToCollectionIfMissing([], profession);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(profession);
      });

      it('should not add a Profession to an array that contains it', () => {
        const profession: IProfession = sampleWithRequiredData;
        const professionCollection: IProfession[] = [
          {
            ...profession,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProfessionToCollectionIfMissing(professionCollection, profession);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Profession to an array that doesn't contain it", () => {
        const profession: IProfession = sampleWithRequiredData;
        const professionCollection: IProfession[] = [sampleWithPartialData];
        expectedResult = service.addProfessionToCollectionIfMissing(professionCollection, profession);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(profession);
      });

      it('should add only unique Profession to an array', () => {
        const professionArray: IProfession[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const professionCollection: IProfession[] = [sampleWithRequiredData];
        expectedResult = service.addProfessionToCollectionIfMissing(professionCollection, ...professionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const profession: IProfession = sampleWithRequiredData;
        const profession2: IProfession = sampleWithPartialData;
        expectedResult = service.addProfessionToCollectionIfMissing([], profession, profession2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(profession);
        expect(expectedResult).toContain(profession2);
      });

      it('should accept null and undefined values', () => {
        const profession: IProfession = sampleWithRequiredData;
        expectedResult = service.addProfessionToCollectionIfMissing([], null, profession, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(profession);
      });

      it('should return initial array if no Profession is added', () => {
        const professionCollection: IProfession[] = [sampleWithRequiredData];
        expectedResult = service.addProfessionToCollectionIfMissing(professionCollection, undefined, null);
        expect(expectedResult).toEqual(professionCollection);
      });
    });

    describe('compareProfession', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProfession(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 12610 };
        const entity2 = null;

        const compareResult1 = service.compareProfession(entity1, entity2);
        const compareResult2 = service.compareProfession(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 12610 };
        const entity2 = { id: 30557 };

        const compareResult1 = service.compareProfession(entity1, entity2);
        const compareResult2 = service.compareProfession(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 12610 };
        const entity2 = { id: 12610 };

        const compareResult1 = service.compareProfession(entity1, entity2);
        const compareResult2 = service.compareProfession(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
