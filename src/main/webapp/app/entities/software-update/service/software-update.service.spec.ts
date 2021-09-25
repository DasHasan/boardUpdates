import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISoftwareUpdate, SoftwareUpdate } from '../software-update.model';

import { SoftwareUpdateService } from './software-update.service';

describe('Service Tests', () => {
  describe('SoftwareUpdate Service', () => {
    let service: SoftwareUpdateService;
    let httpMock: HttpTestingController;
    let elemDefault: ISoftwareUpdate;
    let expectedResult: ISoftwareUpdate | ISoftwareUpdate[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SoftwareUpdateService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        active: false,
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

      it('should create a SoftwareUpdate', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new SoftwareUpdate()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SoftwareUpdate', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            active: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a SoftwareUpdate', () => {
        const patchObject = Object.assign({}, new SoftwareUpdate());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of SoftwareUpdate', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            active: true,
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

      it('should delete a SoftwareUpdate', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSoftwareUpdateToCollectionIfMissing', () => {
        it('should add a SoftwareUpdate to an empty array', () => {
          const softwareUpdate: ISoftwareUpdate = { id: 123 };
          expectedResult = service.addSoftwareUpdateToCollectionIfMissing([], softwareUpdate);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(softwareUpdate);
        });

        it('should not add a SoftwareUpdate to an array that contains it', () => {
          const softwareUpdate: ISoftwareUpdate = { id: 123 };
          const softwareUpdateCollection: ISoftwareUpdate[] = [
            {
              ...softwareUpdate,
            },
            { id: 456 },
          ];
          expectedResult = service.addSoftwareUpdateToCollectionIfMissing(softwareUpdateCollection, softwareUpdate);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SoftwareUpdate to an array that doesn't contain it", () => {
          const softwareUpdate: ISoftwareUpdate = { id: 123 };
          const softwareUpdateCollection: ISoftwareUpdate[] = [{ id: 456 }];
          expectedResult = service.addSoftwareUpdateToCollectionIfMissing(softwareUpdateCollection, softwareUpdate);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(softwareUpdate);
        });

        it('should add only unique SoftwareUpdate to an array', () => {
          const softwareUpdateArray: ISoftwareUpdate[] = [{ id: 123 }, { id: 456 }, { id: 66751 }];
          const softwareUpdateCollection: ISoftwareUpdate[] = [{ id: 123 }];
          expectedResult = service.addSoftwareUpdateToCollectionIfMissing(softwareUpdateCollection, ...softwareUpdateArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const softwareUpdate: ISoftwareUpdate = { id: 123 };
          const softwareUpdate2: ISoftwareUpdate = { id: 456 };
          expectedResult = service.addSoftwareUpdateToCollectionIfMissing([], softwareUpdate, softwareUpdate2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(softwareUpdate);
          expect(expectedResult).toContain(softwareUpdate2);
        });

        it('should accept null and undefined values', () => {
          const softwareUpdate: ISoftwareUpdate = { id: 123 };
          expectedResult = service.addSoftwareUpdateToCollectionIfMissing([], null, softwareUpdate, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(softwareUpdate);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
