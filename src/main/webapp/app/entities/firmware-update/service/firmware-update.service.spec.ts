import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFirmwareUpdate, FirmwareUpdate } from '../firmware-update.model';

import { FirmwareUpdateService } from './firmware-update.service';

describe('Service Tests', () => {
  describe('FirmwareUpdate Service', () => {
    let service: FirmwareUpdateService;
    let httpMock: HttpTestingController;
    let elemDefault: IFirmwareUpdate;
    let expectedResult: IFirmwareUpdate | IFirmwareUpdate[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FirmwareUpdateService);
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

      it('should create a FirmwareUpdate', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new FirmwareUpdate()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FirmwareUpdate', () => {
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

      it('should partial update a FirmwareUpdate', () => {
        const patchObject = Object.assign(
          {
            active: true,
          },
          new FirmwareUpdate()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of FirmwareUpdate', () => {
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

      it('should delete a FirmwareUpdate', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFirmwareUpdateToCollectionIfMissing', () => {
        it('should add a FirmwareUpdate to an empty array', () => {
          const firmwareUpdate: IFirmwareUpdate = { id: 123 };
          expectedResult = service.addFirmwareUpdateToCollectionIfMissing([], firmwareUpdate);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(firmwareUpdate);
        });

        it('should not add a FirmwareUpdate to an array that contains it', () => {
          const firmwareUpdate: IFirmwareUpdate = { id: 123 };
          const firmwareUpdateCollection: IFirmwareUpdate[] = [
            {
              ...firmwareUpdate,
            },
            { id: 456 },
          ];
          expectedResult = service.addFirmwareUpdateToCollectionIfMissing(firmwareUpdateCollection, firmwareUpdate);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a FirmwareUpdate to an array that doesn't contain it", () => {
          const firmwareUpdate: IFirmwareUpdate = { id: 123 };
          const firmwareUpdateCollection: IFirmwareUpdate[] = [{ id: 456 }];
          expectedResult = service.addFirmwareUpdateToCollectionIfMissing(firmwareUpdateCollection, firmwareUpdate);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(firmwareUpdate);
        });

        it('should add only unique FirmwareUpdate to an array', () => {
          const firmwareUpdateArray: IFirmwareUpdate[] = [{ id: 123 }, { id: 456 }, { id: 1342 }];
          const firmwareUpdateCollection: IFirmwareUpdate[] = [{ id: 123 }];
          expectedResult = service.addFirmwareUpdateToCollectionIfMissing(firmwareUpdateCollection, ...firmwareUpdateArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const firmwareUpdate: IFirmwareUpdate = { id: 123 };
          const firmwareUpdate2: IFirmwareUpdate = { id: 456 };
          expectedResult = service.addFirmwareUpdateToCollectionIfMissing([], firmwareUpdate, firmwareUpdate2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(firmwareUpdate);
          expect(expectedResult).toContain(firmwareUpdate2);
        });

        it('should accept null and undefined values', () => {
          const firmwareUpdate: IFirmwareUpdate = { id: 123 };
          expectedResult = service.addFirmwareUpdateToCollectionIfMissing([], null, firmwareUpdate, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(firmwareUpdate);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
