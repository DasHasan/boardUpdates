import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBoardUpdateSuccessor, getBoardUpdateSuccessorIdentifier } from '../board-update-successor.model';

export type EntityResponseType = HttpResponse<IBoardUpdateSuccessor>;
export type EntityArrayResponseType = HttpResponse<IBoardUpdateSuccessor[]>;

@Injectable({ providedIn: 'root' })
export class BoardUpdateSuccessorService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/board-update-successors');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(boardUpdateSuccessor: IBoardUpdateSuccessor): Observable<EntityResponseType> {
    return this.http.post<IBoardUpdateSuccessor>(this.resourceUrl, boardUpdateSuccessor, { observe: 'response' });
  }

  update(boardUpdateSuccessor: IBoardUpdateSuccessor): Observable<EntityResponseType> {
    return this.http.put<IBoardUpdateSuccessor>(
      `${this.resourceUrl}/${getBoardUpdateSuccessorIdentifier(boardUpdateSuccessor) as number}`,
      boardUpdateSuccessor,
      { observe: 'response' }
    );
  }

  partialUpdate(boardUpdateSuccessor: IBoardUpdateSuccessor): Observable<EntityResponseType> {
    return this.http.patch<IBoardUpdateSuccessor>(
      `${this.resourceUrl}/${getBoardUpdateSuccessorIdentifier(boardUpdateSuccessor) as number}`,
      boardUpdateSuccessor,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBoardUpdateSuccessor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBoardUpdateSuccessor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBoardUpdateSuccessorToCollectionIfMissing(
    boardUpdateSuccessorCollection: IBoardUpdateSuccessor[],
    ...boardUpdateSuccessorsToCheck: (IBoardUpdateSuccessor | null | undefined)[]
  ): IBoardUpdateSuccessor[] {
    const boardUpdateSuccessors: IBoardUpdateSuccessor[] = boardUpdateSuccessorsToCheck.filter(isPresent);
    if (boardUpdateSuccessors.length > 0) {
      const boardUpdateSuccessorCollectionIdentifiers = boardUpdateSuccessorCollection.map(
        boardUpdateSuccessorItem => getBoardUpdateSuccessorIdentifier(boardUpdateSuccessorItem)!
      );
      const boardUpdateSuccessorsToAdd = boardUpdateSuccessors.filter(boardUpdateSuccessorItem => {
        const boardUpdateSuccessorIdentifier = getBoardUpdateSuccessorIdentifier(boardUpdateSuccessorItem);
        if (boardUpdateSuccessorIdentifier == null || boardUpdateSuccessorCollectionIdentifiers.includes(boardUpdateSuccessorIdentifier)) {
          return false;
        }
        boardUpdateSuccessorCollectionIdentifiers.push(boardUpdateSuccessorIdentifier);
        return true;
      });
      return [...boardUpdateSuccessorsToAdd, ...boardUpdateSuccessorCollection];
    }
    return boardUpdateSuccessorCollection;
  }
}
