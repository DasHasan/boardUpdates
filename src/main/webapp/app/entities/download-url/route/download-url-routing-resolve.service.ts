import {Injectable} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {ActivatedRouteSnapshot, Resolve, Router} from '@angular/router';
import {EMPTY, Observable, of} from 'rxjs';
import {mergeMap} from 'rxjs/operators';

import {DownloadUrl, IDownloadUrl} from '../download-url.model';
import {DownloadUrlService} from '../service/download-url.service';
import {BoardUpdate} from 'app/entities/board-update/board-update.model';
import {BoardUpdateService} from 'app/entities/board-update/service/board-update.service';

@Injectable({ providedIn: 'root' })
export class DownloadUrlRoutingResolveService implements Resolve<IDownloadUrl> {
  constructor(protected service: DownloadUrlService, protected boardUpdateService: BoardUpdateService, protected router: Router) {}

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

    const boardUpdateId = route.queryParams['boardUpdateId'];
    if (boardUpdateId) {
      return this.boardUpdateService.find(boardUpdateId).pipe(
        mergeMap((boardUpdateResponse: HttpResponse<BoardUpdate>) => {
          if (boardUpdateResponse.body) {
            return of(new DownloadUrl(undefined, undefined, undefined, boardUpdateResponse.body));
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
