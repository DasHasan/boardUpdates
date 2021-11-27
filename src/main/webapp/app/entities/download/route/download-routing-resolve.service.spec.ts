jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDownload, Download } from '../download.model';
import { DownloadService } from '../service/download.service';

import { DownloadRoutingResolveService } from './download-routing-resolve.service';

describe('Service Tests', () => {
  describe('Download routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DownloadRoutingResolveService;
    let service: DownloadService;
    let resultDownload: IDownload | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DownloadRoutingResolveService);
      service = TestBed.inject(DownloadService);
      resultDownload = undefined;
    });

    describe('resolve', () => {
      it('should return IDownload returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDownload = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDownload).toEqual({ id: 123 });
      });

      it('should return new IDownload if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDownload = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDownload).toEqual(new Download());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDownload = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDownload).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
