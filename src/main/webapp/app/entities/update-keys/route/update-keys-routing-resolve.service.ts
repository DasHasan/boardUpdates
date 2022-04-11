import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUpdateKeys, UpdateKeys } from '../update-keys.model';
import { UpdateKeysService } from '../service/update-keys.service';

@Injectable({ providedIn: 'root' })
export class UpdateKeysRoutingResolveService implements Resolve<IUpdateKeys> {
  constructor(protected service: UpdateKeysService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUpdateKeys> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((updateKeys: HttpResponse<UpdateKeys>) => {
          if (updateKeys.body) {
            return of(updateKeys.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UpdateKeys());
  }
}
