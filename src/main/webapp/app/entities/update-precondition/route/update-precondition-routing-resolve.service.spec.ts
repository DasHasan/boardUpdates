jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IUpdatePrecondition, UpdatePrecondition } from '../update-precondition.model';
import { UpdatePreconditionService } from '../service/update-precondition.service';

import { UpdatePreconditionRoutingResolveService } from './update-precondition-routing-resolve.service';

describe('Service Tests', () => {
  describe('UpdatePrecondition routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: UpdatePreconditionRoutingResolveService;
    let service: UpdatePreconditionService;
    let resultUpdatePrecondition: IUpdatePrecondition | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(UpdatePreconditionRoutingResolveService);
      service = TestBed.inject(UpdatePreconditionService);
      resultUpdatePrecondition = undefined;
    });

    describe('resolve', () => {
      it('should return IUpdatePrecondition returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUpdatePrecondition = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultUpdatePrecondition).toEqual({ id: 123 });
      });

      it('should return new IUpdatePrecondition if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUpdatePrecondition = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultUpdatePrecondition).toEqual(new UpdatePrecondition());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUpdatePrecondition = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultUpdatePrecondition).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
