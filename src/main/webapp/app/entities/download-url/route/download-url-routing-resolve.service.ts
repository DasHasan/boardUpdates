import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDownloadUrl, DownloadUrl } from '../download-url.model';
import { DownloadUrlService } from '../service/download-url.service';

@Injectable({ providedIn: 'root' })
export class DownloadUrlRoutingResolveService implements Resolve<IDownloadUrl> {
  constructor(protected service: DownloadUrlService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDownloadUrl> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((downloadUrl: HttpResponse<DownloadUrl>) => {
          if (downloadUrl.body) {
            return of(downloadUrl.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DownloadUrl());
  }
}
