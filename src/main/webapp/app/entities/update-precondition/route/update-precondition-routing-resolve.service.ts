import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUpdatePrecondition, UpdatePrecondition } from '../update-precondition.model';
import { UpdatePreconditionService } from '../service/update-precondition.service';

@Injectable({ providedIn: 'root' })
export class UpdatePreconditionRoutingResolveService implements Resolve<IUpdatePrecondition> {
  constructor(protected service: UpdatePreconditionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUpdatePrecondition> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((updatePrecondition: HttpResponse<UpdatePrecondition>) => {
          if (updatePrecondition.body) {
            return of(updatePrecondition.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UpdatePrecondition());
  }
}
