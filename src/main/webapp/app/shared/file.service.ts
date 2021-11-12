import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class FileService {
  constructor(protected http: HttpClient) {}

  upload(file: File | undefined, filePath: string, fileName: string): Observable<{ path: string }> {
    if (file) {
      const formData = new FormData();
      formData.append('file', file, fileName);
      formData.append('path', filePath);
      return this.http.post<{ path: string }>('/api/file-upload/', formData);
    } else {
      return of({ path: '' });
    }
  }
}
