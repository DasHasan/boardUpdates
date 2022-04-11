import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUpdateVersionPrecondition, UpdateVersionPrecondition } from '../update-version-precondition.model';
import { UpdateVersionPreconditionService } from '../service/update-version-precondition.service';

@Injectable({ providedIn: 'root' })
export class UpdateVersionPreconditionRoutingResolveService implements Resolve<IUpdateVersionPrecondition> {
  constructor(protected service: UpdateVersionPreconditionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUpdateVersionPrecondition> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((updateVersionPrecondition: HttpResponse<UpdateVersionPrecondition>) => {
          if (updateVersionPrecondition.body) {
            return of(updateVersionPrecondition.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UpdateVersionPrecondition());
  }
}
