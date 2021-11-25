import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUpdateKeys, UpdateKeys } from '../update-keys.model';
import { UpdateKeysService } from '../service/update-keys.service';
import { BoardUpdateService } from 'app/entities/board-update/service/board-update.service';
import { BoardUpdate } from 'app/entities/board-update/board-update.model';

@Injectable({ providedIn: 'root' })
export class UpdateKeysRoutingResolveService implements Resolve<IUpdateKeys> {
  constructor(protected service: UpdateKeysService, protected boardUpdateService: BoardUpdateService, protected router: Router) {}

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
    const boardUpdateId = route.queryParams['boardUpdateId'];
    if (boardUpdateId) {
      return this.boardUpdateService.find(boardUpdateId).pipe(
        mergeMap((response: HttpResponse<BoardUpdate>) => {
          if (response.body) {
            return of(new UpdateKeys(undefined, undefined, response.body));
          } else {
            this.router.navigate(['404']); // TODO: error handling when boardUpdateId is missing
            return EMPTY;
          }
        })
      );
    }
    return of(new UpdateKeys());
  }
}
