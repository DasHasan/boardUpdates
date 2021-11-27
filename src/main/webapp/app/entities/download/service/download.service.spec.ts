import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDownload, Download } from '../download.model';

import { DownloadService } from './download.service';

describe('Service Tests', () => {
  describe('Download Service', () => {
    let service: DownloadService;
    let httpMock: HttpTestingController;
    let elemDefault: IDownload;
    let expectedResult: IDownload | IDownload[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DownloadService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        date: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            date: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Download', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            date: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.create(new Download()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Download', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            date: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Download', () => {
        const patchObject = Object.assign({}, new Download());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Download', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            date: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Download', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDownloadToCollectionIfMissing', () => {
        it('should add a Download to an empty array', () => {
          const download: IDownload = { id: 123 };
          expectedResult = service.addDownloadToCollectionIfMissing([], download);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(download);
        });

        it('should not add a Download to an array that contains it', () => {
          const download: IDownload = { id: 123 };
          const downloadCollection: IDownload[] = [
            {
              ...download,
            },
            { id: 456 },
          ];
          expectedResult = service.addDownloadToCollectionIfMissing(downloadCollection, download);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Download to an array that doesn't contain it", () => {
          const download: IDownload = { id: 123 };
          const downloadCollection: IDownload[] = [{ id: 456 }];
          expectedResult = service.addDownloadToCollectionIfMissing(downloadCollection, download);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(download);
        });

        it('should add only unique Download to an array', () => {
          const downloadArray: IDownload[] = [{ id: 123 }, { id: 456 }, { id: 84532 }];
          const downloadCollection: IDownload[] = [{ id: 123 }];
          expectedResult = service.addDownloadToCollectionIfMissing(downloadCollection, ...downloadArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const download: IDownload = { id: 123 };
          const download2: IDownload = { id: 456 };
          expectedResult = service.addDownloadToCollectionIfMissing([], download, download2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(download);
          expect(expectedResult).toContain(download2);
        });

        it('should accept null and undefined values', () => {
          const download: IDownload = { id: 123 };
          expectedResult = service.addDownloadToCollectionIfMissing([], null, download, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(download);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
