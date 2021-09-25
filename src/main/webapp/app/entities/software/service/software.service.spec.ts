import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISoftware, Software } from '../software.model';

import { SoftwareService } from './software.service';

describe('Service Tests', () => {
  describe('Software Service', () => {
    let service: SoftwareService;
    let httpMock: HttpTestingController;
    let elemDefault: ISoftware;
    let expectedResult: ISoftware | ISoftware[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SoftwareService);
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

      it('should create a Software', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Software()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Software', () => {
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

      it('should partial update a Software', () => {
        const patchObject = Object.assign(
          {
            path: 'BBBBBB',
          },
          new Software()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Software', () => {
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

      it('should delete a Software', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSoftwareToCollectionIfMissing', () => {
        it('should add a Software to an empty array', () => {
          const software: ISoftware = { id: 123 };
          expectedResult = service.addSoftwareToCollectionIfMissing([], software);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(software);
        });

        it('should not add a Software to an array that contains it', () => {
          const software: ISoftware = { id: 123 };
          const softwareCollection: ISoftware[] = [
            {
              ...software,
            },
            { id: 456 },
          ];
          expectedResult = service.addSoftwareToCollectionIfMissing(softwareCollection, software);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Software to an array that doesn't contain it", () => {
          const software: ISoftware = { id: 123 };
          const softwareCollection: ISoftware[] = [{ id: 456 }];
          expectedResult = service.addSoftwareToCollectionIfMissing(softwareCollection, software);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(software);
        });

        it('should add only unique Software to an array', () => {
          const softwareArray: ISoftware[] = [{ id: 123 }, { id: 456 }, { id: 54120 }];
          const softwareCollection: ISoftware[] = [{ id: 123 }];
          expectedResult = service.addSoftwareToCollectionIfMissing(softwareCollection, ...softwareArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const software: ISoftware = { id: 123 };
          const software2: ISoftware = { id: 456 };
          expectedResult = service.addSoftwareToCollectionIfMissing([], software, software2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(software);
          expect(expectedResult).toContain(software2);
        });

        it('should accept null and undefined values', () => {
          const software: ISoftware = { id: 123 };
          expectedResult = service.addSoftwareToCollectionIfMissing([], null, software, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(software);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
