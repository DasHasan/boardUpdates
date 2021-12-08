import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { BoardUpdateSuccessor, IBoardUpdateSuccessor } from '../board-update-successor.model';
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
            return of(new BoardUpdateSuccessor(undefined, boardUpdate.body, undefined));
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
