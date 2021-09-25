import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFirmware } from '../firmware.model';
import { FirmwareService } from '../service/firmware.service';

@Component({
  templateUrl: './firmware-delete-dialog.component.html',
})
export class FirmwareDeleteDialogComponent {
  firmware?: IFirmware;

  constructor(protected firmwareService: FirmwareService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.firmwareService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
