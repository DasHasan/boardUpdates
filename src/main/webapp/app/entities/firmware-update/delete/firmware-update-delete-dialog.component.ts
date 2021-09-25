import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFirmwareUpdate } from '../firmware-update.model';
import { FirmwareUpdateService } from '../service/firmware-update.service';

@Component({
  templateUrl: './firmware-update-delete-dialog.component.html',
})
export class FirmwareUpdateDeleteDialogComponent {
  firmwareUpdate?: IFirmwareUpdate;

  constructor(protected firmwareUpdateService: FirmwareUpdateService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.firmwareUpdateService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
