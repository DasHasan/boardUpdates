jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISoftwareUpdate, SoftwareUpdate } from '../software-update.model';
import { SoftwareUpdateService } from '../service/software-update.service';

import { SoftwareUpdateRoutingResolveService } from './software-update-routing-resolve.service';

describe('Service Tests', () => {
  describe('SoftwareUpdate routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SoftwareUpdateRoutingResolveService;
    let service: SoftwareUpdateService;
    let resultSoftwareUpdate: ISoftwareUpdate | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SoftwareUpdateRoutingResolveService);
      service = TestBed.inject(SoftwareUpdateService);
      resultSoftwareUpdate = undefined;
    });

    describe('resolve', () => {
      it('should return ISoftwareUpdate returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSoftwareUpdate = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSoftwareUpdate).toEqual({ id: 123 });
      });

      it('should return new ISoftwareUpdate if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSoftwareUpdate = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSoftwareUpdate).toEqual(new SoftwareUpdate());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSoftwareUpdate = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSoftwareUpdate).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
