import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUpdatePrecondition, getUpdatePreconditionIdentifier } from '../update-precondition.model';

export type EntityResponseType = HttpResponse<IUpdatePrecondition>;
export type EntityArrayResponseType = HttpResponse<IUpdatePrecondition[]>;

@Injectable({ providedIn: 'root' })
export class UpdatePreconditionService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/update-preconditions');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(updatePrecondition: IUpdatePrecondition): Observable<EntityResponseType> {
    return this.http.post<IUpdatePrecondition>(this.resourceUrl, updatePrecondition, { observe: 'response' });
  }

  update(updatePrecondition: IUpdatePrecondition): Observable<EntityResponseType> {
    return this.http.put<IUpdatePrecondition>(
      `${this.resourceUrl}/${getUpdatePreconditionIdentifier(updatePrecondition) as number}`,
      updatePrecondition,
      { observe: 'response' }
    );
  }

  partialUpdate(updatePrecondition: IUpdatePrecondition): Observable<EntityResponseType> {
    return this.http.patch<IUpdatePrecondition>(
      `${this.resourceUrl}/${getUpdatePreconditionIdentifier(updatePrecondition) as number}`,
      updatePrecondition,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUpdatePrecondition>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUpdatePrecondition[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUpdatePreconditionToCollectionIfMissing(
    updatePreconditionCollection: IUpdatePrecondition[],
    ...updatePreconditionsToCheck: (IUpdatePrecondition | null | undefined)[]
  ): IUpdatePrecondition[] {
    const updatePreconditions: IUpdatePrecondition[] = updatePreconditionsToCheck.filter(isPresent);
    if (updatePreconditions.length > 0) {
      const updatePreconditionCollectionIdentifiers = updatePreconditionCollection.map(
        updatePreconditionItem => getUpdatePreconditionIdentifier(updatePreconditionItem)!
      );
      const updatePreconditionsToAdd = updatePreconditions.filter(updatePreconditionItem => {
        const updatePreconditionIdentifier = getUpdatePreconditionIdentifier(updatePreconditionItem);
        if (updatePreconditionIdentifier == null || updatePreconditionCollectionIdentifiers.includes(updatePreconditionIdentifier)) {
          return false;
        }
        updatePreconditionCollectionIdentifiers.push(updatePreconditionIdentifier);
        return true;
      });
      return [...updatePreconditionsToAdd, ...updatePreconditionCollection];
    }
    return updatePreconditionCollection;
  }
}
