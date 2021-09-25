import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFirmware, getFirmwareIdentifier } from '../firmware.model';

export type EntityResponseType = HttpResponse<IFirmware>;
export type EntityArrayResponseType = HttpResponse<IFirmware[]>;

@Injectable({ providedIn: 'root' })
export class FirmwareService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/firmware');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(firmware: IFirmware): Observable<EntityResponseType> {
    return this.http.post<IFirmware>(this.resourceUrl, firmware, { observe: 'response' });
  }

  update(firmware: IFirmware): Observable<EntityResponseType> {
    return this.http.put<IFirmware>(`${this.resourceUrl}/${getFirmwareIdentifier(firmware) as number}`, firmware, { observe: 'response' });
  }

  partialUpdate(firmware: IFirmware): Observable<EntityResponseType> {
    return this.http.patch<IFirmware>(`${this.resourceUrl}/${getFirmwareIdentifier(firmware) as number}`, firmware, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFirmware>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFirmware[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFirmwareToCollectionIfMissing(firmwareCollection: IFirmware[], ...firmwareToCheck: (IFirmware | null | undefined)[]): IFirmware[] {
    const firmware: IFirmware[] = firmwareToCheck.filter(isPresent);
    if (firmware.length > 0) {
      const firmwareCollectionIdentifiers = firmwareCollection.map(firmwareItem => getFirmwareIdentifier(firmwareItem)!);
      const firmwareToAdd = firmware.filter(firmwareItem => {
        const firmwareIdentifier = getFirmwareIdentifier(firmwareItem);
        if (firmwareIdentifier == null || firmwareCollectionIdentifiers.includes(firmwareIdentifier)) {
          return false;
        }
        firmwareCollectionIdentifiers.push(firmwareIdentifier);
        return true;
      });
      return [...firmwareToAdd, ...firmwareCollection];
    }
    return firmwareCollection;
  }
}
