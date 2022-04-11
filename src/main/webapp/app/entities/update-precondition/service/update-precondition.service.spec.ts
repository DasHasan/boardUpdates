import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUpdatePrecondition, UpdatePrecondition } from '../update-precondition.model';

import { UpdatePreconditionService } from './update-precondition.service';

describe('Service Tests', () => {
  describe('UpdatePrecondition Service', () => {
    let service: UpdatePreconditionService;
    let httpMock: HttpTestingController;
    let elemDefault: IUpdatePrecondition;
    let expectedResult: IUpdatePrecondition | IUpdatePrecondition[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(UpdatePreconditionService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a UpdatePrecondition', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new UpdatePrecondition()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a UpdatePrecondition', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a UpdatePrecondition', () => {
        const patchObject = Object.assign({}, new UpdatePrecondition());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of UpdatePrecondition', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a UpdatePrecondition', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addUpdatePreconditionToCollectionIfMissing', () => {
        it('should add a UpdatePrecondition to an empty array', () => {
          const updatePrecondition: IUpdatePrecondition = { id: 123 };
          expectedResult = service.addUpdatePreconditionToCollectionIfMissing([], updatePrecondition);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(updatePrecondition);
        });

        it('should not add a UpdatePrecondition to an array that contains it', () => {
          const updatePrecondition: IUpdatePrecondition = { id: 123 };
          const updatePreconditionCollection: IUpdatePrecondition[] = [
            {
              ...updatePrecondition,
            },
            { id: 456 },
          ];
          expectedResult = service.addUpdatePreconditionToCollectionIfMissing(updatePreconditionCollection, updatePrecondition);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a UpdatePrecondition to an array that doesn't contain it", () => {
          const updatePrecondition: IUpdatePrecondition = { id: 123 };
          const updatePreconditionCollection: IUpdatePrecondition[] = [{ id: 456 }];
          expectedResult = service.addUpdatePreconditionToCollectionIfMissing(updatePreconditionCollection, updatePrecondition);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(updatePrecondition);
        });

        it('should add only unique UpdatePrecondition to an array', () => {
          const updatePreconditionArray: IUpdatePrecondition[] = [{ id: 123 }, { id: 456 }, { id: 57865 }];
          const updatePreconditionCollection: IUpdatePrecondition[] = [{ id: 123 }];
          expectedResult = service.addUpdatePreconditionToCollectionIfMissing(updatePreconditionCollection, ...updatePreconditionArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const updatePrecondition: IUpdatePrecondition = { id: 123 };
          const updatePrecondition2: IUpdatePrecondition = { id: 456 };
          expectedResult = service.addUpdatePreconditionToCollectionIfMissing([], updatePrecondition, updatePrecondition2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(updatePrecondition);
          expect(expectedResult).toContain(updatePrecondition2);
        });

        it('should accept null and undefined values', () => {
          const updatePrecondition: IUpdatePrecondition = { id: 123 };
          expectedResult = service.addUpdatePreconditionToCollectionIfMissing([], null, updatePrecondition, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(updatePrecondition);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
