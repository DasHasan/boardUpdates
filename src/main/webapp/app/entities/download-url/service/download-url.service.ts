import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDownloadUrl, getDownloadUrlIdentifier } from '../download-url.model';

export type EntityResponseType = HttpResponse<IDownloadUrl>;
export type EntityArrayResponseType = HttpResponse<IDownloadUrl[]>;

@Injectable({ providedIn: 'root' })
export class DownloadUrlService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/download-urls');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(downloadUrl: IDownloadUrl): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(downloadUrl);
    return this.http
      .post<IDownloadUrl>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(downloadUrl: IDownloadUrl): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(downloadUrl);
    return this.http
      .put<IDownloadUrl>(`${this.resourceUrl}/${getDownloadUrlIdentifier(downloadUrl) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(downloadUrl: IDownloadUrl): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(downloadUrl);
    return this.http
      .patch<IDownloadUrl>(`${this.resourceUrl}/${getDownloadUrlIdentifier(downloadUrl) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDownloadUrl>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDownloadUrl[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDownloadUrlToCollectionIfMissing(
    downloadUrlCollection: IDownloadUrl[],
    ...downloadUrlsToCheck: (IDownloadUrl | null | undefined)[]
  ): IDownloadUrl[] {
    const downloadUrls: IDownloadUrl[] = downloadUrlsToCheck.filter(isPresent);
    if (downloadUrls.length > 0) {
      const downloadUrlCollectionIdentifiers = downloadUrlCollection.map(downloadUrlItem => getDownloadUrlIdentifier(downloadUrlItem)!);
      const downloadUrlsToAdd = downloadUrls.filter(downloadUrlItem => {
        const downloadUrlIdentifier = getDownloadUrlIdentifier(downloadUrlItem);
        if (downloadUrlIdentifier == null || downloadUrlCollectionIdentifiers.includes(downloadUrlIdentifier)) {
          return false;
        }
        downloadUrlCollectionIdentifiers.push(downloadUrlIdentifier);
        return true;
      });
      return [...downloadUrlsToAdd, ...downloadUrlCollection];
    }
    return downloadUrlCollection;
  }

  protected convertDateFromClient(downloadUrl: IDownloadUrl): IDownloadUrl {
    return Object.assign({}, downloadUrl, {
      expirationDate: downloadUrl.expirationDate?.isValid() ? downloadUrl.expirationDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.expirationDate = res.body.expirationDate ? dayjs(res.body.expirationDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((downloadUrl: IDownloadUrl) => {
        downloadUrl.expirationDate = downloadUrl.expirationDate ? dayjs(downloadUrl.expirationDate) : undefined;
      });
    }
    return res;
  }
}
