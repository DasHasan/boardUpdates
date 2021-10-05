import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SoftwareService } from 'app/entities/software/service/software.service';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { BoardService } from 'app/entities/board/service/board.service';
import { FirmwareService } from 'app/entities/firmware/service/firmware.service';
import { BoardUpdateService } from 'app/entities/board-update/service/board-update.service';
import { IBoardUpdate } from 'app/entities/board-update/board-update.model';

@Injectable({
  providedIn: 'root',
})
export class UpdateService {
  boardUpdates = new BehaviorSubject<IBoardUpdate[] | null>(null);

  constructor(
    protected boardUpdateService: BoardUpdateService,
    protected boardService: BoardService,
    protected softwareService: SoftwareService,
    protected firmwareService: FirmwareService,
    protected http: HttpClient
  ) {}

  loadAllUpdates(): void {
    this.boardUpdateService
      .query()
      .pipe(tap(next => this.boardUpdates.next(next.body)))
      .subscribe();
  }

  uploadFile(file: File, path: string, fileName: string): Observable<{ path: string }> {
    const formData = new FormData();
    formData.append('file', file, fileName);
    formData.append('path', path);
    return this.http.post<{ path: string }>('/api/file-upload/', formData);
  }
}
