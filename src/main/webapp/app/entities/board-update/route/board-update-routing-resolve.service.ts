import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBoardUpdate, BoardUpdate } from '../board-update.model';
import { BoardUpdateService } from '../service/board-update.service';

@Injectable({ providedIn: 'root' })
export class BoardUpdateRoutingResolveService implements Resolve<IBoardUpdate> {
  constructor(protected service: BoardUpdateService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBoardUpdate> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((boardUpdate: HttpResponse<BoardUpdate>) => {
          if (boardUpdate.body) {
            return of(boardUpdate.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BoardUpdate());
  }
}
