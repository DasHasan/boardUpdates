import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGroup, Group } from '../group.model';
import { GroupService } from '../service/group.service';

@Injectable({ providedIn: 'root' })
export class GroupRoutingResolveService implements Resolve<IGroup> {
  constructor(protected service: GroupService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGroup> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((group: HttpResponse<Group>) => {
          if (group.body) {
            return of(group.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Group());
  }
}
