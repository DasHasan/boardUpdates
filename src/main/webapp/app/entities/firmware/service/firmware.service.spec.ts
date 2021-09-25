import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFirmware, Firmware } from '../firmware.model';

import { FirmwareService } from './firmware.service';

describe('Service Tests', () => {
  describe('Firmware Service', () => {
    let service: FirmwareService;
    let httpMock: HttpTestingController;
    let elemDefault: IFirmware;
    let expectedResult: IFirmware | IFirmware[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FirmwareService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        version: 'AAAAAAA',
        path: 'AAAAAAA',
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

      it('should create a Firmware', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Firmware()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Firmware', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            version: 'BBBBBB',
            path: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Firmware', () => {
        const patchObject = Object.assign(
          {
            path: 'BBBBBB',
          },
          new Firmware()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Firmware', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            version: 'BBBBBB',
            path: 'BBBBBB',
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

      it('should delete a Firmware', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFirmwareToCollectionIfMissing', () => {
        it('should add a Firmware to an empty array', () => {
          const firmware: IFirmware = { id: 123 };
          expectedResult = service.addFirmwareToCollectionIfMissing([], firmware);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(firmware);
        });

        it('should not add a Firmware to an array that contains it', () => {
          const firmware: IFirmware = { id: 123 };
          const firmwareCollection: IFirmware[] = [
            {
              ...firmware,
            },
            { id: 456 },
          ];
          expectedResult = service.addFirmwareToCollectionIfMissing(firmwareCollection, firmware);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Firmware to an array that doesn't contain it", () => {
          const firmware: IFirmware = { id: 123 };
          const firmwareCollection: IFirmware[] = [{ id: 456 }];
          expectedResult = service.addFirmwareToCollectionIfMissing(firmwareCollection, firmware);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(firmware);
        });

        it('should add only unique Firmware to an array', () => {
          const firmwareArray: IFirmware[] = [{ id: 123 }, { id: 456 }, { id: 53387 }];
          const firmwareCollection: IFirmware[] = [{ id: 123 }];
          expectedResult = service.addFirmwareToCollectionIfMissing(firmwareCollection, ...firmwareArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const firmware: IFirmware = { id: 123 };
          const firmware2: IFirmware = { id: 456 };
          expectedResult = service.addFirmwareToCollectionIfMissing([], firmware, firmware2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(firmware);
          expect(expectedResult).toContain(firmware2);
        });

        it('should accept null and undefined values', () => {
          const firmware: IFirmware = { id: 123 };
          expectedResult = service.addFirmwareToCollectionIfMissing([], null, firmware, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(firmware);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
