import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormGroup } from '@angular/forms';
import { SoftwareService } from 'app/entities/software/service/software.service';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { ISoftware, Software } from 'app/entities/software/software.model';
import { BoardService } from 'app/entities/board/service/board.service';
import { FirmwareService } from 'app/entities/firmware/service/firmware.service';
import { Firmware, IFirmware } from 'app/entities/firmware/firmware.model';
import { IBoard } from 'app/entities/board/board.model';

@Injectable({
  providedIn: 'root',
})
export class UpdateService {
  constructor(
    protected boardService: BoardService,
    protected softwareService: SoftwareService,
    protected firmwareService: FirmwareService,
    protected http: HttpClient
  ) {}

  save(formGroup: FormGroup, softwareFile: File | undefined, firmwareFile: File | undefined): void {
    const data = {
      softwareVersion: formGroup.get('softwareVersion')!.value as string,
      firmwareVersion: formGroup.get('firmwareVersion')!.value as string,
      board: formGroup.get('board')!.value,
    };

    if (softwareFile) {
      this.uploadFile(softwareFile, 'upload-software', data.board, data.softwareVersion)
        .pipe(switchMap(({ path }) => this.softwareService.create(this.createSoftware(data.board, data.softwareVersion, path))))
        .subscribe(value => {
          // eslint-disable-next-line no-console
          console.log(value);
        });
    }

    if (firmwareFile) {
      this.uploadFile(firmwareFile, 'upload-firmware', data.board, data.firmwareVersion)
        .pipe(switchMap(({ path }) => this.firmwareService.create(this.createFirmware(data.board, data.firmwareVersion, path))))
        .subscribe(value => {
          // eslint-disable-next-line no-console
          console.log(value);
        });
    }
  }

  private createSoftware(board: any, version: string, path: string): ISoftware {
    return {
      ...new Software(),
      board,
      version,
      path,
    };
  }

  private createFirmware(board: any, version: string, path: string): IFirmware {
    return {
      ...new Firmware(),
      board,
      version,
      path,
    };
  }

  private uploadFile(softwareFile: File, url: string, board: IBoard, version: string): Observable<{ path: string }> {
    // eslint-disable-next-line no-console
    console.log(board);

    const formData = new FormData();
    formData.append('file', softwareFile, `autoupdate_${version}.zip`);
    formData.append('boardSerial', board.serial!);
    formData.append('version', version);
    return this.http.post<{ path: string }>('/api/file-upload/' + url, formData);
  }
}
