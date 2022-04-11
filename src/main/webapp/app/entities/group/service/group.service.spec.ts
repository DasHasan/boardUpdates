import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGroup, Group } from '../group.model';

import { GroupService } from './group.service';

describe('Service Tests', () => {
  describe('Group Service', () => {
    let service: GroupService;
    let httpMock: HttpTestingController;
    let elemDefault: IGroup;
    let expectedResult: IGroup | IGroup[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(GroupService);
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

      it('should create a Group', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Group()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Group', () => {
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

      it('should partial update a Group', () => {
        const patchObject = Object.assign({}, new Group());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Group', () => {
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

      it('should delete a Group', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addGroupToCollectionIfMissing', () => {
        it('should add a Group to an empty array', () => {
          const group: IGroup = { id: 123 };
          expectedResult = service.addGroupToCollectionIfMissing([], group);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(group);
        });

        it('should not add a Group to an array that contains it', () => {
          const group: IGroup = { id: 123 };
          const groupCollection: IGroup[] = [
            {
              ...group,
            },
            { id: 456 },
          ];
          expectedResult = service.addGroupToCollectionIfMissing(groupCollection, group);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Group to an array that doesn't contain it", () => {
          const group: IGroup = { id: 123 };
          const groupCollection: IGroup[] = [{ id: 456 }];
          expectedResult = service.addGroupToCollectionIfMissing(groupCollection, group);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(group);
        });

        it('should add only unique Group to an array', () => {
          const groupArray: IGroup[] = [{ id: 123 }, { id: 456 }, { id: 52084 }];
          const groupCollection: IGroup[] = [{ id: 123 }];
          expectedResult = service.addGroupToCollectionIfMissing(groupCollection, ...groupArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const group: IGroup = { id: 123 };
          const group2: IGroup = { id: 456 };
          expectedResult = service.addGroupToCollectionIfMissing([], group, group2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(group);
          expect(expectedResult).toContain(group2);
        });

        it('should accept null and undefined values', () => {
          const group: IGroup = { id: 123 };
          expectedResult = service.addGroupToCollectionIfMissing([], null, group, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(group);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
