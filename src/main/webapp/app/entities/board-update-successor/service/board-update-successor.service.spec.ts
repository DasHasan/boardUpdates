import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBoardUpdateSuccessor, BoardUpdateSuccessor } from '../board-update-successor.model';

import { BoardUpdateSuccessorService } from './board-update-successor.service';

describe('Service Tests', () => {
  describe('BoardUpdateSuccessor Service', () => {
    let service: BoardUpdateSuccessorService;
    let httpMock: HttpTestingController;
    let elemDefault: IBoardUpdateSuccessor;
    let expectedResult: IBoardUpdateSuccessor | IBoardUpdateSuccessor[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(BoardUpdateSuccessorService);
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

      it('should create a BoardUpdateSuccessor', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new BoardUpdateSuccessor()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a BoardUpdateSuccessor', () => {
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

      it('should partial update a BoardUpdateSuccessor', () => {
        const patchObject = Object.assign({}, new BoardUpdateSuccessor());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of BoardUpdateSuccessor', () => {
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

      it('should delete a BoardUpdateSuccessor', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addBoardUpdateSuccessorToCollectionIfMissing', () => {
        it('should add a BoardUpdateSuccessor to an empty array', () => {
          const boardUpdateSuccessor: IBoardUpdateSuccessor = { id: 123 };
          expectedResult = service.addBoardUpdateSuccessorToCollectionIfMissing([], boardUpdateSuccessor);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(boardUpdateSuccessor);
        });

        it('should not add a BoardUpdateSuccessor to an array that contains it', () => {
          const boardUpdateSuccessor: IBoardUpdateSuccessor = { id: 123 };
          const boardUpdateSuccessorCollection: IBoardUpdateSuccessor[] = [
            {
              ...boardUpdateSuccessor,
            },
            { id: 456 },
          ];
          expectedResult = service.addBoardUpdateSuccessorToCollectionIfMissing(boardUpdateSuccessorCollection, boardUpdateSuccessor);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a BoardUpdateSuccessor to an array that doesn't contain it", () => {
          const boardUpdateSuccessor: IBoardUpdateSuccessor = { id: 123 };
          const boardUpdateSuccessorCollection: IBoardUpdateSuccessor[] = [{ id: 456 }];
          expectedResult = service.addBoardUpdateSuccessorToCollectionIfMissing(boardUpdateSuccessorCollection, boardUpdateSuccessor);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(boardUpdateSuccessor);
        });

        it('should add only unique BoardUpdateSuccessor to an array', () => {
          const boardUpdateSuccessorArray: IBoardUpdateSuccessor[] = [{ id: 123 }, { id: 456 }, { id: 30729 }];
          const boardUpdateSuccessorCollection: IBoardUpdateSuccessor[] = [{ id: 123 }];
          expectedResult = service.addBoardUpdateSuccessorToCollectionIfMissing(
            boardUpdateSuccessorCollection,
            ...boardUpdateSuccessorArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const boardUpdateSuccessor: IBoardUpdateSuccessor = { id: 123 };
          const boardUpdateSuccessor2: IBoardUpdateSuccessor = { id: 456 };
          expectedResult = service.addBoardUpdateSuccessorToCollectionIfMissing([], boardUpdateSuccessor, boardUpdateSuccessor2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(boardUpdateSuccessor);
          expect(expectedResult).toContain(boardUpdateSuccessor2);
        });

        it('should accept null and undefined values', () => {
          const boardUpdateSuccessor: IBoardUpdateSuccessor = { id: 123 };
          expectedResult = service.addBoardUpdateSuccessorToCollectionIfMissing([], null, boardUpdateSuccessor, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(boardUpdateSuccessor);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
