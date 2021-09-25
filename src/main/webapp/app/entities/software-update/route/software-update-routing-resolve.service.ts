import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISoftwareUpdate, SoftwareUpdate } from '../software-update.model';
import { SoftwareUpdateService } from '../service/software-update.service';

@Injectable({ providedIn: 'root' })
export class SoftwareUpdateRoutingResolveService implements Resolve<ISoftwareUpdate> {
  constructor(protected service: SoftwareUpdateService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISoftwareUpdate> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((softwareUpdate: HttpResponse<SoftwareUpdate>) => {
          if (softwareUpdate.body) {
            return of(softwareUpdate.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SoftwareUpdate());
  }
}
