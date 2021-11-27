import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDownload, getDownloadIdentifier } from '../download.model';

export type EntityResponseType = HttpResponse<IDownload>;
export type EntityArrayResponseType = HttpResponse<IDownload[]>;

@Injectable({ providedIn: 'root' })
export class DownloadService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/downloads');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(download: IDownload): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(download);
    return this.http
      .post<IDownload>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(download: IDownload): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(download);
    return this.http
      .put<IDownload>(`${this.resourceUrl}/${getDownloadIdentifier(download) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(download: IDownload): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(download);
    return this.http
      .patch<IDownload>(`${this.resourceUrl}/${getDownloadIdentifier(download) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDownload>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDownload[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDownloadToCollectionIfMissing(downloadCollection: IDownload[], ...downloadsToCheck: (IDownload | null | undefined)[]): IDownload[] {
    const downloads: IDownload[] = downloadsToCheck.filter(isPresent);
    if (downloads.length > 0) {
      const downloadCollectionIdentifiers = downloadCollection.map(downloadItem => getDownloadIdentifier(downloadItem)!);
      const downloadsToAdd = downloads.filter(downloadItem => {
        const downloadIdentifier = getDownloadIdentifier(downloadItem);
        if (downloadIdentifier == null || downloadCollectionIdentifiers.includes(downloadIdentifier)) {
          return false;
        }
        downloadCollectionIdentifiers.push(downloadIdentifier);
        return true;
      });
      return [...downloadsToAdd, ...downloadCollection];
    }
    return downloadCollection;
  }

  protected convertDateFromClient(download: IDownload): IDownload {
    return Object.assign({}, download, {
      date: download.date?.isValid() ? download.date.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((download: IDownload) => {
        download.date = download.date ? dayjs(download.date) : undefined;
      });
    }
    return res;
  }
}
