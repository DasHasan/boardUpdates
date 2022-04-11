jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBoardUpdate, BoardUpdate } from '../board-update.model';
import { BoardUpdateService } from '../service/board-update.service';

import { BoardUpdateRoutingResolveService } from './board-update-routing-resolve.service';

describe('Service Tests', () => {
  describe('BoardUpdate routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: BoardUpdateRoutingResolveService;
    let service: BoardUpdateService;
    let resultBoardUpdate: IBoardUpdate | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(BoardUpdateRoutingResolveService);
      service = TestBed.inject(BoardUpdateService);
      resultBoardUpdate = undefined;
    });

    describe('resolve', () => {
      it('should return IBoardUpdate returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBoardUpdate = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBoardUpdate).toEqual({ id: 123 });
      });

      it('should return new IBoardUpdate if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBoardUpdate = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultBoardUpdate).toEqual(new BoardUpdate());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBoardUpdate = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBoardUpdate).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
