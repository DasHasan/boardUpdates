import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IDownloadUrl, DownloadUrl } from '../download-url.model';

import { DownloadUrlService } from './download-url.service';

describe('Service Tests', () => {
  describe('DownloadUrl Service', () => {
    let service: DownloadUrlService;
    let httpMock: HttpTestingController;
    let elemDefault: IDownloadUrl;
    let expectedResult: IDownloadUrl | IDownloadUrl[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DownloadUrlService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        expirationDate: currentDate,
        url: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            expirationDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a DownloadUrl', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            expirationDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            expirationDate: currentDate,
          },
          returnedFromService
        );

        service.create(new DownloadUrl()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a DownloadUrl', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            expirationDate: currentDate.format(DATE_FORMAT),
            url: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            expirationDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a DownloadUrl', () => {
        const patchObject = Object.assign(
          {
            expirationDate: currentDate.format(DATE_FORMAT),
            url: 'BBBBBB',
          },
          new DownloadUrl()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            expirationDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of DownloadUrl', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            expirationDate: currentDate.format(DATE_FORMAT),
            url: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            expirationDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a DownloadUrl', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDownloadUrlToCollectionIfMissing', () => {
        it('should add a DownloadUrl to an empty array', () => {
          const downloadUrl: IDownloadUrl = { id: 123 };
          expectedResult = service.addDownloadUrlToCollectionIfMissing([], downloadUrl);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(downloadUrl);
        });

        it('should not add a DownloadUrl to an array that contains it', () => {
          const downloadUrl: IDownloadUrl = { id: 123 };
          const downloadUrlCollection: IDownloadUrl[] = [
            {
              ...downloadUrl,
            },
            { id: 456 },
          ];
          expectedResult = service.addDownloadUrlToCollectionIfMissing(downloadUrlCollection, downloadUrl);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a DownloadUrl to an array that doesn't contain it", () => {
          const downloadUrl: IDownloadUrl = { id: 123 };
          const downloadUrlCollection: IDownloadUrl[] = [{ id: 456 }];
          expectedResult = service.addDownloadUrlToCollectionIfMissing(downloadUrlCollection, downloadUrl);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(downloadUrl);
        });

        it('should add only unique DownloadUrl to an array', () => {
          const downloadUrlArray: IDownloadUrl[] = [{ id: 123 }, { id: 456 }, { id: 57461 }];
          const downloadUrlCollection: IDownloadUrl[] = [{ id: 123 }];
          expectedResult = service.addDownloadUrlToCollectionIfMissing(downloadUrlCollection, ...downloadUrlArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const downloadUrl: IDownloadUrl = { id: 123 };
          const downloadUrl2: IDownloadUrl = { id: 456 };
          expectedResult = service.addDownloadUrlToCollectionIfMissing([], downloadUrl, downloadUrl2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(downloadUrl);
          expect(expectedResult).toContain(downloadUrl2);
        });

        it('should accept null and undefined values', () => {
          const downloadUrl: IDownloadUrl = { id: 123 };
          expectedResult = service.addDownloadUrlToCollectionIfMissing([], null, downloadUrl, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(downloadUrl);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
