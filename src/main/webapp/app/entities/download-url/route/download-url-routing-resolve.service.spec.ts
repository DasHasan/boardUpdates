jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDownloadUrl, DownloadUrl } from '../download-url.model';
import { DownloadUrlService } from '../service/download-url.service';

import { DownloadUrlRoutingResolveService } from './download-url-routing-resolve.service';

describe('Service Tests', () => {
  describe('DownloadUrl routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DownloadUrlRoutingResolveService;
    let service: DownloadUrlService;
    let resultDownloadUrl: IDownloadUrl | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DownloadUrlRoutingResolveService);
      service = TestBed.inject(DownloadUrlService);
      resultDownloadUrl = undefined;
    });

    describe('resolve', () => {
      it('should return IDownloadUrl returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDownloadUrl = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDownloadUrl).toEqual({ id: 123 });
      });

      it('should return new IDownloadUrl if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDownloadUrl = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDownloadUrl).toEqual(new DownloadUrl());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDownloadUrl = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDownloadUrl).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
