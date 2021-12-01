import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDownloadUrl } from '../download-url.model';
import { DownloadUrlService } from '../service/download-url.service';

@Component({
  templateUrl: './download-url-delete-dialog.component.html',
})
export class DownloadUrlDeleteDialogComponent {
  downloadUrl?: IDownloadUrl;

  constructor(protected downloadUrlService: DownloadUrlService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.downloadUrlService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
