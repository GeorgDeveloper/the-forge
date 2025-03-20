import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IJobDescription } from '../job-description.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../job-description.test-samples';

import { JobDescriptionService, RestJobDescription } from './job-description.service';

const requireRestSample: RestJobDescription = {
  ...sampleWithRequiredData,
  approvalDate: sampleWithRequiredData.approvalDate?.format(DATE_FORMAT),
};

describe('JobDescription Service', () => {
  let service: JobDescriptionService;
  let httpMock: HttpTestingController;
  let expectedResult: IJobDescription | IJobDescription[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(JobDescriptionService);
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

    it('should create a JobDescription', () => {
      const jobDescription = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(jobDescription).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a JobDescription', () => {
      const jobDescription = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(jobDescription).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a JobDescription', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of JobDescription', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a JobDescription', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addJobDescriptionToCollectionIfMissing', () => {
      it('should add a JobDescription to an empty array', () => {
        const jobDescription: IJobDescription = sampleWithRequiredData;
        expectedResult = service.addJobDescriptionToCollectionIfMissing([], jobDescription);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(jobDescription);
      });

      it('should not add a JobDescription to an array that contains it', () => {
        const jobDescription: IJobDescription = sampleWithRequiredData;
        const jobDescriptionCollection: IJobDescription[] = [
          {
            ...jobDescription,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addJobDescriptionToCollectionIfMissing(jobDescriptionCollection, jobDescription);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a JobDescription to an array that doesn't contain it", () => {
        const jobDescription: IJobDescription = sampleWithRequiredData;
        const jobDescriptionCollection: IJobDescription[] = [sampleWithPartialData];
        expectedResult = service.addJobDescriptionToCollectionIfMissing(jobDescriptionCollection, jobDescription);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(jobDescription);
      });

      it('should add only unique JobDescription to an array', () => {
        const jobDescriptionArray: IJobDescription[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const jobDescriptionCollection: IJobDescription[] = [sampleWithRequiredData];
        expectedResult = service.addJobDescriptionToCollectionIfMissing(jobDescriptionCollection, ...jobDescriptionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const jobDescription: IJobDescription = sampleWithRequiredData;
        const jobDescription2: IJobDescription = sampleWithPartialData;
        expectedResult = service.addJobDescriptionToCollectionIfMissing([], jobDescription, jobDescription2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(jobDescription);
        expect(expectedResult).toContain(jobDescription2);
      });

      it('should accept null and undefined values', () => {
        const jobDescription: IJobDescription = sampleWithRequiredData;
        expectedResult = service.addJobDescriptionToCollectionIfMissing([], null, jobDescription, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(jobDescription);
      });

      it('should return initial array if no JobDescription is added', () => {
        const jobDescriptionCollection: IJobDescription[] = [sampleWithRequiredData];
        expectedResult = service.addJobDescriptionToCollectionIfMissing(jobDescriptionCollection, undefined, null);
        expect(expectedResult).toEqual(jobDescriptionCollection);
      });
    });

    describe('compareJobDescription', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareJobDescription(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 8232 };
        const entity2 = null;

        const compareResult1 = service.compareJobDescription(entity1, entity2);
        const compareResult2 = service.compareJobDescription(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 8232 };
        const entity2 = { id: 2589 };

        const compareResult1 = service.compareJobDescription(entity1, entity2);
        const compareResult2 = service.compareJobDescription(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 8232 };
        const entity2 = { id: 8232 };

        const compareResult1 = service.compareJobDescription(entity1, entity2);
        const compareResult2 = service.compareJobDescription(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
