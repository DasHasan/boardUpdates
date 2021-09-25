import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFirmwareUpdate, FirmwareUpdate } from '../firmware-update.model';
import { FirmwareUpdateService } from '../service/firmware-update.service';

@Injectable({ providedIn: 'root' })
export class FirmwareUpdateRoutingResolveService implements Resolve<IFirmwareUpdate> {
  constructor(protected service: FirmwareUpdateService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFirmwareUpdate> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((firmwareUpdate: HttpResponse<FirmwareUpdate>) => {
          if (firmwareUpdate.body) {
            return of(firmwareUpdate.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FirmwareUpdate());
  }
}
