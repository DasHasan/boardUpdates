import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUpdateVersionPrecondition } from '../update-version-precondition.model';
import { UpdateVersionPreconditionService } from '../service/update-version-precondition.service';

@Component({
  templateUrl: './update-version-precondition-delete-dialog.component.html',
})
export class UpdateVersionPreconditionDeleteDialogComponent {
  updateVersionPrecondition?: IUpdateVersionPrecondition;

  constructor(protected updateVersionPreconditionService: UpdateVersionPreconditionService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.updateVersionPreconditionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
