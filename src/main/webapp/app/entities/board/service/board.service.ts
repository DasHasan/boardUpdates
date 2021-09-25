import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBoard, getBoardIdentifier } from '../board.model';

export type EntityResponseType = HttpResponse<IBoard>;
export type EntityArrayResponseType = HttpResponse<IBoard[]>;

@Injectable({ providedIn: 'root' })
export class BoardService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/boards');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(board: IBoard): Observable<EntityResponseType> {
    return this.http.post<IBoard>(this.resourceUrl, board, { observe: 'response' });
  }

  update(board: IBoard): Observable<EntityResponseType> {
    return this.http.put<IBoard>(`${this.resourceUrl}/${getBoardIdentifier(board) as number}`, board, { observe: 'response' });
  }

  partialUpdate(board: IBoard): Observable<EntityResponseType> {
    return this.http.patch<IBoard>(`${this.resourceUrl}/${getBoardIdentifier(board) as number}`, board, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBoard>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBoard[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBoardToCollectionIfMissing(boardCollection: IBoard[], ...boardsToCheck: (IBoard | null | undefined)[]): IBoard[] {
    const boards: IBoard[] = boardsToCheck.filter(isPresent);
    if (boards.length > 0) {
      const boardCollectionIdentifiers = boardCollection.map(boardItem => getBoardIdentifier(boardItem)!);
      const boardsToAdd = boards.filter(boardItem => {
        const boardIdentifier = getBoardIdentifier(boardItem);
        if (boardIdentifier == null || boardCollectionIdentifiers.includes(boardIdentifier)) {
          return false;
        }
        boardCollectionIdentifiers.push(boardIdentifier);
        return true;
      });
      return [...boardsToAdd, ...boardCollection];
    }
    return boardCollection;
  }
}
