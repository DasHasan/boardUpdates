import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDownload, Download } from '../download.model';
import { DownloadService } from '../service/download.service';

@Injectable({ providedIn: 'root' })
export class DownloadRoutingResolveService implements Resolve<IDownload> {
  constructor(protected service: DownloadService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDownload> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((download: HttpResponse<Download>) => {
          if (download.body) {
            return of(download.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Download());
  }
}
