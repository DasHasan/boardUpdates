import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISoftwareUpdate } from '../software-update.model';
import { SoftwareUpdateService } from '../service/software-update.service';

@Component({
  templateUrl: './software-update-delete-dialog.component.html',
})
export class SoftwareUpdateDeleteDialogComponent {
  softwareUpdate?: ISoftwareUpdate;

  constructor(protected softwareUpdateService: SoftwareUpdateService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.softwareUpdateService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
