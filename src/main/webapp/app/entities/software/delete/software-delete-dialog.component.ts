import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISoftware } from '../software.model';
import { SoftwareService } from '../service/software.service';

@Component({
  templateUrl: './software-delete-dialog.component.html',
})
export class SoftwareDeleteDialogComponent {
  software?: ISoftware;

  constructor(protected softwareService: SoftwareService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.softwareService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
