import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFirmwareUpdate, getFirmwareUpdateIdentifier } from '../firmware-update.model';

export type EntityResponseType = HttpResponse<IFirmwareUpdate>;
export type EntityArrayResponseType = HttpResponse<IFirmwareUpdate[]>;

@Injectable({ providedIn: 'root' })
export class FirmwareUpdateService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/firmware-updates');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(firmwareUpdate: IFirmwareUpdate): Observable<EntityResponseType> {
    return this.http.post<IFirmwareUpdate>(this.resourceUrl, firmwareUpdate, { observe: 'response' });
  }

  update(firmwareUpdate: IFirmwareUpdate): Observable<EntityResponseType> {
    return this.http.put<IFirmwareUpdate>(`${this.resourceUrl}/${getFirmwareUpdateIdentifier(firmwareUpdate) as number}`, firmwareUpdate, {
      observe: 'response',
    });
  }

  partialUpdate(firmwareUpdate: IFirmwareUpdate): Observable<EntityResponseType> {
    return this.http.patch<IFirmwareUpdate>(
      `${this.resourceUrl}/${getFirmwareUpdateIdentifier(firmwareUpdate) as number}`,
      firmwareUpdate,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFirmwareUpdate>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFirmwareUpdate[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFirmwareUpdateToCollectionIfMissing(
    firmwareUpdateCollection: IFirmwareUpdate[],
    ...firmwareUpdatesToCheck: (IFirmwareUpdate | null | undefined)[]
  ): IFirmwareUpdate[] {
    const firmwareUpdates: IFirmwareUpdate[] = firmwareUpdatesToCheck.filter(isPresent);
    if (firmwareUpdates.length > 0) {
      const firmwareUpdateCollectionIdentifiers = firmwareUpdateCollection.map(
        firmwareUpdateItem => getFirmwareUpdateIdentifier(firmwareUpdateItem)!
      );
      const firmwareUpdatesToAdd = firmwareUpdates.filter(firmwareUpdateItem => {
        const firmwareUpdateIdentifier = getFirmwareUpdateIdentifier(firmwareUpdateItem);
        if (firmwareUpdateIdentifier == null || firmwareUpdateCollectionIdentifiers.includes(firmwareUpdateIdentifier)) {
          return false;
        }
        firmwareUpdateCollectionIdentifiers.push(firmwareUpdateIdentifier);
        return true;
      });
      return [...firmwareUpdatesToAdd, ...firmwareUpdateCollection];
    }
    return firmwareUpdateCollection;
  }
}
