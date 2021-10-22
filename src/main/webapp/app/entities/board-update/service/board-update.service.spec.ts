import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { UpdateType } from 'app/entities/enumerations/update-type.model';
import { IBoardUpdate, BoardUpdate } from '../board-update.model';

import { BoardUpdateService } from './board-update.service';

describe('Service Tests', () => {
  describe('BoardUpdate Service', () => {
    let service: BoardUpdateService;
    let httpMock: HttpTestingController;
    let elemDefault: IBoardUpdate;
    let expectedResult: IBoardUpdate | IBoardUpdate[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(BoardUpdateService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        version: 'AAAAAAA',
        path: 'AAAAAAA',
        type: UpdateType.FIRMWARE,
        releaseDate: currentDate,
        status: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            releaseDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a BoardUpdate', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            releaseDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            releaseDate: currentDate,
          },
          returnedFromService
        );

        service.create(new BoardUpdate()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a BoardUpdate', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            version: 'BBBBBB',
            path: 'BBBBBB',
            type: 'BBBBBB',
            releaseDate: currentDate.format(DATE_TIME_FORMAT),
            status: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            releaseDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a BoardUpdate', () => {
        const patchObject = Object.assign(
          {
            path: 'BBBBBB',
            type: 'BBBBBB',
            status: 'BBBBBB',
          },
          new BoardUpdate()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            releaseDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of BoardUpdate', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            version: 'BBBBBB',
            path: 'BBBBBB',
            type: 'BBBBBB',
            releaseDate: currentDate.format(DATE_TIME_FORMAT),
            status: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            releaseDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a BoardUpdate', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addBoardUpdateToCollectionIfMissing', () => {
        it('should add a BoardUpdate to an empty array', () => {
          const boardUpdate: IBoardUpdate = { id: 123 };
          expectedResult = service.addBoardUpdateToCollectionIfMissing([], boardUpdate);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(boardUpdate);
        });

        it('should not add a BoardUpdate to an array that contains it', () => {
          const boardUpdate: IBoardUpdate = { id: 123 };
          const boardUpdateCollection: IBoardUpdate[] = [
            {
              ...boardUpdate,
            },
            { id: 456 },
          ];
          expectedResult = service.addBoardUpdateToCollectionIfMissing(boardUpdateCollection, boardUpdate);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a BoardUpdate to an array that doesn't contain it", () => {
          const boardUpdate: IBoardUpdate = { id: 123 };
          const boardUpdateCollection: IBoardUpdate[] = [{ id: 456 }];
          expectedResult = service.addBoardUpdateToCollectionIfMissing(boardUpdateCollection, boardUpdate);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(boardUpdate);
        });

        it('should add only unique BoardUpdate to an array', () => {
          const boardUpdateArray: IBoardUpdate[] = [{ id: 123 }, { id: 456 }, { id: 64326 }];
          const boardUpdateCollection: IBoardUpdate[] = [{ id: 123 }];
          expectedResult = service.addBoardUpdateToCollectionIfMissing(boardUpdateCollection, ...boardUpdateArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const boardUpdate: IBoardUpdate = { id: 123 };
          const boardUpdate2: IBoardUpdate = { id: 456 };
          expectedResult = service.addBoardUpdateToCollectionIfMissing([], boardUpdate, boardUpdate2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(boardUpdate);
          expect(expectedResult).toContain(boardUpdate2);
        });

        it('should accept null and undefined values', () => {
          const boardUpdate: IBoardUpdate = { id: 123 };
          expectedResult = service.addBoardUpdateToCollectionIfMissing([], null, boardUpdate, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(boardUpdate);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
