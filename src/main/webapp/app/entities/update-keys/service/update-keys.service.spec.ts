import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUpdateKeys, UpdateKeys } from '../update-keys.model';

import { UpdateKeysService } from './update-keys.service';

describe('Service Tests', () => {
  describe('UpdateKeys Service', () => {
    let service: UpdateKeysService;
    let httpMock: HttpTestingController;
    let elemDefault: IUpdateKeys;
    let expectedResult: IUpdateKeys | IUpdateKeys[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(UpdateKeysService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        key: 'AAAAAAA',
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

      it('should create a UpdateKeys', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new UpdateKeys()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a UpdateKeys', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            key: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a UpdateKeys', () => {
        const patchObject = Object.assign(
          {
            key: 'BBBBBB',
          },
          new UpdateKeys()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of UpdateKeys', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            key: 'BBBBBB',
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

      it('should delete a UpdateKeys', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addUpdateKeysToCollectionIfMissing', () => {
        it('should add a UpdateKeys to an empty array', () => {
          const updateKeys: IUpdateKeys = { id: 123 };
          expectedResult = service.addUpdateKeysToCollectionIfMissing([], updateKeys);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(updateKeys);
        });

        it('should not add a UpdateKeys to an array that contains it', () => {
          const updateKeys: IUpdateKeys = { id: 123 };
          const updateKeysCollection: IUpdateKeys[] = [
            {
              ...updateKeys,
            },
            { id: 456 },
          ];
          expectedResult = service.addUpdateKeysToCollectionIfMissing(updateKeysCollection, updateKeys);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a UpdateKeys to an array that doesn't contain it", () => {
          const updateKeys: IUpdateKeys = { id: 123 };
          const updateKeysCollection: IUpdateKeys[] = [{ id: 456 }];
          expectedResult = service.addUpdateKeysToCollectionIfMissing(updateKeysCollection, updateKeys);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(updateKeys);
        });

        it('should add only unique UpdateKeys to an array', () => {
          const updateKeysArray: IUpdateKeys[] = [{ id: 123 }, { id: 456 }, { id: 59402 }];
          const updateKeysCollection: IUpdateKeys[] = [{ id: 123 }];
          expectedResult = service.addUpdateKeysToCollectionIfMissing(updateKeysCollection, ...updateKeysArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const updateKeys: IUpdateKeys = { id: 123 };
          const updateKeys2: IUpdateKeys = { id: 456 };
          expectedResult = service.addUpdateKeysToCollectionIfMissing([], updateKeys, updateKeys2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(updateKeys);
          expect(expectedResult).toContain(updateKeys2);
        });

        it('should accept null and undefined values', () => {
          const updateKeys: IUpdateKeys = { id: 123 };
          expectedResult = service.addUpdateKeysToCollectionIfMissing([], null, updateKeys, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(updateKeys);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
