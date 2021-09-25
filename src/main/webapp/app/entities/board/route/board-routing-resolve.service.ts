import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBoard, Board } from '../board.model';
import { BoardService } from '../service/board.service';

@Injectable({ providedIn: 'root' })
export class BoardRoutingResolveService implements Resolve<IBoard> {
  constructor(protected service: BoardService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBoard> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((board: HttpResponse<Board>) => {
          if (board.body) {
            return of(board.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Board());
  }
}
