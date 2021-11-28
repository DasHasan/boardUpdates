import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBoardUpdateSuccessor, BoardUpdateSuccessor } from '../board-update-successor.model';
import { BoardUpdateSuccessorService } from '../service/board-update-successor.service';
import { BoardUpdateService } from 'app/entities/board-update/service/board-update.service';
import { BoardUpdate } from 'app/entities/board-update/board-update.model';

@Injectable({ providedIn: 'root' })
export class BoardUpdateSuccessorRoutingResolveService implements Resolve<IBoardUpdateSuccessor> {
  constructor(protected service: BoardUpdateSuccessorService, protected router: Router, protected boardUpdateService: BoardUpdateService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBoardUpdateSuccessor> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((boardUpdateSuccessor: HttpResponse<BoardUpdateSuccessor>) => {
          if (boardUpdateSuccessor.body) {
            return of(boardUpdateSuccessor.body);
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
        mergeMap((boardUpdate: HttpResponse<BoardUpdate>) => {
          if (boardUpdate.body) {
            const boardUpdateSuccessor = new BoardUpdateSuccessor(undefined, boardUpdate.body, undefined);
            // eslint-disable-next-line no-console
            console.log('boardUpdateSuccessor');
            // eslint-disable-next-line no-console
            console.log(boardUpdateSuccessor);
            return of(boardUpdateSuccessor);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BoardUpdateSuccessor());
  }
}
