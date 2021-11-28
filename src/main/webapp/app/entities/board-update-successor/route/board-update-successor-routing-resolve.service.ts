import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBoardUpdateSuccessor, BoardUpdateSuccessor } from '../board-update-successor.model';
import { BoardUpdateSuccessorService } from '../service/board-update-successor.service';

@Injectable({ providedIn: 'root' })
export class BoardUpdateSuccessorRoutingResolveService implements Resolve<IBoardUpdateSuccessor> {
  constructor(protected service: BoardUpdateSuccessorService, protected router: Router) {}

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
    return of(new BoardUpdateSuccessor());
  }
}
