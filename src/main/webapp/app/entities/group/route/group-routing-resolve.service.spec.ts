jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IGroup, Group } from '../group.model';
import { GroupService } from '../service/group.service';

import { GroupRoutingResolveService } from './group-routing-resolve.service';

describe('Service Tests', () => {
  describe('Group routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: GroupRoutingResolveService;
    let service: GroupService;
    let resultGroup: IGroup | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(GroupRoutingResolveService);
      service = TestBed.inject(GroupService);
      resultGroup = undefined;
    });

    describe('resolve', () => {
      it('should return IGroup returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultGroup = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultGroup).toEqual({ id: 123 });
      });

      it('should return new IGroup if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultGroup = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultGroup).toEqual(new Group());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultGroup = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultGroup).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
