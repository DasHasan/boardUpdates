import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBoardUpdate, getBoardUpdateIdentifier } from '../board-update.model';

export type EntityResponseType = HttpResponse<IBoardUpdate>;
export type EntityArrayResponseType = HttpResponse<IBoardUpdate[]>;

@Injectable({ providedIn: 'root' })
export class BoardUpdateService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/board-updates');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(boardUpdate: IBoardUpdate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(boardUpdate);
    return this.http
      .post<IBoardUpdate>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(boardUpdate: IBoardUpdate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(boardUpdate);
    return this.http
      .put<IBoardUpdate>(`${this.resourceUrl}/${getBoardUpdateIdentifier(boardUpdate) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(boardUpdate: IBoardUpdate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(boardUpdate);
    return this.http
      .patch<IBoardUpdate>(`${this.resourceUrl}/${getBoardUpdateIdentifier(boardUpdate) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBoardUpdate>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBoardUpdate[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBoardUpdateToCollectionIfMissing(
    boardUpdateCollection: IBoardUpdate[],
    ...boardUpdatesToCheck: (IBoardUpdate | null | undefined)[]
  ): IBoardUpdate[] {
    const boardUpdates: IBoardUpdate[] = boardUpdatesToCheck.filter(isPresent);
    if (boardUpdates.length > 0) {
      const boardUpdateCollectionIdentifiers = boardUpdateCollection.map(boardUpdateItem => getBoardUpdateIdentifier(boardUpdateItem)!);
      const boardUpdatesToAdd = boardUpdates.filter(boardUpdateItem => {
        const boardUpdateIdentifier = getBoardUpdateIdentifier(boardUpdateItem);
        if (boardUpdateIdentifier == null || boardUpdateCollectionIdentifiers.includes(boardUpdateIdentifier)) {
          return false;
        }
        boardUpdateCollectionIdentifiers.push(boardUpdateIdentifier);
        return true;
      });
      return [...boardUpdatesToAdd, ...boardUpdateCollection];
    }
    return boardUpdateCollection;
  }

  protected convertDateFromClient(boardUpdate: IBoardUpdate): IBoardUpdate {
    return Object.assign({}, boardUpdate, {
      releaseDate: boardUpdate.releaseDate?.isValid() ? boardUpdate.releaseDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.releaseDate = res.body.releaseDate ? dayjs(res.body.releaseDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((boardUpdate: IBoardUpdate) => {
        boardUpdate.releaseDate = boardUpdate.releaseDate ? dayjs(boardUpdate.releaseDate) : undefined;
      });
    }
    return res;
  }
}
