import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAdditionalTraining } from '../additional-training.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../additional-training.test-samples';

import { AdditionalTrainingService, RestAdditionalTraining } from './additional-training.service';

const requireRestSample: RestAdditionalTraining = {
  ...sampleWithRequiredData,
  trainingDate: sampleWithRequiredData.trainingDate?.format(DATE_FORMAT),
  nextTrainingDate: sampleWithRequiredData.nextTrainingDate?.format(DATE_FORMAT),
};

describe('AdditionalTraining Service', () => {
  let service: AdditionalTrainingService;
  let httpMock: HttpTestingController;
  let expectedResult: IAdditionalTraining | IAdditionalTraining[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AdditionalTrainingService);
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

    it('should create a AdditionalTraining', () => {
      const additionalTraining = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(additionalTraining).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AdditionalTraining', () => {
      const additionalTraining = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(additionalTraining).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AdditionalTraining', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AdditionalTraining', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AdditionalTraining', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAdditionalTrainingToCollectionIfMissing', () => {
      it('should add a AdditionalTraining to an empty array', () => {
        const additionalTraining: IAdditionalTraining = sampleWithRequiredData;
        expectedResult = service.addAdditionalTrainingToCollectionIfMissing([], additionalTraining);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(additionalTraining);
      });

      it('should not add a AdditionalTraining to an array that contains it', () => {
        const additionalTraining: IAdditionalTraining = sampleWithRequiredData;
        const additionalTrainingCollection: IAdditionalTraining[] = [
          {
            ...additionalTraining,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAdditionalTrainingToCollectionIfMissing(additionalTrainingCollection, additionalTraining);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AdditionalTraining to an array that doesn't contain it", () => {
        const additionalTraining: IAdditionalTraining = sampleWithRequiredData;
        const additionalTrainingCollection: IAdditionalTraining[] = [sampleWithPartialData];
        expectedResult = service.addAdditionalTrainingToCollectionIfMissing(additionalTrainingCollection, additionalTraining);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(additionalTraining);
      });

      it('should add only unique AdditionalTraining to an array', () => {
        const additionalTrainingArray: IAdditionalTraining[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const additionalTrainingCollection: IAdditionalTraining[] = [sampleWithRequiredData];
        expectedResult = service.addAdditionalTrainingToCollectionIfMissing(additionalTrainingCollection, ...additionalTrainingArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const additionalTraining: IAdditionalTraining = sampleWithRequiredData;
        const additionalTraining2: IAdditionalTraining = sampleWithPartialData;
        expectedResult = service.addAdditionalTrainingToCollectionIfMissing([], additionalTraining, additionalTraining2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(additionalTraining);
        expect(expectedResult).toContain(additionalTraining2);
      });

      it('should accept null and undefined values', () => {
        const additionalTraining: IAdditionalTraining = sampleWithRequiredData;
        expectedResult = service.addAdditionalTrainingToCollectionIfMissing([], null, additionalTraining, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(additionalTraining);
      });

      it('should return initial array if no AdditionalTraining is added', () => {
        const additionalTrainingCollection: IAdditionalTraining[] = [sampleWithRequiredData];
        expectedResult = service.addAdditionalTrainingToCollectionIfMissing(additionalTrainingCollection, undefined, null);
        expect(expectedResult).toEqual(additionalTrainingCollection);
      });
    });

    describe('compareAdditionalTraining', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAdditionalTraining(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 2143 };
        const entity2 = null;

        const compareResult1 = service.compareAdditionalTraining(entity1, entity2);
        const compareResult2 = service.compareAdditionalTraining(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 2143 };
        const entity2 = { id: 25601 };

        const compareResult1 = service.compareAdditionalTraining(entity1, entity2);
        const compareResult2 = service.compareAdditionalTraining(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 2143 };
        const entity2 = { id: 2143 };

        const compareResult1 = service.compareAdditionalTraining(entity1, entity2);
        const compareResult2 = service.compareAdditionalTraining(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
