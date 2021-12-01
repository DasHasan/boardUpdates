jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IUpdateKeys, UpdateKeys } from '../update-keys.model';
import { UpdateKeysService } from '../service/update-keys.service';

import { UpdateKeysRoutingResolveService } from './update-keys-routing-resolve.service';

describe('Service Tests', () => {
  describe('UpdateKeys routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: UpdateKeysRoutingResolveService;
    let service: UpdateKeysService;
    let resultUpdateKeys: IUpdateKeys | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(UpdateKeysRoutingResolveService);
      service = TestBed.inject(UpdateKeysService);
      resultUpdateKeys = undefined;
    });

    describe('resolve', () => {
      it('should return IUpdateKeys returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUpdateKeys = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultUpdateKeys).toEqual({ id: 123 });
      });

      it('should return new IUpdateKeys if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUpdateKeys = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultUpdateKeys).toEqual(new UpdateKeys());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultUpdateKeys = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultUpdateKeys).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
