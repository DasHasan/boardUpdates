jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBoardUpdateSuccessor, BoardUpdateSuccessor } from '../board-update-successor.model';
import { BoardUpdateSuccessorService } from '../service/board-update-successor.service';

import { BoardUpdateSuccessorRoutingResolveService } from './board-update-successor-routing-resolve.service';

describe('Service Tests', () => {
  describe('BoardUpdateSuccessor routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: BoardUpdateSuccessorRoutingResolveService;
    let service: BoardUpdateSuccessorService;
    let resultBoardUpdateSuccessor: IBoardUpdateSuccessor | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(BoardUpdateSuccessorRoutingResolveService);
      service = TestBed.inject(BoardUpdateSuccessorService);
      resultBoardUpdateSuccessor = undefined;
    });

    describe('resolve', () => {
      it('should return IBoardUpdateSuccessor returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBoardUpdateSuccessor = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBoardUpdateSuccessor).toEqual({ id: 123 });
      });

      it('should return new IBoardUpdateSuccessor if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBoardUpdateSuccessor = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultBoardUpdateSuccessor).toEqual(new BoardUpdateSuccessor());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBoardUpdateSuccessor = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBoardUpdateSuccessor).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
