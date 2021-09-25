import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISoftwareUpdate, getSoftwareUpdateIdentifier } from '../software-update.model';

export type EntityResponseType = HttpResponse<ISoftwareUpdate>;
export type EntityArrayResponseType = HttpResponse<ISoftwareUpdate[]>;

@Injectable({ providedIn: 'root' })
export class SoftwareUpdateService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/software-updates');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(softwareUpdate: ISoftwareUpdate): Observable<EntityResponseType> {
    return this.http.post<ISoftwareUpdate>(this.resourceUrl, softwareUpdate, { observe: 'response' });
  }

  update(softwareUpdate: ISoftwareUpdate): Observable<EntityResponseType> {
    return this.http.put<ISoftwareUpdate>(`${this.resourceUrl}/${getSoftwareUpdateIdentifier(softwareUpdate) as number}`, softwareUpdate, {
      observe: 'response',
    });
  }

  partialUpdate(softwareUpdate: ISoftwareUpdate): Observable<EntityResponseType> {
    return this.http.patch<ISoftwareUpdate>(
      `${this.resourceUrl}/${getSoftwareUpdateIdentifier(softwareUpdate) as number}`,
      softwareUpdate,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISoftwareUpdate>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISoftwareUpdate[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSoftwareUpdateToCollectionIfMissing(
    softwareUpdateCollection: ISoftwareUpdate[],
    ...softwareUpdatesToCheck: (ISoftwareUpdate | null | undefined)[]
  ): ISoftwareUpdate[] {
    const softwareUpdates: ISoftwareUpdate[] = softwareUpdatesToCheck.filter(isPresent);
    if (softwareUpdates.length > 0) {
      const softwareUpdateCollectionIdentifiers = softwareUpdateCollection.map(
        softwareUpdateItem => getSoftwareUpdateIdentifier(softwareUpdateItem)!
      );
      const softwareUpdatesToAdd = softwareUpdates.filter(softwareUpdateItem => {
        const softwareUpdateIdentifier = getSoftwareUpdateIdentifier(softwareUpdateItem);
        if (softwareUpdateIdentifier == null || softwareUpdateCollectionIdentifiers.includes(softwareUpdateIdentifier)) {
          return false;
        }
        softwareUpdateCollectionIdentifiers.push(softwareUpdateIdentifier);
        return true;
      });
      return [...softwareUpdatesToAdd, ...softwareUpdateCollection];
    }
    return softwareUpdateCollection;
  }
}
