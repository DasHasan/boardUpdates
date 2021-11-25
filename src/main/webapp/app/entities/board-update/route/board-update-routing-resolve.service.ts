import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBoardUpdate, BoardUpdate } from '../board-update.model';
import { BoardUpdateService } from '../service/board-update.service';
import { BoardService } from 'app/entities/board/service/board.service';
import { Board } from 'app/entities/board/board.model';

@Injectable({ providedIn: 'root' })
export class BoardUpdateRoutingResolveService implements Resolve<IBoardUpdate> {
  constructor(protected service: BoardUpdateService, protected boardService: BoardService, protected router: Router) {}

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

    const boardId = route.queryParams['boardId'];
    if (boardId) {
      return this.boardService.find(boardId).pipe(
        mergeMap((boardResponse: HttpResponse<Board>) => {
          if (boardResponse.body) {
            return of(new BoardUpdate(undefined, undefined, undefined, undefined, undefined, undefined, undefined, boardResponse.body));
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
