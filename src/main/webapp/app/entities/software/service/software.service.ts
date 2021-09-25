import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISoftware, getSoftwareIdentifier } from '../software.model';

export type EntityResponseType = HttpResponse<ISoftware>;
export type EntityArrayResponseType = HttpResponse<ISoftware[]>;

@Injectable({ providedIn: 'root' })
export class SoftwareService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/software');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(software: ISoftware): Observable<EntityResponseType> {
    return this.http.post<ISoftware>(this.resourceUrl, software, { observe: 'response' });
  }

  update(software: ISoftware): Observable<EntityResponseType> {
    return this.http.put<ISoftware>(`${this.resourceUrl}/${getSoftwareIdentifier(software) as number}`, software, { observe: 'response' });
  }

  partialUpdate(software: ISoftware): Observable<EntityResponseType> {
    return this.http.patch<ISoftware>(`${this.resourceUrl}/${getSoftwareIdentifier(software) as number}`, software, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISoftware>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISoftware[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSoftwareToCollectionIfMissing(softwareCollection: ISoftware[], ...softwareToCheck: (ISoftware | null | undefined)[]): ISoftware[] {
    const software: ISoftware[] = softwareToCheck.filter(isPresent);
    if (software.length > 0) {
      const softwareCollectionIdentifiers = softwareCollection.map(softwareItem => getSoftwareIdentifier(softwareItem)!);
      const softwareToAdd = software.filter(softwareItem => {
        const softwareIdentifier = getSoftwareIdentifier(softwareItem);
        if (softwareIdentifier == null || softwareCollectionIdentifiers.includes(softwareIdentifier)) {
          return false;
        }
        softwareCollectionIdentifiers.push(softwareIdentifier);
        return true;
      });
      return [...softwareToAdd, ...softwareCollection];
    }
    return softwareCollection;
  }
}
