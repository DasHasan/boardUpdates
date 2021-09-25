import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFirmware, Firmware } from '../firmware.model';
import { FirmwareService } from '../service/firmware.service';

@Injectable({ providedIn: 'root' })
export class FirmwareRoutingResolveService implements Resolve<IFirmware> {
  constructor(protected service: FirmwareService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFirmware> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((firmware: HttpResponse<Firmware>) => {
          if (firmware.body) {
            return of(firmware.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Firmware());
  }
}
