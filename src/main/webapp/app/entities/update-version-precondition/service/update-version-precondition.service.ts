import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUpdateVersionPrecondition, getUpdateVersionPreconditionIdentifier } from '../update-version-precondition.model';

export type EntityResponseType = HttpResponse<IUpdateVersionPrecondition>;
export type EntityArrayResponseType = HttpResponse<IUpdateVersionPrecondition[]>;

@Injectable({ providedIn: 'root' })
export class UpdateVersionPreconditionService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/update-version-preconditions');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(updateVersionPrecondition: IUpdateVersionPrecondition): Observable<EntityResponseType> {
    return this.http.post<IUpdateVersionPrecondition>(this.resourceUrl, updateVersionPrecondition, { observe: 'response' });
  }

  update(updateVersionPrecondition: IUpdateVersionPrecondition): Observable<EntityResponseType> {
    return this.http.put<IUpdateVersionPrecondition>(
      `${this.resourceUrl}/${getUpdateVersionPreconditionIdentifier(updateVersionPrecondition) as number}`,
      updateVersionPrecondition,
      { observe: 'response' }
    );
  }

  partialUpdate(updateVersionPrecondition: IUpdateVersionPrecondition): Observable<EntityResponseType> {
    return this.http.patch<IUpdateVersionPrecondition>(
      `${this.resourceUrl}/${getUpdateVersionPreconditionIdentifier(updateVersionPrecondition) as number}`,
      updateVersionPrecondition,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUpdateVersionPrecondition>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUpdateVersionPrecondition[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUpdateVersionPreconditionToCollectionIfMissing(
    updateVersionPreconditionCollection: IUpdateVersionPrecondition[],
    ...updateVersionPreconditionsToCheck: (IUpdateVersionPrecondition | null | undefined)[]
  ): IUpdateVersionPrecondition[] {
    const updateVersionPreconditions: IUpdateVersionPrecondition[] = updateVersionPreconditionsToCheck.filter(isPresent);
    if (updateVersionPreconditions.length > 0) {
      const updateVersionPreconditionCollectionIdentifiers = updateVersionPreconditionCollection.map(
        updateVersionPreconditionItem => getUpdateVersionPreconditionIdentifier(updateVersionPreconditionItem)!
      );
      const updateVersionPreconditionsToAdd = updateVersionPreconditions.filter(updateVersionPreconditionItem => {
        const updateVersionPreconditionIdentifier = getUpdateVersionPreconditionIdentifier(updateVersionPreconditionItem);
        if (
          updateVersionPreconditionIdentifier == null ||
          updateVersionPreconditionCollectionIdentifiers.includes(updateVersionPreconditionIdentifier)
        ) {
          return false;
        }
        updateVersionPreconditionCollectionIdentifiers.push(updateVersionPreconditionIdentifier);
        return true;
      });
      return [...updateVersionPreconditionsToAdd, ...updateVersionPreconditionCollection];
    }
    return updateVersionPreconditionCollection;
  }
}
