import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDownload } from '../download.model';
import { DownloadService } from '../service/download.service';

@Component({
  templateUrl: './download-delete-dialog.component.html',
})
export class DownloadDeleteDialogComponent {
  download?: IDownload;

  constructor(protected downloadService: DownloadService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.downloadService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
