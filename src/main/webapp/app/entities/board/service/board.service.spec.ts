import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBoard, Board } from '../board.model';

import { BoardService } from './board.service';

describe('Service Tests', () => {
  describe('Board Service', () => {
    let service: BoardService;
    let httpMock: HttpTestingController;
    let elemDefault: IBoard;
    let expectedResult: IBoard | IBoard[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(BoardService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        serial: 'AAAAAAA',
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

      it('should create a Board', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Board()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Board', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            serial: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Board', () => {
        const patchObject = Object.assign(
          {
            serial: 'BBBBBB',
          },
          new Board()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Board', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            serial: 'BBBBBB',
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

      it('should delete a Board', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addBoardToCollectionIfMissing', () => {
        it('should add a Board to an empty array', () => {
          const board: IBoard = { id: 123 };
          expectedResult = service.addBoardToCollectionIfMissing([], board);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(board);
        });

        it('should not add a Board to an array that contains it', () => {
          const board: IBoard = { id: 123 };
          const boardCollection: IBoard[] = [
            {
              ...board,
            },
            { id: 456 },
          ];
          expectedResult = service.addBoardToCollectionIfMissing(boardCollection, board);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Board to an array that doesn't contain it", () => {
          const board: IBoard = { id: 123 };
          const boardCollection: IBoard[] = [{ id: 456 }];
          expectedResult = service.addBoardToCollectionIfMissing(boardCollection, board);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(board);
        });

        it('should add only unique Board to an array', () => {
          const boardArray: IBoard[] = [{ id: 123 }, { id: 456 }, { id: 95084 }];
          const boardCollection: IBoard[] = [{ id: 123 }];
          expectedResult = service.addBoardToCollectionIfMissing(boardCollection, ...boardArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const board: IBoard = { id: 123 };
          const board2: IBoard = { id: 456 };
          expectedResult = service.addBoardToCollectionIfMissing([], board, board2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(board);
          expect(expectedResult).toContain(board2);
        });

        it('should accept null and undefined values', () => {
          const board: IBoard = { id: 123 };
          expectedResult = service.addBoardToCollectionIfMissing([], null, board, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(board);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
