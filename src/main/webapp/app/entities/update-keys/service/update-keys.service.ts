import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUpdateKeys, getUpdateKeysIdentifier } from '../update-keys.model';

export type EntityResponseType = HttpResponse<IUpdateKeys>;
export type EntityArrayResponseType = HttpResponse<IUpdateKeys[]>;

@Injectable({ providedIn: 'root' })
export class UpdateKeysService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/update-keys');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(updateKeys: IUpdateKeys): Observable<EntityResponseType> {
    return this.http.post<IUpdateKeys>(this.resourceUrl, updateKeys, { observe: 'response' });
  }

  update(updateKeys: IUpdateKeys): Observable<EntityResponseType> {
    return this.http.put<IUpdateKeys>(`${this.resourceUrl}/${getUpdateKeysIdentifier(updateKeys) as number}`, updateKeys, {
      observe: 'response',
    });
  }

  partialUpdate(updateKeys: IUpdateKeys): Observable<EntityResponseType> {
    return this.http.patch<IUpdateKeys>(`${this.resourceUrl}/${getUpdateKeysIdentifier(updateKeys) as number}`, updateKeys, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUpdateKeys>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUpdateKeys[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUpdateKeysToCollectionIfMissing(
    updateKeysCollection: IUpdateKeys[],
    ...updateKeysToCheck: (IUpdateKeys | null | undefined)[]
  ): IUpdateKeys[] {
    const updateKeys: IUpdateKeys[] = updateKeysToCheck.filter(isPresent);
    if (updateKeys.length > 0) {
      const updateKeysCollectionIdentifiers = updateKeysCollection.map(updateKeysItem => getUpdateKeysIdentifier(updateKeysItem)!);
      const updateKeysToAdd = updateKeys.filter(updateKeysItem => {
        const updateKeysIdentifier = getUpdateKeysIdentifier(updateKeysItem);
        if (updateKeysIdentifier == null || updateKeysCollectionIdentifiers.includes(updateKeysIdentifier)) {
          return false;
        }
        updateKeysCollectionIdentifiers.push(updateKeysIdentifier);
        return true;
      });
      return [...updateKeysToAdd, ...updateKeysCollection];
    }
    return updateKeysCollection;
  }
}
