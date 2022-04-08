import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUpdatePrecondition } from '../update-precondition.model';
import { UpdatePreconditionService } from '../service/update-precondition.service';

@Component({
  templateUrl: './update-precondition-delete-dialog.component.html',
})
export class UpdatePreconditionDeleteDialogComponent {
  updatePrecondition?: IUpdatePrecondition;

  constructor(protected updatePreconditionService: UpdatePreconditionService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.updatePreconditionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
