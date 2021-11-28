import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { ProfileService } from 'app/layouts/profiles/profile.service';

@Injectable({
  providedIn: 'root',
})
export class FileService {
  constructor(protected http: HttpClient, protected profileService: ProfileService) {}

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

  download(id: number): void {
    this.profileService.getProfileInfo().subscribe(value => {
      if (value.inDev) {
        window.open(`http://localhost:8080/download-update/${id}`, '_blank');
      } else {
        window.open(`/download-update/${id}`, '_blank');
      }
    });
  }
}
